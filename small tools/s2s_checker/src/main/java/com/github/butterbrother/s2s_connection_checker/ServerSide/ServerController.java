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

import com.github.butterbrother.s2s_connection_checker.Config.ConfigFile;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Обеспечивает приём и обработку подключений в режиме сервера.
 * <p>
 * Это основной поток, который порождает и управляет остальными потоками с работающими сокетами.
 */
public class ServerController implements Runnable {
    private boolean active = true;
    private ConfigFile configFile;
    private ServerSocket currentServerSocker = null;
    private Thread masterThread = null;
    private ConcurrentLinkedDeque<ConnectionHandler> connectionHandlers = null;
    private Logger log;
    private Logger masterLog;

    /**
     * Инициализация управляющего потока.
     *
     * @param configFile файл конфигурации
     */
    public ServerController(ConfigFile configFile, Logger logger) {
        this.configFile = configFile;

        masterLog = logger;
        log = Logger.getLogger("Listener");
        log.setParent(logger);

        // Если нет белого списка разрешённых клиентов, то сразу завершаем работу
        if (configFile.getAllows().length == 0) {
            active = false;

            log.info("Allows client not set. Please set it via list into section " + ConfigFile.ALLOW_HEADER + " in configuration file");

        } else {
            masterThread = new Thread(this);
            masterThread.start();
        }
    }

    /**
     * Основной управляющий поток.
     * Создаёт серверный сокет, который принимает и обрабатывает входящие подключения.
     */
    public void run() {
        connectionHandlers = new ConcurrentLinkedDeque<>();

        int workerIndex = 0;

        while (active) {
            try (ServerSocket server = new ServerSocket(configFile.getPort())) {

                log.info("Waiting incoming");

                // Сохраняем его
                currentServerSocker = server;

                // Настраиваем предпочтения
                server.setPerformancePreferences(0, 1, 0);  // предпочитаем низкую задержку в ущебр скорости

                while (active) {
                    try {
                        // Ожидаем подключение
                        if (log.isLoggable(Level.FINE))
                            log.fine("Accepting inbound connection");

                        Socket inbound = server.accept();

                        if (log.isLoggable(Level.FINE))
                            log.fine("Accepted");

                        // Выполняем проверку, что клиент в списке разрешённых
                        if (checkInetAddress(inbound)) {
                            log.info("Incoming connection from " + inbound.getInetAddress().toString() + " accepted.");

                            // Даём добро
                            connectionHandlers.add(new ConnectionHandler(inbound, workerIndex++, this, masterLog));
                        } else
                            log.info("Incoming connection from " + inbound.getInetAddress().toString() + " rejected.");

                    } catch (SocketTimeoutException timeout) {
                        log.info("Waiting incoming");
                    }
                }

            } catch (IOException ioErr) {
                log.warning(ioErr.getMessage());

                currentServerSocker = null;
            }
        }

        log.info("Shutting down");

        // Останавливаем все обработчики
        for (ConnectionHandler thread : connectionHandlers)
            thread.inactivate();

        // ожидаем их завершения (они вызывают workerDome() по завершению, удаляющий их из списка. т.е. ждём очистки списка)
        while (connectionHandlers.size() > 0)
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
                System.exit(1);
            }

        log.info("Stopped");
    }

    /**
     * Сверяет адрес клиента со списокм разрешённых адресов.
     *
     * @param inboundSocket входящее подключение
     * @return true - адрес находится в белом списке
     */
    private boolean checkInetAddress(Socket inboundSocket) {
        InetAddress incoming = inboundSocket.getInetAddress();

        for (InetAddress allowed : configFile.getAllows())
            if (Arrays.equals(allowed.getAddress(), incoming.getAddress()))
                return true;

        return false;
    }

    /**
     * Отключение сервера, плавная остановка приложения с закрытием всех соединений.
     */
    public void disable() {
        active = false;
        try {
            if (currentServerSocker != null)
                currentServerSocker.close();
        } catch (IOException ioErr) {
            ioErr.printStackTrace();
        }
    }

    /**
     * Вызов со стороны рабочего процесса, что тот завершил работу.
     *
     * @param worker рабочий процесс
     */
    void workerDone(ConnectionHandler worker) {
        if (connectionHandlers != null) {
            connectionHandlers.remove(worker);
        }
    }

    /**
     * Присоединение к потоку
     */
    public void join() {
        try {
            if (masterThread.isAlive())
                masterThread.join();
        } catch (InterruptedException ignore) {
            log.info("Interrupted");
            disable();
        }

        try {
            if (masterThread.isAlive())
                masterThread.join();
        } catch (InterruptedException ignore) {
            System.exit(0);
        }
    }

}
