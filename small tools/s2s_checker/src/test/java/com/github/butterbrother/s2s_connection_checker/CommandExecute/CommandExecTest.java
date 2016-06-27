package com.github.butterbrother.s2s_connection_checker.CommandExecute;

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

import com.github.butterbrother.s2s_connection_checker.Core.LogManagerChanger;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Тестирование исполнения команд
 */
public class CommandExecTest {

    /**
     * Тестирование выполнения команды на примере ping
     *
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void pingTest() throws InterruptedException, IOException {
        Path logFile = Paths.get("target/ping_test-s2s-0.log");
        Files.deleteIfExists(logFile);

        LogManagerChanger lmc = new LogManagerChanger();
        lmc.redirectToFile("target/ping_test-s2s-%g.log", -1, -1);

        Command command = new Command(Logger.getGlobal(),
                new String[]{
                        "ping", Command.IP_MASC
                }, InetAddress.getByName("127.0.0.1"));

        command.execute();
        Thread.sleep(5000);
        command.stop();
        Thread.sleep(1000);

        boolean found = false;
        boolean zeroLoss = false;

        try (BufferedReader reader = Files.newBufferedReader(logFile, Charset.defaultCharset())) {
            String buffer;

            while ((buffer = reader.readLine()) != null) {
                if (buffer.contains("127.0.0.1"))
                    found = true;

                if (buffer.contains("0%") || buffer.contains("bytes from 127.0.0.1"))
                    zeroLoss = true;
            }
        }

        // В логе должен быть выхлоп команды с IP-адресом, который подставляется в маске
        org.junit.Assert.assertTrue("Localhost IP must be exists in log ", found);

        // Пинг должен пройти без потерь, т.е. команда вообще должна выполниться и корректно
        org.junit.Assert.assertTrue("Ping must be work correct ", zeroLoss);
    }
}
