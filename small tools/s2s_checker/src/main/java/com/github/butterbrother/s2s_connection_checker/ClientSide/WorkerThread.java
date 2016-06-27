package com.github.butterbrother.s2s_connection_checker.ClientSide;

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

import com.github.butterbrother.s2s_connection_checker.CommandExecute.Command;
import com.github.butterbrother.s2s_connection_checker.Config.Target;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Рабочий поток клиента.
 * <p>
 * Подключается к серверу s2s. Передаёт и получает определённое сообщение.
 * При неудачном подключении запускает команды для анализа причин обрыва.
 */
class WorkerThread
        implements Runnable, Comparable<WorkerThread> {
    /**
     * Поток управления клиентами.
     */
    private ClientController owner;

    /**
     * Флаг активности потока
     */
    private boolean active = true;

    /**
     * Логгер
     */
    private Logger log;

    /**
     * Порядковый индекс рабочего процесса. Он же является его хеш-кодом.
     */
    private int index;

    /**
     * Текущее подключение
     */
    private Socket currentClientSocket = null;

    /**
     * Цель данного рабочего процесса.
     */
    private Target target;

    /**
     * Набор команд диагностики, выполняемых при разрыве соединения
     */
    private ConcurrentLinkedDeque<Command> commands;

    WorkerThread(ClientController owner, Target target, Logger parent, int index, String[][] commandsList) {
        this.owner = owner;
        this.target = target;

        log = Logger.getLogger("Client worker " + target);
        log.setParent(parent);

        this.index = index;

        commands = new ConcurrentLinkedDeque<>();
        for (String cmd[] : commandsList)
            commands.add(new Command(log, cmd, target.getAddress()));

        new Thread(this).start();
    }

    /**
     * Поток обработчика клиента
     */
    public void run() {
        long bytes_send = 0;  // общее число переданных байт

        while (active) {

            if (log.isLoggable(Level.FINE)) log.fine("Trying to connect");

            try (Socket client = new Socket(target.getAddress(), target.getPort())) {

                log.info("Connected");
                client.setSoTimeout(1000);

                // Буфер, который будем отправлять, и в который будем класть ответы
                byte buffer[] = new byte[8192];
                // Заполняем чёрти чем
                Arrays.fill(buffer, (byte) 0b10101);

                InputStream input_data = client.getInputStream();
                OutputStream output_data = client.getOutputStream();

                // Отправляем первыми
                output_data.write(buffer);

                bytes_send += buffer.length;

                int size;

                // Каждые 100 мс только и делаем, что гоняем трафик
                while (active && (size = input_data.read(buffer)) > 0) {
                    output_data.write(buffer, 0, size);

                    bytes_send += size;

                    // После каждой отправки засыпаем
                    try {
                        if (log.isLoggable(Level.FINE)) log.fine("Currently total send bytes = " + bytes_send);

                        if (active) Thread.sleep(100);  // Дабы не творить DoS
                    } catch (InterruptedException ignore) {
                        disable();
                        log.info("Interrupted");
                    }
                }
            } catch (IOException ioErr) {

                if (active) {
                    // В случае ошибки пишем в лог и запускаем команды диагностики.
                    // Одновременно все
                    log.severe(ioErr.getMessage());

                    for (Command command : commands)
                        if (!command.isActive())
                            command.execute();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignore) {
                        log.info("Interrupted");
                        active = false;
                    }
                }

            }

        }

        owner.workerDone(this);

        if (log.isLoggable(Level.FINE))
            log.fine("Total send " + bytes_send + " bytes.");
    }

    /**
     * Отключение рабочего процесса
     */
    void disable() {
        active = false;
        if (currentClientSocket != null && currentClientSocket.isConnected())
            try {
                currentClientSocket.close();
            } catch (IOException ioErr) {
                log.warning(ioErr.getMessage());
            }

        for (Command command : commands)
            command.stop();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof WorkerThread && index == ((WorkerThread) o).index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public int compareTo(WorkerThread o) {
        return index = o.index;
    }
}
