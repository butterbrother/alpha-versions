package com.github.butterbrother.zajdag.core;

/**
 * Интерфейс для логгера
 */
public interface logger {
    /**
     * Отладочная запись в лог массива из байт.
     * В лог пишется HEX-представление
     *
     * @param byteArray     Массив байт
     * @param message       Комментарий или описание
     */
    void debug(byte[] byteArray, Object ... message);

    /**
     * Отладочная запись в лог
     *
     * @param message   Сообщение
     */
    void debug(Object... message);

    /**
     * Информационная запись в лог
     *
     * @param message   Сообщение
     */
    void info(Object... message);

    /**
     * Предупреждение в лог
     *
     * @param message   Сообщение
     */
    void warning(Object... message);

    /**
     * Предупреждение
     *
     * @param error             Полученное исключение
     * @param additionalInfo    Дополнительная информация
     */
    void warning(Exception error, Object... additionalInfo);

    /**
     * Сообщение об ошибке
     *
     * @param message       Сообщение
     */
    void error(Object ... message);

    /**
     * Сообщение об ошибке с исключением
     *
     * @param error             Полученное исключение
     * @param additionalInfo    Дополнительная информация
     */
    void error(Exception error, Object... additionalInfo);

    /**
     * Создание именованного логгера для определённого модуля.
     * Все записи будут дополняться префиксом с его именем.
     * Для визуального отделения в логе.
     *
     * @param moduleName    Имя модуля
     * @return              Именованный логгер
     */
    logger getModuleLogger(String moduleName);
}
