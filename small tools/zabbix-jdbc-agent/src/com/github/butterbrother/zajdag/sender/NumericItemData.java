package com.github.butterbrother.zajdag.sender;

import org.json.JSONObject;

/**
 * Числовой элемент данных - целое положительное
 */
public class NumericItemData
        extends ItemData {

    // Полученные целочисленные данные
    private long value;

    /**
     * Заполнение данных элемента данных
     *
     * @param key   Ключ элемента данных
     * @param value Полученное значение
     */
    protected NumericItemData(String key, long value) {
        super(key);
        this.value = value;
    }

    /**
     * Получение данных для отправки в формате JSON
     *
     * @return Данные в JSON
     */
    @Override
    protected JSONObject getJSONdata() {
        return super.getJSONdata().put("value", value);
    }
}
