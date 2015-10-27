package com.github.butterbrother.zajdag.sender;

import org.json.JSONObject;

/**
 * Текстовый элемент данных
 */
public class TextItemData
        extends ItemData {

    private String value;

    /**
     * Заполнение данных элемента данных
     *
     * @param key   Ключ элемента данных
     * @param value Полученное значение
     */
    public TextItemData(String key, String value) {
        super(key);
        this.value = value;
    }

    /**
     * Получение данных для отправки в формате JSON
     *
     * @return Данные в JSON
     */
    @Override
    public JSONObject getJSONdata() {
        return super.getJSONdata().put("value", value);
    }
}
