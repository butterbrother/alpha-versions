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

import java.net.InetAddress;

/**
 * Целевые сервера для клиентского режима.
 * Хранилище для адреса и порта.
 * Используется в режиме клиента для рабочих процессов
 * {@link com.github.butterbrother.s2s_connection_checker.ClientSide.WorkerThread}
 */
public class Target
        implements Comparable<Target> {
    private InetAddress address;
    private int port;
    private int hashCode;
    private String id;

    /**
     * Стандартная инициализация
     *
     * @param address адрес цели
     * @param port    порт цели
     */
    Target(InetAddress address, int port) {
        this.address = address;
        this.port = port;

        hashCode = address.hashCode() + port;

        if (address.getHostName().isEmpty())
            id = address.getHostAddress() + ":" + port;
        else
            id = address.getHostName() + " (" + address.getHostAddress() + ":" + port + ")";
    }

    /**
     * Получение адреса цели
     *
     * @return адрес цели
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * Получение порта цели
     *
     * @return порт цели
     */
    public int getPort() {
        return port;
    }

    /**
     * Отдаёт хеш-код.
     * Хеш-код генерируется из суммы хеш-кода адреса (InetAddress) и порта.
     * @return  хеш-код
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
     * Выполнение сравнения двух целей.
     * @param o другая цель, для сравнения
     * @return  результат сравнения
     */
    @Override
    public int compareTo(Target o) {
        return hashCode - o.hashCode;
    }

    /**
     * Сравнение двух целей.
     * @param o другая цель
     * @return  true - теоретически, цели одинаковые
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Target && hashCode == o.hashCode();
    }

    /**
     * Отображение в виде строки.
     *
     * @return  Отдаётся сгенерированный ID, содержащий
     * имя хоста (IP:порт)
     * либо
     * IP:порт
     * если не удалось определить имя хоста
     */
    @Override
    public String toString() {
        return id;
    }
}
