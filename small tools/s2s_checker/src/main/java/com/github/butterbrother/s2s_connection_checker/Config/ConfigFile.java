package com.github.butterbrother.s2s_connection_checker.Config;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Парсер файла конфигурации.
 * <p>
 * Файл конфигурации представляет собой что-то вроде ini-файла.
 * Т.е.:<br>
 * [<b>{@link #ALLOW_HEADER}</b>]<br>
 * сервер 1<br>
 * сервер 2<br>
 * ...<br>
 * сервер n<br>
 * [<b>{@link #TARGET_HEADER}</b>]<br>
 * сервер 1<br>
 * сервер 2<br>
 * ...<br>
 * сервер n<br>
 * [<b>{@link #COMMANDS_HEADER}</b>]<br>
 * команда 1<br>
 * команда 2<br>
 * ...<br>
 * команда n<br>
 * [<b>{@link #PORT_HEADER}</b>]
 * порт сервера/клиента (по-умолчанию)
 * <br>
 * Описание заголовков смотрите далее по ссылкам.
 */
public class ConfigFile {
    /**
     * Заголовок, после которого перечисляются целевые сервера для
     * подключения в режиме клиента.
     */
    public static final String TARGET_HEADER = "[targets]";
    /**
     * Заголовок, после которого перечисляются разрешённые ip-адреса
     * либо хоста клиентов, которые могут подключаться в режиме
     * сервера
     */
    public static final String ALLOW_HEADER = "[allow]";
    /**
     * Заголовок, после которого перечисляется список команд, выполняемый
     * на стороне клиента при проблеме соединения с сервером
     */
    public static final String COMMANDS_HEADER = "[commands]";
    /**
     * Заголовок, после которого указывается номер порта.
     * Используется только первая строка, остальные игнорируются.
     */
    public static final String PORT_HEADER = "[port]";
    /**
     * Заголовок, после которого указывается путь к логу.
     * Используется только первая строка, остальные игнорируются.
     * Если путь не будет указан - будет использоваться вывод в консоль
     * (stderr)
     */
    public static final String LOGPATH_HEADER = "[log]";
    /**
     * Порт приложения по-умолчанию
     */
    public static final int DEFAULT_PORT = 20500;
    /**
     * Режим работы - ничего не делаем
     */
    private static final int MODE_NOTHING = 0;
    /**
     * Режим работы - собираем список целевых серверов
     */
    private static final int MODE_ACCUM_TARGETS = 1;
    /**
     * Режим работы - собираем список разрешённых клиентов
     */
    private static final int MODE_ACCUM_ALLOW = 2;
    /**
     * Режим - сбор команд, выполняемых клиентом при проблеме с соединением
     */
    private static final int MODE_ACCUM_COMMANDS = 3;
    /**
     * Режим - определение номера порта
     */
    private static final int MODE_SET_PORT = 4;
    /**
     * Режим - указание пути к логу
     */
    private static final int MODE_SET_LOGPATH = 5;
    /**
     * Список целевых адресов
     */
    private final Target[] targets;
    /**
     * Список разрешённых клиентов
     */
    private final InetAddress[] allows;
    /**
     * Список команд
     */
    private final String[][] commands;
    /**
     * Порт, для работы в режиме сервера
     */
    private final int port;

    /**
     * Путь к файлу лога
     */
    private final String logPath;

    /**
     * Инициализация и считывание из файла конфигурации.
     *
     * @param configFile путь к файлу конфигурации
     * @throws IOException ошибка чтения из файла
     */
    public ConfigFile(Path configFile) throws IOException {
        List<Target> targetsList = new ArrayList<>();
        List<InetAddress> allowList = new ArrayList<>();
        List<String[]> commandsList = new LinkedList<>();

        int port = DEFAULT_PORT;
        boolean port_changed = false;

        boolean log_path_is_set = false;
        String logPath = "";

        long line_number = 0;

        try (BufferedReader configReader = Files.newBufferedReader(configFile, Charset.defaultCharset())) {
            String buffer, wiped;
            String splitted[];
            char buffer_array[], wiped_array[];
            int i, len;

            int work_mode = MODE_NOTHING;

            while ((buffer = configReader.readLine()) != null) {
                ++line_number;

                // Находим и отделяем комментарии от параметров
                if (buffer.contains("#")) {
                    splitted = buffer.split("#");

                    if (splitted.length == 0) continue;

                    buffer = splitted[0];
                }


                if (buffer.contains("//")) {
                    splitted = buffer.split("//");

                    if (splitted.length == 0) continue;

                    buffer = splitted[0];
                }

                if (buffer.contains(";")) {
                    splitted = buffer.split(";");

                    if (splitted.length == 0) continue;

                    buffer = splitted[0];
                }

                // Вырезаем все пробелы. Как по краям, так и внутри строк
                // А так же режем табы
                buffer = buffer.trim();
                if (buffer.isEmpty()) continue;

                buffer_array = buffer.toLowerCase().toCharArray();
                len = 0;
                wiped_array = new char[buffer_array.length];

                for (i = 0; i < buffer_array.length; ++i)
                    if (buffer_array[i] != ' ' && buffer_array[i] != '\t')
                        wiped_array[len++] = buffer_array[i];

                if (len == 0) continue;

                // Дальше будем анализировать очищенную строку
                wiped = new String(wiped_array, 0, len);

                /*
                // Пропускаем строки, содержащие только комментарии
                if (wiped.startsWith("#") || wiped.startsWith("//") || wiped.startsWith(";"))
                    continue;
                    */

                // Смотрим, возможно ли переключить режим работы
                if (wiped.charAt(0) == '[')
                    switch (wiped) {
                        case ALLOW_HEADER:
                            work_mode = MODE_ACCUM_ALLOW;
                            continue;

                        case TARGET_HEADER:
                            work_mode = MODE_ACCUM_TARGETS;
                            continue;

                        case COMMANDS_HEADER:
                            work_mode = MODE_ACCUM_COMMANDS;
                            continue;

                        case PORT_HEADER:
                            work_mode = MODE_SET_PORT;
                            continue;

                        case LOGPATH_HEADER:
                            work_mode = MODE_SET_LOGPATH;
                            continue;
                    }

                // Собираем адреса и т.д.
                switch (work_mode) {
                    case MODE_NOTHING:
                        continue;

                    case MODE_ACCUM_ALLOW:
                        allowList.add(InetAddress.getByName(wiped));
                        continue;

                    case MODE_ACCUM_TARGETS:
                        // В конфиге можно указать порт через двоеточие
                        // А можно и не указывать - берём дефолтный

                        if (wiped.contains(":")) {

                            String target_host[] = wiped.split(":");

                            targetsList.add(new Target(
                                    InetAddress.getByName(target_host[0]),
                                    Integer.parseInt(target_host[1]))
                            );
                        } else {
                            targetsList.add(new Target(
                                    InetAddress.getByName(wiped),
                                    DEFAULT_PORT
                            ));
                        }
                        continue;

                    case MODE_ACCUM_COMMANDS:
                        commandsList.add(buffer.split(" "));
                        continue;

                    case MODE_SET_PORT:
                        if (!port_changed) {
                            port = Integer.parseInt(wiped);
                            port = Math.abs(port);
                            port_changed = true;
                        }
                        continue;

                    case MODE_SET_LOGPATH:
                        if (!log_path_is_set) {
                            logPath = buffer;
                            log_path_is_set = true;
                        }
                }
            }
        } catch (UnknownHostException | NumberFormatException err) {
            throw new IOException("In config file at line " + line_number + ": " + err.getMessage());
        }

        // Запоминаем окончательный номер порта
        this.port = port;

        // Запоминаем путь к логу
        this.logPath = logPath;

        // Сохраняем собранные адреса
        allows = new InetAddress[allowList.size()];
        allowList.toArray(allows);

        targets = new Target[targetsList.size()];
        targetsList.toArray(targets);

        commands = new String[commandsList.size()][];
        commandsList.toArray(commands);
    }

    /**
     * Получение списка целей
     *
     * @return список целей
     */
    public Target[] getTargets() {
        return targets;
    }

    /**
     * Получение списка киентов, которые могут подключаться при работе в режиме сервера
     *
     * @return список
     */
    public InetAddress[] getAllows() {
        return allows;
    }

    /**
     * Получение последовательного списка команд, выполняемых при обрыве соединения при работае в режиме клиента
     *
     * @return список
     */
    public String[][] getCommands() {
        return commands;
    }

    /**
     * Получение номера порта, по которому будет работать приложение
     *
     * @return номер порта
     */
    public int getPort() {
        return port;
    }

    /**
     * Получение пути к файлу лога.
     *
     * @return если путь был определён в файле конфигурации - вернётся путь к логу.
     * иначе вернётся пустая строка (не null)
     */
    public String getLogPath() {
        return logPath;
    }
}
