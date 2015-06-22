package org.litecli;

import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Простейший CLI-клиент для JDBC-драйвера
 */
public class core {

    /**
     * Основа
     */
    public static void main(String args[]) {
        /*
        Парсим входные параметры
         */
        // Признаки:
        boolean nextConfig = false; // следующий параметр будет значением файла конфигурации
        boolean nextSQL = false;    // следующий параметр будет значением файла с запросами
        boolean nextOut = false;    // следующий параметр будет значение выходного файла
        boolean nextDelim = false;  // следующий параметр будет значением разделителя столбцов

        boolean showHeaders = false; // Показывать заголовки столбцов результатов запросов
        String configFile = "litecli.properties"; // Имя файла конфигурации
        String sqlFile = "";    // Имя файла с запросами
        String outFile = "";    // Имя файла с выводом
        String delimiter = ";"; // Разделитель столбцов

        // Непосредственно парсим параметры
        for (String item : args)
            switch (item) {
                case "-c":
                case "--config":
                    nextConfig = true;
                    break;
                case "-s":
                case "--sql":
                    nextSQL = true;
                    break;
                case "-o":
                case "--output":
                    nextOut = true;
                    break;
                case "-d":
                case "--delimiter":
                    nextDelim = true;
                    break;
                case "-e":
                case "--headers":
                    showHeaders = true;
                    break;
                default:
                    if (nextConfig) {
                        configFile = item;
                        nextConfig = false;
                    } else if (nextSQL) {
                        sqlFile = item;
                        nextSQL = false;
                    } else if (nextOut) {
                        outFile = item;
                        nextOut = false;
                    } else if (nextDelim) {
                        delimiter = item;
                        nextDelim = false;
                    } else {
                        System.err.println("Unable to parse parameter: " + item);
                        System.exit(1);
                    }
            }

        // Читаем конфиг
        Properties params = new Properties();
        try (FileInputStream inputFile = new FileInputStream(new File(configFile))) {
            params.load(inputFile);
        } catch (IOException err) {
            System.err.println("Unable to read config file:");
            err.printStackTrace();
            System.exit(2);
        }

        // Конвертируем запросы, передаём на исполнение
        connector(params, sqlReader(sqlFile), delimiter, outFile, showHeaders);

    }

