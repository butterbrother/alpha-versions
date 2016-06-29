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

import com.github.butterbrother.s2s_connection_checker.Config.ConfigFile;
import com.github.butterbrother.s2s_connection_checker.Config.Target;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

/**
 * Управляет соединениями к целевым серверам.
 * Исползуется в режиме клиента.
 */
public class ClientController
        implements Runnable {

    /**
     * Логгер контроллера
     */
    private Logger log;

    /**
     * Флаг активности процесса
     */
    private boolean active = true;

    /**
     * Файл конфигурации
     */
    private ConfigFile configFile;

    /**
     * Массив со списком клиентов
     */
    private ConcurrentLinkedDeque<WorkerThread> workerThreads = null;

    /**
     * Поток процесса управления клиентами
     */
    private Thread masterThread = null;

    /**
     * Инициализация.
     *
     * @param configFile файл конфигурации
     * @param logger     логгер
     */
    public ClientController(ConfigFile configFile, Logger logger) {
        log = Logger.getLogger("Connection manager");
        log.setParent(logger);
        this.configFile = configFile;

        if (configFile.getTargets().length == 0) {
            active = false;

            log.info("Targets not set. Please set it via list into section " + ConfigFile.TARGET_HEADER + " in configuration file");
        } else {

            masterThread = new Thread(this);
            masterThread.start();
        }
    }

    /**
     * Основной рабочий процесс управления клиентскими соединениями.
     */
    public void run() {
        int index = 0;

        workerThreads = new ConcurrentLinkedDeque<>();

        log.info("Starting connections");

        // Запускаем все рабочие процессы
        for (Target target : configFile.getTargets()) {
            workerThreads.add(new DefaultClient(
                    this,
                    target,
                    log,
                    index++,
                    configFile.getCommands()
            ));
        }

        // Ждём
        while (active) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException interrupted) {
                log.info("Interrupted");
                active = false;
            }
        }

        // Останавливаем все рабочие процессы
        for (WorkerThread thread : workerThreads)
            thread.inactivate();

        // Ждём, пока они не завершатся. Каждый рабочий процесс при завершении дёргает
        // ожидаем их завершения (они вызывают workerDome() по завершению, удаляющий их из списка. т.е. ждём очистки списка)
        while (workerThreads.size() > 0)
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
                System.exit(1);
            }

        log.info("Stopped");
    }

    /**
     * Запрос на остановку клиента
     */
    public void disable() {
        active = false;
        masterThread.interrupt();
    }

    /**
     * Сигнал о том, что рабочий процесс завершил работу.
     * Вызывается со стороны рабочего процесса.
     * Это удаляет его из списка процессов.
     *
     * @param worker рабочий процесс
     */
    void workerDone(DefaultClient worker) {
        if (workerThreads != null)
            workerThreads.remove(worker);
    }

    /**
     * Присоединение к потоку для ожидания
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
