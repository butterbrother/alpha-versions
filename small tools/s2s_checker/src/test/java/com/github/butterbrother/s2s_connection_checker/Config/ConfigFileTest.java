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

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

/**
 * Тест считывателя файла конфигурации
 */
public class ConfigFileTest {

    private static final Random rnd = new Random();

    /**
     * Корректный файл конфигурации.
     */
    private static final String validConfig[] =
            {"// Commend",
                    ConfigFile.ALLOW_HEADER,
                    "127.0.0.1 ; is localhost",
                    "  ya.ru  # is yandex site",
                    "",
                    " ",
                    " ; another comment",
                    "\tsome.com",
                    "\t\t  arrays.com  \t",
                    ConfigFile.TARGET_HEADER,
                    "127.0.0.1",
                    "  localhost : 5000 //localhost with 500 port ",
                    "########################",
                    "\t# yet another comment\t",
                    "#######################",
                    "\t127.0 .0. 2",
                    "",
                    ConfigFile.COMMANDS_HEADER,
                    "ping // ping command",
                    "tracert -w 3 {PID} # a win32 tracert",
                    ConfigFile.PORT_HEADER,
                    "12 000 # port number",
                    "8080",
                    ConfigFile.LOGPATH_HEADER,
                    "  ./s2s log.log  // a log path  ",
                    "/var/log/s2s.log"};

    /**
     * Проверка, что не пустой файл вообще открывается и обрабатывается.
     */
    @Test
    public void testIO() throws IOException {
        Path config = createValidConfig();

        new ConfigFile(config);

        removeConfig(config);
    }

    /**
     * Проверка, что файл конфигурации корректно обработан.
     *
     * @throws IOException
     */
    @Test
    public void configCorrectlyReadedTest() throws IOException {
        Path configPath = createValidConfig();

        ConfigFile configFile = new ConfigFile(configPath);

        InetAddress validAllows[] = getValidAllows();
        InetAddress configAllows[] = configFile.getAllows();

        Target validTargets[] = getValidTargets();
        Target configTargets[] = configFile.getTargets();

        String validCommands[][] = getValidCommands();
        String configCommands[][] = configFile.getCommands();

        int validPort = 12000;  // ! должен соответствовать первому порту в validConfig[]
        int configPort = configFile.getPort();

        String validLogPath = "./s2s log.log";
        String configLogPath = configFile.getLogPath();

        org.junit.Assert.assertTrue("Allows list must be match ", Arrays.deepEquals(validAllows, configAllows));

        org.junit.Assert.assertTrue("Targets list must be match ", Arrays.deepEquals(validTargets, configTargets));

        org.junit.Assert.assertTrue("Commands list must be match ", validateArrays(validCommands, configCommands));

        org.junit.Assert.assertEquals("Port number must be match ", validPort, configPort);

        org.junit.Assert.assertEquals("Path to log file must be match ", validLogPath, configLogPath);

        removeConfig(configPath);
    }

    /**
     * Сравнение двумерных массивов объектов
     *
     * @param first  первый массив
     * @param second второй массив
     * @return true - соответствуют
     */
    private boolean validateArrays(Object first[][], Object second[][]) {
        try {
            for (int i = 0; i < first.length; ++i)
                if (!Arrays.deepEquals(first[i], second[i]))
                    return false;

        } catch (ArrayIndexOutOfBoundsException err) {
            return false;
        }

        return true;
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

    /**
     * Вспомогательный метод. Генерирует массив интернет-адресов, с которых возможно подключение.
     * Должен соответствовать содержанию и порядку в тестовом корректном файле конфигурации.
     *
     * @return массив разрешённых адресов.
     * @throws UnknownHostException
     */
    private InetAddress[] getValidAllows() throws UnknownHostException {

        return new InetAddress[]{
                InetAddress.getByName("127.0.0.1"),
                InetAddress.getByName("ya.ru"),
                InetAddress.getByName("some.com"),
                InetAddress.getByName("arrays.com")
        };

    }

    /**
     * Вспомогательный метод. Генерирует массив интернет-адресов, к которым необходимо подключаться.
     * Должен соответствовать содержанию и порядку в тестовом корректном файле конфигурации.
     *
     * @return массив разрешённых адресов.
     * @throws UnknownHostException
     */
    private Target[] getValidTargets() throws UnknownHostException {

        return new Target[]{
                new Target(InetAddress.getByName("127.0.0.1"), ConfigFile.DEFAULT_PORT),
                new Target(InetAddress.getByName("localhost"), 5000),
                new Target(InetAddress.getByName("127.0.0.2"), ConfigFile.DEFAULT_PORT)
        };

    }

    /**
     * Вспомогательный метод. Генерирует массив команд, которые выполняются для тестирования узлов при потере связи.
     * Должен соответствовать содержанию и порядку в тестовом корректном файле конфигурации.
     *
     * @return массив команд
     */
    private String[][] getValidCommands() {
        return new String[][]{
                {"ping"},
                {"tracert", "-w", "3", "{PID}"}
        };
    }
}
