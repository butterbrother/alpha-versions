package com.github.butterbrother.s2s_connection_checker.ServerSide;

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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Рабочий поток, который непосредственно работает с клиентским соединением.
 * <p>
 * Получает данные и тут же их отправляет (эхо).
 */
class WorkerThread
        implements Runnable, Comparable<WorkerThread> {
    private boolean active = true;
    private Socket inboundConnection;
    private String id;
    private int index;
    private ServerController owner;
    private Thread currentThread;
    private Logger log;

    /**
     * Инициализация. Производится из {@link ServerController}.
     *
     * @param inboundConnection входящее соединение
     */
    WorkerThread(Socket inboundConnection, int workerIndex, ServerController owner, Logger logger) {
        this.inboundConnection = inboundConnection;

        String hostName = inboundConnection.getInetAddress().getHostName();
        String hostAddress = inboundConnection.getInetAddress().getHostAddress();

        if (! hostName.isEmpty())
            this.id = "Listener worker " + hostName + " (" + hostAddress + ")" ;
        else
            this.id = "Listener worker " + hostAddress;

        this.index = workerIndex;
        this.owner = owner;

        log = Logger.getLogger(id);
        log.setParent(logger);

        currentThread = new Thread(this);
        currentThread.start();
    }

    /**
     * Поток обработчика клиентского соединения
     */
    public void run() {
        log.info("Connection started.");

        long bytes_statistic = 0;   // статистика по числу принятых байт
        int sleep_counter = 0;  // Обеспечивает периодическую паузу

        try (Socket inbound = inboundConnection) {

            InputStream inputStream = inbound.getInputStream();
            OutputStream outputStream = inbound.getOutputStream();

            byte buffer[] = new byte[8192];
            int size;

            // Пока активны - просто отсылаем те же байты, что и получили ранее
            // работаем по принципу эхо-сервера.
            while (active) {
                while ((size = inputStream.read(buffer)) > 0 && active) {
                    outputStream.write(buffer, 0, size);

                    // Каждые 100 блоков - засыпаем
                    if (++sleep_counter >= 10) {
                        sleep_counter = 0;

                        if (log.isLoggable(Level.FINE))
                            log.fine("Currently total received bytes = " + bytes_statistic);

                        try {
                            if (active) Thread.sleep(100);
                        } catch (InterruptedException interrupted) {
                            inactivate();
                        }
                    }

                    bytes_statistic += size;
                }

                // Засыпаем так же при отсутствии данных
                if (inputStream.available() <= 0)
                    try {
                        if (active) Thread.sleep(100);
                    } catch (InterruptedException interrupted) {
                        inactivate();
                    }
            }

        } catch (IOException ioErr) {
            log.warning(ioErr.getMessage());
            active = false;
        }

        log.info("Connection closed");

        if (log.isLoggable(Level.FINE))
            log.fine("Total received " + bytes_statistic + " bytes.");

        owner.workerDone(this);
    }

    /**
     * Остановка текущего обработчика
     */
    void inactivate() {
        active = false;
        currentThread.interrupt();

        if (inboundConnection.isConnected())
            try {
                inboundConnection.close();
            } catch (IOException ioErr) {
                log.warning(ioErr.getMessage());
            }
    }

    /**
     * Сравнение.
     * <p>
     * Сравнение рабочих потоков по их индексу.
     *
     * @param o другой поток
     * @return результат сравнения
     */
    @Override
    public int compareTo(WorkerThread o) {
        return index - o.index;
    }

    /**
     * Генерация хеш-кода
     *
     * @return хеш-код на основе переданного индекса
     */
    @Override
    public int hashCode() {
        return index;
    }

    /**
     * Сравнение.
     * Проверяется только индекс рабочих потоков.
     *
     * @param o объект вида {@link WorkerThread}
     * @return true, если такой же объект и тот же индекс.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WorkerThread))
            return false;

        WorkerThread remote = (WorkerThread) o;

        return remote.index == index;
    }

    /**
     * Возвращает строковое представление.
     *
     * @return строковое представление
     */
    @Override
    public String toString() {
        return id;
    }
}
