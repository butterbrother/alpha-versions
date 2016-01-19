package com.github.butterbrother.ews.redirector.filter;

import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;

/**
 * Рабочий статичный фильтр для проверки и фильтрации
 * сообщений. Конвертируется из редактируемого фильтра
 */
public class MailFilter {
    public static final int OPERATOR_AND = 0;
    public static final int OPERATOR_OR = 1;
    public static final String[] Operators = { "And", "Or" };

    private int operator;
    private FilterRule[] rules;

    protected MailFilter(FilterRule[] rules, int operator) {
        this.operator = operator;
        this.rules = rules;
    }

    /**
     * Преобразует редактируемый фильтр в статичный
     * @param editable  редактируемый фильтр
     * @return          статичный фильтр
     */
    public static MailFilter convert(EditableMailFilter editable) {
        FilterRule[] rules = new FilterRule[editable.getRules().size()];
        editable.getRules().values().toArray(rules);

        return new MailFilter(rules, editable.getOperator());
    }

    /**
     * Фильтрация сообщения
     *
     * @param filters   текущие фильтры
     * @param message   сообщение
     * @return          true - один или несколько фильтров сработали на сообщении
     * @throws ServiceLocalException
     */
    public static boolean filtrate(MailFilter[] filters, EmailMessage message) throws ServiceLocalException {
        for (MailFilter filter : filters)
            if (filter.check(message))
                return true;

        return false;
    }

    /**
     * Фильтрация сообщения
     * @param message   сообщение
     * @return          true - одно или несколько правил сработали на сообщении
     */
    protected boolean check(EmailMessage message) throws ServiceLocalException {
        if (operator == OPERATOR_AND) {
            for (FilterRule rule : rules) {
                if (! checkRule(rule, message))
                    return false;
            }

            return true;
        } else {
            for (FilterRule rule : rules) {
                if (checkRule(rule, message))
                    return true;
            }

            return false;
        }
    }


    /**
     * Проверка сообщения на срабатывание по правилу
     * @param rule      правило
     * @param message   сообщение
     * @return          true - правило сработало для данного сообщения
     * @throws ServiceLocalException
     */
    private boolean checkRule(FilterRule rule, EmailMessage message) throws ServiceLocalException {
        switch (rule.getType()) {
            case FilterRule.TYPE_FROM:
                return rule.check(message.getFrom().getAddress());
            case FilterRule.TYPE_SUBJECT:
                return rule.check(message.getSubject());
            case FilterRule.TYPE_MESSAGE:
                return rule.check(message.getBody().toString());
            case FilterRule.TYPE_TO:
                return checkAddressesRule(rule, message.getToRecipients());
            case FilterRule.TYPE_CC:
                return checkAddressesRule(rule, message.getCcRecipients());
            case FilterRule.TYPE_BCC:
                return checkAddressesRule(rule, message.getBccRecipients());
        }

        return false;
    }

    /**
     * Проверяет на соответствие правилу каждый адрес в списке.
     * Необходимо для обработки правил для списка e-mail: получатели, в копии и т.д.
     * @param rule  Правило
     * @param recipients    Список адресов
     * @return  true - правило сработало на одном или нескольких адресатах
     */
    private boolean checkAddressesRule(FilterRule rule, EmailAddressCollection recipients) {
        for (EmailAddress address : recipients.getItems()) {
            if (rule.check(address.getAddress()))
                return true;
        }

        return false;
    }

}
