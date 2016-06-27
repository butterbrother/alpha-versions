package com.github.butterbrother.s2s_connection_checker;

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

import com.github.butterbrother.s2s_connection_checker.ClientSide.ClientController;
import com.github.butterbrother.s2s_connection_checker.CmdLine.CmdLineOpts;
import com.github.butterbrother.s2s_connection_checker.CmdLine.CmdLineParser;
import com.github.butterbrother.s2s_connection_checker.Config.ConfigFile;
import com.github.butterbrother.s2s_connection_checker.Core.LogManagerChanger;
import com.github.butterbrother.s2s_connection_checker.ServerSide.ServerController;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Загрузчик приложения и его точка отсчёта.
 */
public class Loader {

    public static void main(String args[]) {
        LogManagerChanger logChanger = new LogManagerChanger();

        // Вначале проверяем корректность аргументов командной строки
        CmdLineParser parser = new CmdLineParser();

        if (parser.requiredHelp(args)) {
            parser.showHelpMessage();
            System.exit(0);
        }

        Logger log = Logger.getLogger("Loader");

        CmdLineOpts cmdLineOpts = null;

        try {
            cmdLineOpts = parser.parseCmdLine(args);
        } catch (Exception err) {
            log.severe(err.getMessage());
            parser.showHelpMessage();
            System.exit(1);
        }

        // Включаем отладку, если запрошено через аргумент командной строки
        if (cmdLineOpts.debugEnabled())
            logChanger.enableDebug();

        // Далее обрабатываем файл конфигурации
        ConfigFile configFile = null;

        try {
            configFile = new ConfigFile(cmdLineOpts.getConfigFilePath());

            // Перенаправляем лог в файл, если он был указан
            if (!configFile.getLogPath().isEmpty())
                logChanger.redirectToFile(configFile.getLogPath(), -1, -1);
        } catch (IOException ioErr) {
            log.severe(ioErr.getMessage());
            System.exit(2);
        }

        // И работаем в зависимости от указанного режима работы
        if (cmdLineOpts.isServerMode()) {

            ServerController serverController = new ServerController(configFile, Logger.getGlobal());
            serverController.join();

        } else {
            ClientController clientController = new ClientController(configFile, Logger.getGlobal());
            clientController.join();
        }
    }
}
