package com.github.butterbrother.zajdag.sender;

import org.json.JSONObject;

/**
 * Данные для элемента данных zabbix
 * Абстрактный класс для типизированных элементов данных.
 */
abstract public class ItemData {
    // Ключ элемента данных
    private String key;
    // Дата получения результата
    private long clock;

    /**
     * Заполнение данных элемента данных
     *
     * @param key ключ элемента данных
     */
    protected ItemData(String key) {
        this.key = key;
        clock = System.currentTimeMillis() / 1000L;
    }

    /**
     * Получение данных для отправки, в формате JSON
     *
     * @return Данные в JSON
     */
    protected JSONObject getJSONdata() {
        return new JSONObject()
                .put("key", key)
                .put("clock", clock);
    }
}
