package com.github.butterbrother.zajdag.core;

/**
 * Интерфейс для логгера
 */
public interface logger {
    // Запись различного уровня логов
    void debug(Object ... message);
    void info(Object ... message);
    void warning(Object ... message);
    void warning(Exception error, Object ... additionalInfo);
    void error(Exception error);
    void error(Exception error, Object ... additionalInfo);
}
