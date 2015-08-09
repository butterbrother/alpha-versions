package org.butterbrother.odbased;

/**
 * Основной инициализирующий процесс
 */
public class initial implements staticValues {
    public static void main(String args[]) {
        // Обрабатываем параметры командной строки
        cmdLineParser cmdlines = new cmdLineParser(args);
        if (cmdlines.getWorkMode() == WORK_MODE_HELP)
            showHelp();

        // Считываем настройки
        settingsStorage config = new settingsStorage(cmdlines.getConfigPath(), cmdlines.getQueryPath());

        // Работаем исходя из режима работы
        switch (cmdlines.getWorkMode()) {
            // Режимы работы клиента
            case WORK_MODE_CLIENT:
            case WORK_MODE_STOP:
            case WORK_MODE_RESTART:
                new clientProcess().execute(config.checkClientSettings(), cmdlines.getWorkMode());
                break;
            // Режим работы сервиса
            case WORK_MODE_START:
                new serviceProcess().execute(config);
        }
    }

    /**
     * Отображение справки по параметрам запуска
     */
    private static void showHelp() {
        // Тут будет справка
        System.exit(0);
    }
}
