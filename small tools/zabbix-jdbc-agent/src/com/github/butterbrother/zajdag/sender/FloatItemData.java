package com.github.butterbrother.zajdag.sender;

import org.json.JSONObject;

/**
 * Числовой элемент данных - с плавающей точкой
 */
public class FloatItemData
        extends ItemData {

    // Данные
    private double value;

    /**
     * Заполнение данных элемента данных
     *
     * @param key   Ключ элемента данных
     * @param value Полученное значение
     */
    protected FloatItemData(String key, double value) {
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
