package com.github.butterbrother.s2s_connection_checker.Core;

/*
* Copyright (C) 2016  Oleg Bobukh <o.bobukh@yandex.ru>
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software Foundation,
* Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.LogManager;

/**
 * Выполняет динамическую генерацию файла конфигурации для логгера JDK.
 */
public class LogManagerChanger {
    /**
     * Параметры по-умолчанию
     */
    private static String[][] DefaultProperties = {
            // Формат логгирования
            {"java.util.logging.SimpleFormatter.format", "<%1$tF %<tT.%<tL> <%4$s> <%3$s> %5$s %6$s%n"},

            // Настройки глобального логгера
            // Список хендлеров. По-умолчанию выводим только в консоль
            {"handlers", "java.util.logging.ConsoleHandler"},
            // Уровни по-умолчанию - info
            {".level", "INFO"},

            // Конфигурация файлового хендлера
            // Уровень логгирования для файлов
            {"java.util.logging.FileHandler.level", "INFO"},
            {"java.util.logging.FileHandler.formatter", "java.util.logging.SimpleFormatter"},
            {"java.util.logging.FileHandler.limit", "10485760"},
            {"java.util.logging.FileHandler.append", "true"},
            {"java.util.logging.FileHandler.count", "10"},
            // Имя файла лога по-умолчанию. $HOME/s2s-номер.log
            {"java.util.logging.FileHandler.pattern", "%h/s2s-%u%g.log"},

            // Конфигурация консольного хендлера
            // Уровень логгирования для файлов
            {"java.util.logging.ConsoleHandler.level", "INFO"},
            {"java.util.logging.ConsoleHandler.formatter", "java.util.logging.SimpleFormatter"}
    };
    private Properties LogManagerProperties;

    /**
     * Инициализация по-умолчанию.
     * Выводить лог в консоль.
     */
    public LogManagerChanger() {
        // Генерируем properties
        LogManagerProperties = new Properties();
        for (String property[] : DefaultProperties)
            LogManagerProperties.put(property[0], property[1]);


        uploadProperties();
    }

    /**
     * Загрузка сгенерированного Properties в LogManager.
     * <p>
     * LogManager не позволяет напрямую загрузить Properties, только поток из файла.
     * Поэтому Properties сохраняется в буффер, откуда уже загружается в LogManager в виде потока.
     */
    private void uploadProperties() {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            LogManagerProperties.store(buffer, "Generated configuration");
            buffer.flush();

            try (ByteArrayInputStream loader_from_buffer = new ByteArrayInputStream(buffer.toByteArray())) {

                LogManager.getLogManager().readConfiguration(loader_from_buffer);
            } catch (IOException load_err) {
                System.err.println("ERROR: unable to load logger configuration data: " + load_err.getMessage());
            }
        } catch (IOException save_err) {
            System.err.println("ERROR: unable generate logger configuration data: " + save_err.getMessage());
        }
    }

    /**
     * Перенаправление логгирования в файл.
     *
     * @param filename Имя файла, а лучше паттерн, соответствующий java.util.logging.FileHandler.pattern
     *                 Если не указано (null), то лог пишется в каталог пользователя, s2s-номер.log
     * @param limit    лимит (размер лога) в байтах. Если не указано положительное число больше нуля,
     *                 то используется умолчание - 10 485 760 (10 Мб.)
     * @param count    максимальное число файлов по ротированию
     *                 если не указано (<=0), то используется умолчание - 10 шт.
     */
    public void redirectToFile(String filename, int limit, int count) {
        LogManagerProperties.setProperty("handlers", "java.util.logging.FileHandler");

        if (filename != null)
            LogManagerProperties.setProperty("java.util.logging.FileHandler.pattern", filename);

        if (limit > 0)
            LogManagerProperties.setProperty("java.util.logging.FileHandler.limit", Integer.toString(limit));

        if (count > 0)
            LogManagerProperties.setProperty("java.util.logging.FileHandler.count", Integer.toString(count));

        uploadProperties();
    }

    /**
     * Разрешить сообщения отладки
     */
    public void enableDebug() {
        LogManagerProperties.setProperty(".level", "ALL");
        LogManagerProperties.setProperty("java.util.logging.FileHandler.level", "ALL");
        LogManagerProperties.setProperty("java.util.logging.ConsoleHandler.level", "ALL");

        uploadProperties();
    }

    /**
     * Изменение формата вывода логгера.
     *
     * @param format формат
     *               Используется форматирование SimpleFormatter -
     *               <a href="https://docs.oracle.com/javase/7/docs/api/java/util/logging/SimpleFormatter.html#formatting>см. здесь</a>
     */
    public void changeFmt(String format) {
        if (format != null) {
            LogManagerProperties.setProperty("java.util.logging.SimpleFormatter.format", format);

            uploadProperties();
        }
    }
}
