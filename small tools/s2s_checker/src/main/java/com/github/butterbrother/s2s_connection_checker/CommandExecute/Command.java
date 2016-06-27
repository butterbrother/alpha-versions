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

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

/**
 * Выполняет диагностическую команду для переданного адреса
 */
public class Command
        implements Runnable {
    /**
     * Маска для подмены в команде на ip-адрес цели
     */
    public static final String IP_MASC = "{IP}";

    /**
     * Маска для подмены в команде на имя хоста цели
     */
    public static final String HOST_MASC = "{HOST}";

    /**
     * Запускает команду с переданными параметрами и переданным IP-адрсеом на конце
     */
    private ProcessBuilder processBuilder;

    /**
     * Текущий запущенный процесс
     */
    private Process currentProcess = null;

    /**
     * Текущий сборкик выхлопа процесса
     */
    private OutputCollector collector = null;

    /**
     * Логгер
     */
    private Logger log;

    /**
     * Флаг активности
     */
    private boolean active = false;

    /**
     * ID исполнителя команд
     */
    private String id;

    /**
     * Инициализация
     *
     * @param parent  Переданный логгер
     * @param command команда для диагностики.
     *                Аргументы автоматически подменяются:<br>
     *                {@link #IP_MASC} - на IP-адрес цели<br>
     *                {@link #HOST_MASC} - на доменное имя цели
     * @param target  адрес для выполнения диагностики
     */
    public Command(Logger parent, String[] command, InetAddress target) {

        // Осуществляем подмену по маскам
        for (int i = 0; i < command.length; ++i) {
            String normalized = command[i].toUpperCase().trim();

            switch (normalized) {
                case IP_MASC:
                    command[i] = target.getHostAddress();
                    continue;

                case HOST_MASC:
                    command[i] = target.getHostName();
            }
        }

        processBuilder = new ProcessBuilder(command);

        StringBuilder id_builder = new StringBuilder();
        for (String item : command)
            id_builder.append(item).append(' ');

        id = "Command executor [" + id_builder.toString().trim() + "]";

        log = Logger.getLogger(id);
        log.setParent(parent);
    }

    /**
     * Запуск команды на исполнение.
     * Если команда уже запущена - повторного запуска не будет.
     */
    public void execute() {
        if (!active)
            new Thread(this, id).start();
        else
            log.warning("Already run");
    }

    /**
     * Выполнение команды
     */
    public void run() {
        active = true;

        try {
            currentProcess = processBuilder.start();

            log.info("Command executed");

            collector = new OutputCollector(currentProcess, log);

            try {
                currentProcess.waitFor();
            } catch (InterruptedException interrupted) {
                currentProcess.destroy();
            }

            collector.stop();
            collector.join();

            log.info(collector.getCollectedStderr());
            log.info(collector.getCollectedStdio());
        } catch (IOException ioErr) {
            log.severe(ioErr.getMessage());
        }

        currentProcess = null;
        collector = null;

        active = false;
    }

    /**
     * Остановка выполнения процесса.
     * Если не запущен - ничего не произойдёт.
     */
    public void stop() {
        if (currentProcess != null)
            currentProcess.destroy();

        if (collector != null)
            collector.stop();
    }

    @Override
    public String toString() {
        return id;
    }

    /**
     * Проверка статуса активности команды
     *
     * @return статус активности
     */
    public boolean isActive() {
        return active;
    }
}
