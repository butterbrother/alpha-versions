package com.github.butterbrother.ews.redirector.filter;

import java.util.Map;
import java.util.TreeMap;

/**
 * Редактируемый фильтр для сообщений.
 * Состоит из одного или нескольких правил.
 * @deprecated Излишний. В итоге все правила редактируются текстом в таблице с
 * выпадающими списками.
 */
@Deprecated
public class EditableMailFilter {

    private Map<Integer, FilterRule> rules = new TreeMap<>();
    private String name = "New filter";
    private int operator = MailFilter.OPERATOR_AND;

    /**
     * Добавляет новое правило в список правил.
     * Правило добавляется в конец
     * @param rule  Правило фильтрации
     */
    public void addRule(FilterRule rule) {
        rules.put(rules.size(), rule);
    }

    /**
     * Модификация правила
     * @param ruleNumber    номер правила
     * @param rule          правило
     */
    public void modifyRule(int ruleNumber, FilterRule rule) {
        rules.remove(ruleNumber);
        rules.put(ruleNumber, rule);
    }

    /**
     * Смена логического оператора для фильтра.
     * @param operator  Логический оператор
     */
    public void changeOperator(int operator) {
        this.operator = operator;
    }

    /**
     * Удаление правила. Смещает остальные правила.
     * @param ruleNumber    номер правила
     */
    public void removeRule(int ruleNumber) {
        rules.remove(ruleNumber);
        Map<Integer, FilterRule> oldMap = rules;
        rules = new TreeMap<>();

        for (Map.Entry<Integer, FilterRule> item : oldMap.entrySet()) {
            addRule(item.getValue());
        }
    }

    /**
     * Возвращает отображение для правила в таблице
     * @param ruleNumber    номер правила
     * @return              отображение правила для строки таблицы
     */
    public String[] getRuleDisplay(int ruleNumber) {
        return rules.get(ruleNumber).getRuleView();
    }

    /**
     * Возвращает текущий логический оператор
     *
     * @return  логический оператор
     */
    public int getOperator() {
        return operator;
    }

    /**
     * Отдаёт все правила для генерации статичного фильтра.
     * @return  правила
     */
    protected Map<Integer, FilterRule> getRules() {
        return rules;
    }

    /**
     * Отдаёт имя фильтра
     * @return  имя
     */
    public String getName() {
        return name;
    }

    /**
     * Переименовывает фильтр
     * @param name  имя фильтра
     */
    public void rename(String name) {
        this.name = name;
    }

    /**
     * Возвращает массив из массива строк.
     * Для заполнения таблицы AWT с правилами.
     * Вызывается один раз, при инициализации окна редактирования правил.
     *
     * @return  Все правила фильтра
     */
    public String[][] getAllRulesTable() {
        String[][] allRules = new String[rules.size()][];
        for (Map.Entry<Integer, FilterRule> rule : rules.entrySet()) {
            allRules[rule.getKey()] = rule.getValue().getRuleView();
        }

        return allRules;
    }
}
