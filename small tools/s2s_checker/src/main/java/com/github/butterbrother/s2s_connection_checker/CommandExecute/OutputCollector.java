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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Выполняет сбор выхлопа (stdio/stderr) из приложения.
 * Работает в отдельном потоке и позволяет избегать блокировок из-за переполнения буфера ввода-вывода.
 */
class OutputCollector
        implements Runnable {
    /**
     * Собранный выхлоп stdio
     */
    private StringBuilder collected_stdio = new StringBuilder().append("STDIO: ");

    /**
     * Собранный выхлоп stderr
     */
    private StringBuilder collected_stderr = new StringBuilder().append("STDERR: ");

    /**
     * Процесс, выхлоп которого слушаем
     */
    private Process process;

    /**
     * Логгер
     */
    private Logger log;

    /**
     * Поток сбора логов
     */
    private Thread currentThread;

    /**
     * Флаг активности
     */
    private boolean active = true;

    /**
     * Инициализация
     *
     * @param process рабочий процесс
     * @param parent  лог
     */
    OutputCollector(Process process, Logger parent) {
        this.process = process;

        log = Logger.getLogger("Command output collector");
        log.setParent(parent);

        currentThread = new Thread(this);
        currentThread.start();
    }

    /**
     * Процесс сбора выхлопа
     */
    public void run() {
        try (InputStream stdio_stream = process.getInputStream();
             InputStream stderr_stream = process.getErrorStream();
             BufferedReader buffered_stdio_reader = new BufferedReader(new InputStreamReader(stdio_stream));
             BufferedReader buffered_stderr_reader = new BufferedReader(new InputStreamReader(stderr_stream))) {

            String buffer_stdio, buffer_stderr = null;
            while (active) {
                while (active &&
                        ((buffer_stdio = buffered_stdio_reader.readLine()) != null ||
                                (buffer_stderr = buffered_stderr_reader.readLine()) != null)) {

                    if (buffer_stdio != null)
                        collected_stdio.append(buffer_stdio).append(System.lineSeparator());

                    if (buffer_stderr != null)
                        collected_stderr.append(buffer_stderr).append(System.lineSeparator());
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException err) {
                    log.info("Interrupted");
                }
            }

        } catch (IOException err) {
            log.severe(err.getMessage());
        }

        active = false;
    }

    /**
     * Остановка сбора данных.
     * Вызывается, когда процесс завершён.
     * После вызова данного метода предпочтительно
     * дождаться завершения сбора через {@link #join()}
     */
    void stop() {
        active = false;
    }

    /**
     * Получение собранного выхлопа.
     *
     * @return стандартный выхлоп
     */
    String getCollectedStdio() {
        return collected_stdio.toString();
    }

    /**
     * Получение собранных ошибок.
     *
     * @return выхлоп ошибок
     */
    String getCollectedStderr() {
        return collected_stderr.toString();
    }

    /**
     * Дождаться завершения процесса сбора выхлопа.
     */
    void join() {
        try {
            currentThread.join();
        } catch (InterruptedException ignore) {
            currentThread.interrupt();
        }
    }
}
