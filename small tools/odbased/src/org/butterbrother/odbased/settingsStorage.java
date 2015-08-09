package org.butterbrother.odbased;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * Отвечает за чтение настроек из файла настроек,
 * хранения и отдачу параметров.
 */
public class settingsStorage implements staticValues {
    // Параметры
    private Properties settings;
    // Файл запроса
    private String sqlFileName;

    /**
     * Инициализация с чтением файла настроек.
     * Неудачное чтение настроек аварийно завершит работу приложения
     *
     * @param configFileName Файл конфигурации
     * @param sqlFileName    Файл с запросом
     */
    public settingsStorage(String configFileName, String sqlFileName) {
        this.sqlFileName = sqlFileName;
        try (BufferedReader settingsReader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(configFileName))), 4096)) {
            settings = new Properties();
            settings.load(settingsReader);
        } catch (IOException e) {
            System.err.println("Unable to open configuration file " + configFileName + ": " + e);
            System.exit(EXIT_ERR_READ_CONFIG);
        }
    }

    /**
     * Получение параметра по его имени.
     * Регистр имени параметра при этом игнорируется.
     *
     * @param parameterName Имя параметра, ключ
     * @return Значение параметра
     * @throws ParameterNotFoundException Параметр не найден
     */
    public String getParameter(String parameterName) throws ParameterNotFoundException {
        for (Map.Entry<Object, Object> item : settings.entrySet()) {
            if (item.getKey().toString().equalsIgnoreCase(parameterName))
                return item.getValue().toString();
        }

        throw new ParameterNotFoundException();
    }

    /**
     * Проверка наличия параметров для сервиса.
     * Отдаёт набор настроек.
     *
     * @return Совокупность настроек для сервиса
     */
    public ServiceSettings checkServiceSettings() {
        String URL = "", login = "", password = "", driver = null;
        Path pid;
        int port;
        try {
            URL = getParameter("URL");
        } catch (ParameterNotFoundException e) {
            System.err.println("ERROR: Database JDBC url not set. Set URL= parameter");
            System.exit(EXIT_ERR_READ_CONFIG);
        }

        try {
            login = getParameter("login");
        } catch (ParameterNotFoundException e) {
            System.out.println("WARN: Login not set. Use login= parameter");
        }

        try {
            password = getParameter("password");
        } catch (ParameterNotFoundException e) {
            System.out.println("WARN: Password not set. Use password= parameter");
        }

        try {
            driver = getParameter("driver");
        } catch (ParameterNotFoundException e) {
            System.out.println("WARN: JDBC driver not set. Use driver= parameter");
        }

        port = getPortParameter();

        pid = getPidPathParameter();

        // Проверяем, есть ли уже запущенный процесс
        checkPidAvaliability(true, pid);

        return new ServiceSettings(URL, login, password, driver, port, pid);
    }

    /**
     * Проверяет наличие параметров для клиента
     *
     * @return  Совокупность параметров клиента
     */
    public ClientSettings checkClientSettings() {
        int port = getPortParameter();
        Path pid = getPidPathParameter();

        // Проверяем, есть ли уже запущенный процесс
        checkPidAvaliability(false, pid);

        // Если существует файл с запросом - считываем запрос и сохраняем
        String sqlRequest = "";
        if (sqlFileName != null && ! sqlFileName.isEmpty()) {
            Path sqlFile = Paths.get(sqlFileName);
            if (Files.exists(sqlFile) && Files.isRegularFile(sqlFile)) {
                try (BufferedReader sqlCommandReader = new BufferedReader(Files.newBufferedReader(sqlFile, StandardCharsets.UTF_8))) {
                    String buffer;
                    StringBuilder cmdReader = new StringBuilder();
                    while ((buffer = sqlCommandReader.readLine()) != null)
                        cmdReader.append(buffer).append('\n');
                    sqlRequest = cmdReader.toString();
                } catch (IOException e) {
                    System.err.println("ERROR: unable to read sql commands file " + e);
                    sqlRequest = "";
                }
            }
        }

        return new ClientSettings(pid, port, sqlRequest);
    }

    /**
     * Проверка на наличие-отсутствие уже активного сервиса.
     * Действует в зависимости от режима работы.
     * Если клиент и нет pid-файла - аварийное завершение, сервис не запущен.
     * Если сервер и есть pid-файл - аналогично, сервис уже запущен.
     *
     * @param isServerMode  Это режим сервера?
     * @param pid           PID-файл
     */
    private void checkPidAvaliability(boolean isServerMode, Path pid) {
        if (isServerMode && Files.exists(pid)) {
            System.err.println("ERROR: process already run");
            System.exit(EXIT_ERR_STARTUP);
        }
        if (! isServerMode && Files.notExists(pid)) {
            System.err.println("ERROR: process not started");
            System.exit(EXIT_ERR_STARTUP);
        }
    }

    /**
     * Проверяет наличие параметра расположения PID-файла в файле конфигурации
     * Если параметр есть - будет отдан, иначе будет отдан путь по-умолчанию
     *
     * @return Расположение PID-файла.
     */
    private Path getPidPathParameter() {
        try {
            return Paths.get(getParameter("pid"));
        } catch (ParameterNotFoundException e) {
            System.out.println("WARN: PID file path not set. It file contain secret phrase. Use pid= to set it path. Using default -" + pidDefaultPath);
            return Paths.get(pidDefaultPath);
        }
    }

    /**
     * Проверяет наличие параметра порта обработчика.
     * Если есть параметр - будет отдан, иначе будет отдан порт по-умолчанию
     *
     * @return Порт обаботчика
     */
    private int getPortParameter() {
        try {
            return Integer.parseInt(getParameter("port"));
        } catch (NumberFormatException e) {
            System.out.println("WARN: Incorrect port: " + e);
        } catch (ParameterNotFoundException e) {
            System.out.println("INFO: Using port default port to inbound local connection");
        }
        return defaultPort;
    }

    /**
     * Настройки для сервиса
     */
    public class ServiceSettings {
        public String URL, login, password, driver;
        public Path pidFile;
        public int port;

        private ServiceSettings(String URL, String login, String password, String driver, int port, Path pidFile) {
            this.URL = URL;
            this.login = login;
            this.password = password;
            this.driver = driver;
            this.port = port;
            this.pidFile = pidFile;
        }
    }

    /**
     * Настройки для клиента
     */
    public class ClientSettings {
        public Path pidFile;
        public int port;
        public String sqlRequest;

        private ClientSettings(Path pidFile, int port, String sqlRequest) {
            this.pidFile = pidFile;
            this.port = port;
            this.sqlRequest = sqlRequest;
        }
    }
}
