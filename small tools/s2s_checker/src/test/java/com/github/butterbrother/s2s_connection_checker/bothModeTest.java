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
import com.github.butterbrother.s2s_connection_checker.Config.ConfigFile;
import com.github.butterbrother.s2s_connection_checker.Core.LogManagerChanger;
import com.github.butterbrother.s2s_connection_checker.ServerSide.ServerController;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Тестирование основного функционала
 */
public class bothModeTest {

    /**
     * Файл конфигурации для проведения испытания
     */
    private static final String validConfig[] = {
            ConfigFile.TARGET_HEADER,
            "127.0.0.1:5000",
            ConfigFile.ALLOW_HEADER,
            "127.0.0.1",
            ConfigFile.PORT_HEADER,
            "5000",
            ConfigFile.COMMANDS_HEADER,
            "ping {IP}"
    };
    /**
     * Для генерации случайного имени для файла конфигурации
     */
    private Random rnd = new Random();

    /**
     * Основное тестирование модулей
     *
     * @throws Exception
     */
    @Test
    public void commonsTest() throws Exception {

        // Создаём и загружаем параметры, подготавливаем тестирование
        configureLogs();

        Path configPath = createValidConfig();

        ConfigFile configFile = new ConfigFile(configPath);

        removeConfig(configPath);

        // Основная часть
        // Стартуем оба модуля. Как будто на хосте запущено приложение в двух экземплярах
        ServerController server = new ServerController(configFile, Logger.getGlobal());
        ClientController client = new ClientController(configFile, Logger.getGlobal());

        Thread.sleep(10000);
        server.disable();
        Thread.sleep(3000);
        client.disable();
        Thread.sleep(1000);
    }

    /**
     * Настраивает логгирование для теста
     *
     * @throws IOException
     */
    private void configureLogs() throws IOException {
        Path logFile = Paths.get("target/modules_test-s2s-0.log");
        Files.deleteIfExists(logFile);

        LogManagerChanger lmc = new LogManagerChanger();
        lmc.redirectToFile("target/modules_test-s2s-%g.log", -1, -1);
        lmc.enableDebug();
    }

    /**
     * Вспомогательный метод. Создаёт корректный файл конфигурации.
     * Корректный в том плане, что не содержит ошибки. Но содержит лишние пробелы и т.п.
     * Вообщем, он дожен обработаться.
     *
     * @return путь к созданному файлу.
     * @throws IOException
     */
    private Path createValidConfig() throws IOException {
        // Позволит избежать блокировок под Win32
        String uniqName = "s2s_config_" + rnd.nextInt() + ".ini";

        Path config = Paths.get(uniqName);

        try (BufferedWriter writer = Files.newBufferedWriter(config, Charset.defaultCharset())) {
            for (String value : validConfig)
                writer.append(value).append(System.lineSeparator());
        }

        return config;
    }

    /**
     * Вспомогательный метод. Удаление файла конфигурации (если тот существует)
     *
     * @param config путь к файлу конфигурации
     */
    private void removeConfig(Path config) {
        try {
            Files.deleteIfExists(config);
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }
}
