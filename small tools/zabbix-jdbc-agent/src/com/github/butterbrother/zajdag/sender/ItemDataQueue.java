package com.github.butterbrother.zajdag.sender;

import com.github.butterbrother.zajdag.core.logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Очередь данных для элементов данных.
 * Основано на блокирующей очереди, т.к. заполнение данных
 * и отправка параллельны
 */
public class ItemDataQueue
        extends ConcurrentLinkedQueue<ItemData> {

    // Параметры агента: обязательное имя хоста, не обязательные ip и порт
    private String hostname;
    private String ip;
    private int port;

    // Семафор
    private boolean blocked = false;

    // Логгер
    private logger log;

    /**
     * Инициализация очереди с параметрами агента
     *
     * @param hostname Имя хоста
     * @param ip       IP-адрес, не обязательное поле
     * @param port     Порт. Может быть 0, если не обязателен
     */
    public ItemDataQueue(String hostname, String ip, int port, logger log) {
        super();
        this.hostname = hostname;
        this.ip = ip;
        this.port = port;
        this.log = log;
    }

    /**
     * Добавление данных для числового элемента данных - целое числов
     *
     * @param key   Ключ элемента данных
     * @param value Значение
     */
    synchronized public void add(String key, long value) {
        while (blocked) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                log.debug("Interrupted, but ignored (nothing to doing)");
            }
        }
        blocked = true;
        super.offer(new NumericItemData(key, value));
        log.debug("Added integer: [", key, "]-[",value, "]");
        blocked = false;
    }

    /**
     * Добавление данных для числового элемента данных - дробное число
     *
     * @param key   Ключ элемента данных
     * @param value Значение
     */
    synchronized public void add(String key, double value) {
        while (blocked) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                log.debug("Interrupted, but ignored (nothing to doing)");
            }
            blocked = true;
            super.offer(new FloatItemData(key, value));
            log.debug("Added float: [", key, "]-[",value, "]");
            blocked = false;
        }
    }

    /**
     * Добавление данных для текстового элемента данных
     *
     * @param key   Ключ элемента данных
     * @param value Значение
     */
    synchronized public void add(String key, String value) {
        while (blocked) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                log.debug("Interrupted, but ignored (nothing to doing)");
            }
            blocked = true;
            super.offer(new TextItemData(key, value));
            log.debug("Added text: [", key, "]-[",value, "]");
            blocked = false;
        }
    }

    /**
     * Генерация данных JSON для отправки в zabbix.
     * При генерации очередь опустошается.
     * Используется ZabbixSender-ом
     *
     * @return Массив данных JSON
     */
    protected JSONArray getJSONdata() {
        while (blocked) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                log.debug("Interrupted, but ignored (nothing to doing)");
            }
        }
        blocked = true;

        log.debug("Building JSON data array");
        log.debug("Total items count:", super.size());
        JSONArray JSONData = new JSONArray();
        ItemData buffer;
        JSONObject json;
        while ((buffer = super.poll()) != null) {
            json = buffer.getJSONdata();

            json.put("host", hostname);
            if (ip != null)
                json.put("ip", ip);
            if (port > 0)
                json.put("port", port);

            log.debug("Adding ", json);
            JSONData.put(json);
        }

        log.debug("Build complete, now queue size:", super.size());
        blocked = false;

        return JSONData;
    }
}
