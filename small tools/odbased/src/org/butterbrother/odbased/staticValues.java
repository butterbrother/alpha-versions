package org.butterbrother.odbased;

/**
 * Константы
 */
public interface staticValues {
    // Режимы работы
    int WORK_MODE_START = 0;
    int WORK_MODE_STOP = 1;
    int WORK_MODE_CLIENT = 2;
    int WORK_MODE_HELP = 3;
    int WORK_MODE_RESTART = 4;
    int WORK_MODE_DEFAULT = WORK_MODE_CLIENT;

    // Имя файла конфигурации по-умолчанию
    String DEFAULT_CONFIG_PATH = "odbased.properties";

    // Коды возврата
    int EXIT_OK = 0;
    int EXIT_ERR_READ_CONFIG = 1;
    int EXIT_ERR_STARTUP = 2;
    int EXIT_INTERRUPTED = 3;
    int EXIT_RUNTIME_ERROR = 4;

    // Строка, завершающая запрос и ответ
    String BREAK_STRING = "//<BREAK>//";
    // Строка останова приложения
    String STOP_STRING = "//<STOP>//";
    // Строка перезапуска приложения
    String RESTART_STRING = "//<RESTART>//";

    // Настройки по-умолчанию
    int defaultPort = 11000;   // Порт
    String pidDefaultPath = "odbcd.pid";    // pid-файл
}
