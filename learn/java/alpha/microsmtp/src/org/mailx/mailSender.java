/*
* Copyright (c) 2014, Oleg Bobukh
* MIT License, http://opensource.org/licenses/mit-license.php
* with classpath restrictions.
*
* Данная лицензия разрешает лицам, получившим копию данного
* программного обеспечения и сопутствующей документации
* (в дальнейшем именуемыми «Программное Обеспечение»),
* безвозмездно использовать Программное Обеспечение
* без ограничений, включая неограниченное право на
* использование, копирование, изменение, добавление,
* публикацию, распространение, сублицензирование и/или
* продажу копий Программного Обеспечения, а также лицам,
* которым предоставляется данное Программное Обеспечение,
* при соблюдении следующих условий:
*
* Указанное выше уведомление об авторском праве и данные
* условия должны быть включены во все копии или значимые
* части данного Программного Обеспечения.
*
* ДАННОЕ ПРОГРАММНОЕ ОБЕСПЕЧЕНИЕ ПРЕДОСТАВЛЯЕТСЯ «КАК ЕСТЬ»,
* БЕЗ КАКИХ-ЛИБО ГАРАНТИЙ, ЯВНО ВЫРАЖЕННЫХ ИЛИ ПОДРАЗУМЕВАЕМЫХ,
* ВКЛЮЧАЯ ГАРАНТИИ ТОВАРНОЙ ПРИГОДНОСТИ, СООТВЕТСТВИЯ ПО ЕГО
* КОНКРЕТНОМУ НАЗНАЧЕНИЮ И ОТСУТСТВИЯ НАРУШЕНИЙ, НО НЕ
* ОГРАНИЧИВАЯСЬ ИМИ. НИ В КАКОМ СЛУЧАЕ АВТОРЫ ИЛИ
* ПРАВООБЛАДАТЕЛИ НЕ НЕСУТ ОТВЕТСТВЕННОСТИ ПО КАКИМ-ЛИБО
* ИСКАМ, ЗА УЩЕРБ ИЛИ ПО ИНЫМ ТРЕБОВАНИЯМ, В ТОМ ЧИСЛЕ, ПРИ
* ДЕЙСТВИИ КОНТРАКТА, ДЕЛИКТЕ ИЛИ ИНОЙ СИТУАЦИИ, ВОЗНИКШИМ ИЗ-ЗА
* ИСПОЛЬЗОВАНИЯ ПРОГРАММНОГО ОБЕСПЕЧЕНИЯ ИЛИ ИНЫХ ДЕЙСТВИЙ
* С ПРОГРАММНЫМ ОБЕСПЕЧЕНИЕМ.
*/

package org.mailx;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.butterbrother.iniProp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Формирование и передача сообщения
 */
public class mailSender {

    // Настройки
    private String subject;
    private LinkedList<String> sendTo;
    private LinkedList<String> ccAddr;
    private LinkedList<String> bccAddr;
    private LinkedList<String> attachments;

    // Сообщение
    private MultiPartEmail sendMail = new MultiPartEmail();

