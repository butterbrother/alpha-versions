package com.github.butterbrother.zajdag.sender;

import org.json.JSONObject;

/**
 * Данные для элемента данных zabbix
 */
public class ItemData {
    // Ключ элемента данных
    private String key;
    // Значение элемента данных
    private Object value;
    // Дата получения результата
    private long clock;

    /**
     * Заполнение данных элемента данных
     *
     * @param key   ключ элемента данных
     * @param value результат
     */
    public ItemData(String key, Object value) {
        this.key = key;
        this.value = value;
        clock = System.currentTimeMillis() / 1000L;
    }

    /**
     * Получение данных для отправки, в формате JSON
     *
     * @return Данные в JSON
     */
    public JSONObject getJSONdata(String hostName) {
        return new JSONObject()
                .put("host", hostName)
                .put("key", key)
                .put("value", value)
                .put("clock", clock);
    }
}