    /**
     * Парсит файлы с запросами. Либо интерактивный ввод/pipe
     * @param sqlFileName   Имя файла с запросами. Если пуст - выполняется интерактивный ввод
     * @return  Список запросов в формате LinkedList
     */
    private static LinkedList<String> sqlReader(String sqlFileName) {
        // Необработанный список запросов
        StringBuilder RAWrequests = new StringBuilder();

        if (!sqlFileName.equals("")) {
            /*
            Неинтерактивный ввод, из файла.
            Считываем файл в список
             */
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(sqlFileName))))) {
                String buffer;
                while ((buffer = reader.readLine()) != null) {
                    RAWrequests.append(buffer);
                }
            } catch (FileNotFoundException err) {
                System.err.println("SQL file not found: " + err);
                err.printStackTrace();
                System.exit(2);
            } catch (IOException err) {
                System.err.println("Unable to open or read file: " + err);
                err.printStackTrace();
                System.exit(2);
            }
        } else {
            /*
            Интерактивный ввод либо ввод из pipe
             */
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                // Если не pipe (буфер пуст), то выводим приглашение
                boolean showPrompt = (System.in.available() == 0);

                String buffer;
                if (showPrompt) System.out.print("Enter SQL query (Ctrl+D and Enter or \"EXIT;\" and Enter to end):\n>> ");
                while ((buffer = reader.readLine()) != null) {
                    if (showPrompt && buffer.equalsIgnoreCase("exit;")) break;
                    RAWrequests.append(buffer);
                    if (showPrompt) System.out.print(">> ");
                }
            } catch (IOException err) {
                System.err.println("I/O error: " + err);
                err.printStackTrace();
                System.exit(2);
            }
        }

        /*
        Далее разделяем на запросы (отделены точкой с запятой)
        */
        StringTokenizer separator = new StringTokenizer(RAWrequests.toString(), ";");
        LinkedList<String> batchCommands = new LinkedList<>();
        while (separator.hasMoreElements())
            batchCommands.add(separator.nextToken());

        return batchCommands;
    }

    /**
     * Подключение, запись результатов, и т.д.
     * @param connectionProperties  Параметры соединения из файла
     * @param requests              Запросы
     * @param delimiter             Разделитель колонок
     * @param outputFile            Выходной файл
     */
    private static void connector(Properties connectionProperties, LinkedList<String> requests, String delimiter, String outputFile, boolean showHeaders) {
        // Проверяем наличие параметров
        // Драйвер
        String driverName = connectionProperties.getProperty("driver", "");
        if (! driverName.isEmpty()) {
            try {
                DriverManager.registerDriver((Driver) Class.forName(driverName).newInstance());
            } catch (ClassNotFoundException err) {
                System.err.println("Unable to load driver (Not found): " + err);
                err.printStackTrace();
                System.exit(1);
            } catch (InstantiationException err) {
                System.err.println("Unable to load driver (Driver load error): " + err);
                err.printStackTrace();
                System.exit(1);
            } catch (IllegalAccessException err) {
                System.err.println("Unable to load driver (Access error): " + err);
                err.printStackTrace();
                System.exit(1);
            } catch (SQLException err) {
                System.err.println("Unable to load JDBC driver: " + err);
                err.printStackTrace();
                System.exit(1);
            }
        }
        // Строка подключения
        String url = connectionProperties.getProperty("url", "");
        if (url.isEmpty()) {
            System.err.println("Connection url not set");
            System.exit(1);
        }
        String login = connectionProperties.getProperty("login", "");
        String password = connectionProperties.getProperty("password", "");

        /*
        Подключение и запуск запросов
         */
        try (Connection sqlConnect = DriverManager.getConnection(url, login, password);
        Statement statement = sqlConnect.createStatement()) {
            if (! outputFile.isEmpty()) {
                // Запись результатов в файл (выходной файл указан)
                try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFile))))) {
                    for (String query : requests) {
                        out.append(executor(statement, delimiter, query, showHeaders)).append('\n');
                    }
                } catch (IOException err) {
                    System.err.println("Error writing to output file: " + err);
                    err.printStackTrace();
                }
            } else {
                // Запись результатов в стандартный поток (выходной файл не указан)
                for (String query : requests) {
                    System.out.println(executor(statement, delimiter, query, showHeaders));
                }
            }
        } catch (SQLException err) {
            System.err.println("SQL error: " + err);
            err.printStackTrace();
            System.exit(3);
        }
    }

    /**
     * Непосредственное исполнение запросов
     * @param statement Выражение SQL
     * @param delimiter Разделитель
     * @param query     Запрос
     * @return          Результат запроса
     * @throws SQLException
     */
    private static String executor(Statement statement, String delimiter, String query, boolean showHeaders) throws SQLException {
        StringBuilder results = new StringBuilder();

        // Выполняем запрос, получаем результат и метаданные
        ResultSet queryResults = statement.executeQuery(query);
        ResultSetMetaData queryMetaData = queryResults.getMetaData();

        // Отображаем заголовок таблицы запроса, если задано
        if (showHeaders) {
            if (queryMetaData.getColumnCount() != 0)
                // Перебираем заголовки
                for (int row = 1; row <= queryMetaData.getColumnCount(); row ++) {
                    // И пишем имя каждого
                    results.append(queryMetaData.getColumnName(row));
                    // Ставим разделители, если только это не последний столбец
                    if (row != queryMetaData.getColumnCount())
                        results.append(delimiter);
                }
            results.append('\n');
        }

        // Перебираем строки
        while (queryResults.next()) {
            if (queryMetaData.getColumnCount() != 0)
                // И столбцы
                for (int row = 1; row <= queryMetaData.getColumnCount(); row++) {
                    // И записываем их
                    results.append(queryResults.getString(row));
                    // Ставим разделители если только это не последний столбец
                    if (row != queryMetaData.getColumnCount())
                        results.append(delimiter);
                }
            results.append('\n');   // Вставляем перенос после записи каждой строки
        }
        return results.toString();
    }
}
