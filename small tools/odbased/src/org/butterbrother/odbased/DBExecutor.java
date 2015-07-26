package org.butterbrother.odbased;

import java.sql.*;

/**
 * Выполняет подключение к БД с помощью JDBC,
 * поддерживает постоянное соединение.
 * Выполняет запросы, получаемые от сокета.
 */
public class DBExecutor implements staticValues, Runnable {
    // Параметры подключения к БД
    private String DBURL;
    private String login;
    private String password;

    // Состояние работы
    private boolean activity = false;
    private boolean connectionActive = false;
    private Thread dbExecutorProcess;

    // Активное соединение
    Connection currentConnection = null;

    /**
     * Инициализация.
     * Неудачная инициализация (ошибка загрузки драйвера) приведёт к аварийному завершению приложения
     *
     * @param databaseURL       JDBC-URL
     * @param userName          Логин
     * @param userPassword      Пароль
     * @param JDBCDriver        Имя драйвера
     */
    public DBExecutor(String databaseURL, String userName, String userPassword, String JDBCDriver) {
        // Регистрируем драйвер
        try {
            Driver driver = (Driver) Class.forName(JDBCDriver).newInstance();
            DriverManager.registerDriver(driver);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println("Unable to load JDBC driver " + JDBCDriver + ": " + e);
            System.exit(EXIT_ERR_STARTUP);
        } catch (SQLException e) {
            System.err.println("Unable to register JDBC driver " + JDBCDriver + ": " + e);
            System.exit(EXIT_ERR_STARTUP);
        }

        // Сохраняем настройки подключения
        DBURL = databaseURL;
        login = userName;
        password = userPassword;

        // Создаём собственный рабочий поток
        dbExecutorProcess = new Thread(this, "DB Executor");
        dbExecutorProcess.start();
    }

    /**
     * Основное рабочее состояние
     */
    @Override
    public void run() {
        activity = true;
        while (activity) {
            System.out.println("Trying to connect");
            // Подключаемся и переподключаемся
            try (Connection con = DriverManager.getConnection(DBURL, login, password)) {
                System.out.println("Connected");
                // Сохраняем ссылку и меняем статус подключения
                currentConnection = con;
                connectionActive = true;
                // Держим соединение, пока оно активно
                while (connectionActive && activity) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException inE) {
                        System.exit(EXIT_INTERRUPTED);
                    }
                }
            } catch (SQLException conE) {
                connectionActive = false;
                System.err.println("Connection error: " + conE);
                // Замираем перед следующей попыткой переподключиться
                try {
                    if (activity) Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.exit(EXIT_INTERRUPTED);
                }
            }
            System.out.println("Disconnected from DB");
        }
    }

    /**
     * Отключение исполнителя запросов.
     * Ожидается завершение работы процесса
     */
    synchronized public void switchOff() {
        System.out.println("Switch off DB connections");
        activity = false;
        try {
            dbExecutorProcess.join();
        } catch (InterruptedException e) {
            System.exit(EXIT_INTERRUPTED);
        }
    }

    /**
     * Выполнение запроса
     * @param query     Запрос
     * @return          Результат запроса
     */
    public String executeQuery(String query) {
        if (activity && connectionActive && currentConnection != null) {
            try (Statement stat = currentConnection.createStatement()) {
                // Исполняем запрос, генерируем и отдаём результат
                ResultSet results = stat.executeQuery(query);
                ResultSetMetaData meta = results.getMetaData();
                StringBuilder retValue = new StringBuilder();

                if (meta.getColumnCount() > 0) {
                    while (results.next()) {
                        for (int i = 1; i <= meta.getColumnCount(); i++) {
                            retValue.append(results.getString(i));
                            if (i < meta.getColumnCount()) retValue.append("\t");
                        }
                    }
                    if (! results.isLast()) retValue.append("\n");
                }

                return retValue.toString();
            } catch (SQLException statE) {
                return "ERR: execute error: " + statE;
            }
        }
        return "ERR: Not connected";
    }
}
