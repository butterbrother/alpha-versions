package com.github.butterbrother.ews.redirector.filter;

/**
 * Правило для фильтрации сообщения.
 * Из таких правил формируется одиночный фильтр.
 */
public class FilterRule {
    public static final int TYPE_FROM = 0;
    public static final int TYPE_TO = 1;
    public static final int TYPE_CC = 2;
    public static final int TYPE_BCC = 3;
    public static final int TYPE_SUBJECT = 4;
    public static final int TYPE_MESSAGE = 5;

    public static final int OPERATOR_EQUALS = 0;
    public static final int OPERATOR_NOT_EQUALS = 1;
    public static final int OPERATOR_CONTAINS = 2;
    public static final int OPERATOR_NOT_CONTAINS = 3;

    // Порядок должен соответствовать числовым константам, т.к. используется для списка
    public static final String[] RuleTypes = {
            "From",
            "To",
            "Copy",
            "Hide copy",
            "Subject",
            "Message"
    };

    public static final String[] RuleOperators = {
            "==",
            "<>",
            "contains",
            "not contains"
    };

    private int ruleType;
    private int ruleOperator;
    private String ruleValue;

    /**
     * Инициализация правила
     * @param type      Тип правила
     * @param operator  Оператор сравнения
     * @param value     Значение правила
     */
    public FilterRule(int type, int operator, String value) {
        ruleType = type;
        ruleOperator = operator;
        ruleValue = value.toLowerCase();
    }

    /**
     * Проверка выполнения правила для данных.
     * @param data  Данные
     * @return      Соответствие правилу фильтра
     */
    public boolean check(String data) {
        switch (ruleOperator) {
            case OPERATOR_EQUALS:
                return data.equalsIgnoreCase(ruleValue);
            case OPERATOR_NOT_EQUALS:
                return ! data.equalsIgnoreCase(ruleValue);
            case OPERATOR_CONTAINS:
                return data.toLowerCase().contains(ruleValue);
            case OPERATOR_NOT_CONTAINS:
                return ! data.toLowerCase().contains(ruleValue);
        }

        return false;
    }

    /**
     * Созадёт представление для таблицы в интерфейсе, AWT/Swing
     * @return  строка для таблицы из трёх столбцов
     */
    public String[] getRuleView() {
        return new String[] { RuleTypes[ruleType], RuleOperators[ruleOperator], ruleValue };
    }

    /**
     * Получить тип правила
     * @return
     */
    public int getType() {
        return ruleType;
    }

    /**
     * Получить оператор сравнения правила
     * @return
     */
    public int getOperator() {
        return ruleOperator;
    }
}
