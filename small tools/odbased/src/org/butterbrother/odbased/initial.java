package org.butterbrother.odbased;

/**
 * Основной инициализирующий процесс
 */
public class initial implements staticValues {
    public static void main(String args[]) {
        cmdLineParser cmdlines = new cmdLineParser(args);

        if (cmdlines.getWorkMode() == WORK_MODE_HELP)
            showHelp();

        settingsStorage config = new settingsStorage(cmdlines.getConfigPath());

        switch (cmdlines.getWorkMode()) {
            case WORK_MODE_START:
        }
    }

    /**
     * Отображение справки по параметрам запуска
     */
    public static void showHelp() {
        // Тут будет справка
        System.exit(0);
    }

    public static void startStopRestart(int workMode) {

    }
}