    // Разделитель (для 1.5)
    private String SEPARATOR = System.getProperty("line.separator");
    /**
     * Инициализация. Здесь же указываются свойства
     * соединения
     *
     * @param subject       Тема сообщения из cmdline
     * @param fromAddress   От кого - из cmdline
     * @param sendTo        Адресаты
     * @param ccAddr        Копия
     * @param bccAddr       Скрытая копия
     * @param attachments   Вложения
     * @param settings      Настройки (ini-файл)
     */
    public mailSender(
            String subject,
            String fromAddress,
            LinkedList<String> sendTo,
            LinkedList<String> ccAddr,
            LinkedList<String> bccAddr,
            LinkedList<String> attachments,
            iniProp settings
    ) {
        // Определение
        this.subject = subject;
        this.sendTo = sendTo;
        this.ccAddr = ccAddr;
        this.bccAddr = bccAddr;
        this.attachments = attachments;

        // Параметры
        String SMTP_SECTION = "smtp";
        String SERVER = "Server";
        String LOGIN = "Login";
        String PASSWORD = "Password";
        String SSL_ENABLE = "SSL";
        String PORT = "Port";
        String CONNECTION_TIMEOUT = "Timeout";
        String TLS_ENABLE = "TLS";

        // Считываем настройки
        // Имя сервера
        String smtpServer = settings.getParameter(SMTP_SECTION, SERVER);
        if (smtpServer != null)
            sendMail.setHostName(smtpServer);
        else {
            System.err.println("SMTP server not set!");
            System.err.println("It can be set in mailx.ini, section [" + SMTP_SECTION + "], " + SERVER + " parameter");
            System.exit(1);
        }

        // Логин и пароль
        String smtpLogin = settings.getParameter(SMTP_SECTION, LOGIN, "");
        String smtpPassword = settings.getParameter(SMTP_SECTION, PASSWORD, "");
        sendMail.setAuthentication(smtpLogin, smtpPassword);

        // SSL
        sendMail.setSSLOnConnect(settings.getBooleanParameter(SMTP_SECTION, SSL_ENABLE));
        // TLS
        sendMail.setStartTLSRequired(settings.getBooleanParameter(SMTP_SECTION, TLS_ENABLE));

        // Порт сервера
        sendMail.setSmtpPort(
                settings.getIntParameter(SMTP_SECTION, PORT,
                        sendMail.isSSLOnConnect() ? 465 : 25)
        );

        // Таймаут
        try {
            sendMail.setSocketTimeout(
                    settings.getIntParameter(SMTP_SECTION, CONNECTION_TIMEOUT)
            );
        } catch (NumberFormatException ignore) {}

        // Отправитель
        try {
            if (fromAddress != null) {
                sendMail.setFrom(fromAddress);
            } else if (smtpLogin != null) {
                sendMail.setFrom(smtpLogin);
            }
        } catch (EmailException exc) {
            System.err.println("Unable to set \"From\" address: " + exc);
        }
    }
    /**
     * Отправка сообщения
     */
    public void sendMessage() {
        // Получаем тело сообщения
        String messageBody = getBuffer();

        // Указываем тему сообщения
        sendMail.setSubject((subject != null ? subject : "mailx"));

        // Добавляем содержимое сообщения
        try {
            sendMail.setMsg(messageBody);
        } catch (EmailException exc) {
            System.err.println("Unable to set message body text: " + exc);
        }

        // Указываем адресатов
        for (String address : sendTo) {
            try {
                sendMail.addTo(address);
            } catch (EmailException exc) {
                System.err.println("Unable to set address to " + address + ": " + exc);
            }
        }

        // Указываем адресатов в копии
        for (String address : ccAddr) {
            try {
                sendMail.addCc(address);
            } catch (EmailException exc) {
                System.err.println("Unable to set copy to " + address + ": " + exc);
            }
        }

        // Указываем скрытых адресов
        for (String address : bccAddr) {
            try {
                sendMail.addBcc(address);
            } catch (EmailException exc) {
                System.err.println("Unable to set hide copy to " + address + ": " + exc);
            }
        }

        // Добавляем вложения
        for (String item : attachments) {
            EmailAttachment attach = new EmailAttachment();
            attach.setPath(item);
            attach.setDisposition(EmailAttachment.ATTACHMENT);
            try {
                sendMail.attach(attach);
            } catch (EmailException exc) {
                System.err.println("Unable to attach file " + item + ": " + exc);
            }
        }

        // Отправляем
        try {
            sendMail.send();
        } catch (EmailException exc) {
            System.err.println("Send mail error: " + exc);
        }
    }

    /**
     * Получение тела письма. Из stdin либо pipe
     *
     * @return Тело письма
     */
    private String getBuffer() {
        try {
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            if (System.in.available() == 0)
                System.out.println("Enter message (Ctrl+D to end):");
            StringBuilder readed = new StringBuilder();
            String buffer;
            while ((buffer = stdin.readLine()) != null) readed.append(buffer).append(SEPARATOR);
            stdin.close();
            return readed.toString();
        } catch (IOException exc) {
            System.err.println("I/O error: " + exc);
            System.exit(1);
        }
        return "";
    }
}
