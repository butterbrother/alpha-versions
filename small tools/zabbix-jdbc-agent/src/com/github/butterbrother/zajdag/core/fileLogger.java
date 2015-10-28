package com.github.butterbrother.zajdag.core;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Calendar;

/**
 * Стандартное логгирование в файл
 */
public class fileLogger
    implements logger {

    // Путь к файлу лога
    private Path logPath;
    // Дата создания файла лога
    private Calendar logCreateDate;

    // Это - основной логгер
    private boolean isCoreLogger;
    // Ссылка на основной логгер, если это логгер модуля
    private fileLogger coreLogger;

    // Файл лога
    private PrintWriter log;

    public fileLogger(String logPath) {

    }
}
