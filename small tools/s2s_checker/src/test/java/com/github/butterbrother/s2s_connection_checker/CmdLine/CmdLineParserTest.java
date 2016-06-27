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

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Тестирование прасера командной строки.
 */
public class CmdLineParserTest {

    /**
     * Проверка, что парсер находит ключи справки.
     * Среди аргументов командной строки только ключи справки.
     * Как короткий, так и длинный вариант.
     */
    @Test
    public void onlyHelpTest() {
        String cmdLine[] = {"-h"};
        String cmdLineLong[] = {"--help"};

        CmdLineParser cmdLineParser = new CmdLineParser();
        org.junit.Assert.assertTrue("Help is exist ", cmdLineParser.requiredHelp(cmdLine));
        org.junit.Assert.assertTrue("Help is exist ", cmdLineParser.requiredHelp(cmdLineLong));
    }

    /**
     * Проверка, что парсер корректно обрабатывает верные аргументы командной строки
     * и передаёт истинные параметры.
     *
     * @throws Exception ошибка при тестировании
     */
    @Test
    public void validCmdLine() throws Exception {
        Path temp = rebuildIniFile();

        String cmdLine[] = {"-c", temp.toString(), "-s"};
        String cmdLineLong[] = {"--config=" + temp.toString(), "--server"};
        String cmdLineWithDebug[] = { "-c", temp.toString(), "-d" };

        CmdLineParser cmdLineParser = new CmdLineParser();
        CmdLineOpts validOpts = new CmdLineOpts(true, temp, false);
        CmdLineOpts withDebug = new CmdLineOpts(false, temp, true);

        CmdLineOpts result = cmdLineParser.parseCmdLine(cmdLine);
        org.junit.Assert.assertEquals("Result of parse must be equals ", result, validOpts);

        result = cmdLineParser.parseCmdLine(cmdLineLong);
        org.junit.Assert.assertEquals("Result of parse long params must be equals ", result, validOpts);

        result = cmdLineParser.parseCmdLine(cmdLineWithDebug);
        org.junit.Assert.assertEquals("Result of parse params with enabled debug must be equals ", result, withDebug);

        deleteIniFile(temp);
    }

    /**
     * Проверка, что появляется исключение, если не передан путь к файлу
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void notRequiredCmdLine() throws Exception {
        String cmdLine[] = {"-s"};

        CmdLineParser cmdLineParser = new CmdLineParser();
        cmdLineParser.parseCmdLine(cmdLine);
    }

    /**
     * Проверка, что появляется исключение, если переданный путь к файлу не существует
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void fileNotFound() throws Exception {
        String cmdline[] = {"-c", "blah-blah.ini"};

        new CmdLineParser().parseCmdLine(cmdline);
    }

    /**
     * Создание-пересоздание пустого временного ini-файла.
     *
     * @return временный ini-файл
     * @throws IOException
     */
    private Path rebuildIniFile() throws IOException {
        Path temp = Paths.get("./s2s.ini");

        deleteIniFile(temp);

        Files.createFile(temp);

        return temp;
    }

    /**
     * Удаление временного ini-файла
     *
     * @param path временный ini-файл
     * @throws IOException
     */
    private void deleteIniFile(Path path) throws IOException {
        if (Files.exists(path))
            Files.delete(path);
    }
}
