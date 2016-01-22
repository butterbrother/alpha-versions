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
     * @param rawRule   Строковое правило из таблицы
     */
    public FilterRule(String[] rawRule) {
        ruleType = -1;
        ruleOperator = -1;
        for (int i = 0; i < RuleTypes.length; ++i) {
            if (rawRule[0].equals(RuleTypes[i])) {
                ruleType = i;
                break;
            }
        }
        for (int i = 0; i < RuleOperators.length; ++i) {
            if (rawRule[1].equals(RuleOperators[i])) {
                ruleOperator = i;
                break;
            }
        }
        ruleValue = rawRule[2].trim();
        if (ruleType < 0 || ruleOperator < 0)
            throw new ArrayIndexOutOfBoundsException();
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
     * @return  возвращает тип правила
     */
    public int getType() {
        return ruleType;
    }
}
