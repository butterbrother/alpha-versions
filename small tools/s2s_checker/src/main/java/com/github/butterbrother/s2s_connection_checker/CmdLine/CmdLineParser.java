package com.github.butterbrother.s2s_connection_checker.CmdLine;

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

import com.github.butterbrother.s2s_connection_checker.Config.ConfigFile;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Парсер и хранилище параметров, переданных через
 * аргументы командной строки
 */
public class CmdLineParser {
    /**
     * Полные опции командной строки, со всеми ключами
     */
    private Options cmdLineOptions;
    /**
     * Опции командной строки, содержащие только ключ справки
     */
    private Options onlyHelpOptions;
    /**
     * Форматтер для вывода справки
     */
    private HelpFormatter helpFormatter = new HelpFormatter();

    /**
     * Инициализация
     */
    public CmdLineParser() {
        // Режим работы - сервер или клиент
        Option serverWorkMode = Option.builder("s")
                .longOpt("server")
                .hasArg(false)
                .desc("Server work mode (listening inbound connections).")
                .required(false)
                .build();

        // Обязательный аргумент - расположение файла конфигурации
        Option configFileLocation = Option.builder("c")
                .longOpt("config")
                .argName("path")
                .hasArg(true)
                .desc("Location to configuration file.")    // TODO: здесь в итоге должна быть ссылка на описание конфига
                .required(true)
                .numberOfArgs(1)
                .build();

        // Отображение справки
        Option showHelp = Option.builder("h")
                .longOpt("help")
                .hasArg(false)
                .desc("Show this help")
                .required(false)
                .build();

        // Включение отладки
        Option enableDebug = Option.builder("d")
                .longOpt("debug")
                .hasArg(false)
                .desc("Enable debug")
                .required(false)
                .build();

        // Основной парсер командной строки
        cmdLineOptions = new Options()
                .addOption(serverWorkMode)
                .addOption(configFileLocation)
                .addOption(showHelp)
                .addOption(enableDebug);

        // Парсер только с ключом справки
        onlyHelpOptions = new Options()
                .addOption(showHelp);
    }

    /**
     * Определение, необходимо ли отображение справки
     *
     * @param args аргументы командной строки
     * @return необходимость показа справки
     */
    public boolean requiredHelp(String... args) {
        try {
            CommandLine helpCmdLine = new DefaultParser().parse(onlyHelpOptions, args, false);

            return helpCmdLine.hasOption('h');

        } catch (ParseException ignore) {
            return false;
        }
    }

    /**
     * Парсинг аргументов командной строки.
     *
     * @param args аргументы командной строки
     * @return Если аргументы корректные, то вернутся параметры командной строки
     * @throws Exception исключение, если аргументы были некорректные. Либо указанный файл
     *                   конфигурации не существует.
     */
    public CmdLineOpts parseCmdLine(String... args) throws Exception {
        try {
            CommandLine commandLine = new DefaultParser().parse(cmdLineOptions, args, true);

            String configFileStringPath = commandLine.getOptionValue('c');
            Path configFilePath = Paths.get(configFileStringPath);

            if (Files.notExists(configFilePath))
                throw new IOException("Configuration file \"" + configFileStringPath + "\" not exists.");

            if (!Files.isRegularFile(configFilePath))
                throw new IOException("Configuration file \"" + configFileStringPath + "\" is not regular file.");

            boolean serverMode = commandLine.hasOption('s');

            boolean debugMode = commandLine.hasOption('d');

            return new CmdLineOpts(serverMode, configFilePath, debugMode);
        } catch (ParseException | IOException err) {
            throw new Exception(err.getMessage());
        }
    }

    /**
     * Отображение справки
     */
    public void showHelpMessage() {
        helpFormatter.printHelp("java -jar s2s_checker.jar",
                "\nUsing for long-term network stability check between servers.\n\nKeys:",
                cmdLineOptions,
                "\nConfiguration file is:\n" +
                        "# This is comment. Also comment can be begin from ; and //\n" +
                        "\n" +
                        ConfigFile.ALLOW_HEADER + " # After this header enumerate all hosts\n" +
                        "# that can connect to s2s into server mode\n" +
                        "127.0.0.1 # as sample\n" +
                        ConfigFile.TARGET_HEADER + " # After this header enumerate all hosts\n" +
                        "# thar needed connect into client mode. Hosts == hosts with run s2s\n" +
                        "# into server mode\n" +
                        "127.0.0.1 # as sample. Using default port - " + ConfigFile.DEFAULT_PORT + "\n" +
                        "localhost:5000 # also can define s2s server port\n" +
                        ConfigFile.COMMANDS_HEADER + " # After this header enumerate diagnostic\n" +
                        "# commands into client mode. It run immediately if connection to s2s server\n" +
                        "# lost\n" +
                        "ping {IP} # can substitute remote s2s server ip into command\n" +
                        "traceroute {HOST} # or substitute remote hostname\n" +
                        ConfigFile.PORT_HEADER + " # After this header define port number for server\n" +
                        "# mode. If it not set, using default - " + ConfigFile.DEFAULT_PORT + "\n" +
                        "8090 # as sample. Application read only first port number into config file\n" +
                        ConfigFile.LOGPATH_HEADER + " # After this header define log path.\n" +
                        "# If it not set - use console output. \n" +
                        "# Application used java.util.logging.FileHandler.pattern format.\n" +
                        "./s2s-%g.log # as sample. %g - rotation version number (total - 10 * 10 Mb)\n" +

                        "\nPlease report issues to https://github.com/butterbrother/",
                true);
    }

}
