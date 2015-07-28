package org.butterbrother.odbased;

/**
 * Получение данных из командной строки
 */
public class cmdLineParser implements staticValues {
    // Режим работы приложения
    private int workMode = WORK_MODE_DEFAULT;
    // Путь к файлу конфигурации
    private String configPath = DEFAULT_CONFIG_PATH;
    // Путь к файлу с SQL-запросом. Пустой, считываем из stdin
    private String queryPath = "";

    public cmdLineParser(String args[]) {
        boolean nextIsConfig = false;   // Следующий параметр - конфиг
        boolean nextIsQuery = false;    // Следующий параметр - файл с запросом

        // Парсим аргументы командной строки
        parsing_args:
        {
            for (String arg : args) {
                if (!nextIsConfig && !nextIsQuery) {
                    switch (arg.toLowerCase()) {
                        case "s":
                        case "start":
                            workMode = WORK_MODE_START;
                            break;
                        case "t":
                        case "stop":
                            workMode = WORK_MODE_STOP;
                            break;
                        case "r":
                        case "restart":
                            workMode = WORK_MODE_RESTART;
                            break;
                        case "-c":
                        case "--config":
                            nextIsConfig = true;
                            break;
                        case "-q":
                        case "--query":
                            nextIsQuery = true;
                            break;
                        default:
                            System.err.println("Unknown parameter - " + arg);
                        case "-h":
                        case "--help":
                            workMode = WORK_MODE_HELP;
                            break parsing_args;
                    }
                } else if (nextIsConfig) {
                    configPath = arg;
                    nextIsConfig = false;
                } else {
                    queryPath = arg;
                    nextIsQuery = false;
                }
            }
        }
    }

    /**
     * Получение режима работы.
     * <p/>
     * Режимы работы описаны в staticValues
     *
     * @return заданный режим работы
     */
    public int getWorkMode() {
        return workMode;
    }

    /**
     * Получение пути и имени файла конфигурации
     *
     * @return Путь к файлу конфигурации
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * Получение пути и имени файла с SQL-запросом
     *
     * @return Путь к файлу с SQL-запросом
     */
    public String getQueryPath() {
        return queryPath;
    }
}
