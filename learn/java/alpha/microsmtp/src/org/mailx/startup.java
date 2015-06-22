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

import org.butterbrother.coreTools;
import org.butterbrother.iniProp;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Попытка реализовать кросс-платформенный mailx с поддержкой SMTP
 */
public class startup {

    private static String[] ini4jNotFound = {
            "Ini4j library not found",
            "This library need to work with ini-files",
            "You can download it from site:",
            "http://sourceforge.net/projects/ini4j/files/ (-bin or -all version)",
            "unpack and place jar-file into \"lib\" directory"
    };
    private static String[] aceNotFound = {
            "Apache commons email library not found",
            "This library need to work with smtp",
            "You can download it from site:",
            "http://commons.apache.org/proper/commons-email/download_email.cgi (Binaries)",
            "unpack and place jar-file into \"lib\" directory"
    };
    private static String[] javaxMailNotFound = {
            "Java Mail API library not found",
            "This library need to work with e-mail",
            "You can download it from site:",
            "http://java.net/projects/javamail/downloads/download/javax.mail.jar",
            "and place it into \"lib\" directory"
    };



    /**
     * Начало
     *
     * @param args command-line arguments
     */
    public static void main(String args[]) {
        // Подгружаем все jar-файлы из lib
        coreTools.libPreloader();
        // Проверка наличия библиотек
        String ini4jPath = "org.ini4j.Ini";
        coreTools.checkClassAvailability(ini4jPath, ini4jNotFound, 1);
        String acePath = "org.apache.commons.mail.Email";
        coreTools.checkClassAvailability(acePath, aceNotFound, 1);
        String javaxMailPath = "javax.mail.Authenticator";
        coreTools.checkClassAvailability(javaxMailPath, javaxMailNotFound, 1);
        // Сообщение
        String subject = "";
        LinkedList<String> attachments = new LinkedList<String>();
        LinkedList<String> sendTo = new LinkedList<String>();
        LinkedList<String> ccAddr = new LinkedList<String>();
        LinkedList<String> bccAddr = new LinkedList<String>();
        String fromAddress = null;
        // Парсим ключи
        for (int i = 0; i < args.length; i++) {
            String loCase = args[i].toLowerCase();
            // Топик
            if (loCase.equals("-s") && i + 1 < args.length) {
                subject = args[i + 1];
                i++;
                continue;
            }
            // Вложение
            if (loCase.equals("-a") && i + 1 < args.length) {
                attachments.add(args[i + 1]);
                i++;
                continue;
            }
            // Копия
            if (loCase.equals("-c") && i + 1 < args.length) {
                ccAddr.add(args[i + 1]);
                i++;
                continue;
            }
            // Скрытая копия
            if (loCase.equals("-b") && i + 1 < args.length) {
                bccAddr.add(args[i + 1]);
                i++;
                continue;
            }
            // От кого
            if (loCase.equals("-r") && i + 1 < args.length) {
                fromAddress = args[i + 1];
                i++;
                continue;
            }
            // Справка
            if (loCase.equals("-h"))
                getHelp();
            // Получатели, без ключей
            sendTo.add(args[i]);
        }

        if (args.length == 0)
            getHelp();

        // Считываем настройки
        iniProp settings = readSettings();
        // Отправляем
        mailSender sender = new mailSender(
                subject,
                fromAddress,
                sendTo,
                ccAddr,
                bccAddr,
                attachments,
                settings
        );
        sender.sendMessage();
    }

    /**
     * Чтение настроек из ini-файла
     */
    private static iniProp readSettings() {
        try {
            return new iniProp(new File(coreTools.getJarFileLocation(), "mailx.ini"));
        } catch (IOException exc) {
            System.err.println("Unable to read ini file: " + exc);
            System.exit(1);
        }

        return new iniProp();
    }

    /**
     * Вывод справочной информации.
     * Завершает работу приложения
     */
    private static void getHelp() {
        String[] helpMessage = {
                "mailx - send internet mail",
                "java -jar mailx.jar [-s subject] [-a attachment] [-c cc-addr] [-b bcc-addr] [-r from addr] to-address",
                "Config stored in mailx.ini",
                "Message text get from pipe (echo \"Message body\" | java -jar maix)",
                "or keyboard input (if pipe empty)",
                "",
                "mailx.ini structure:",
                "[smtp]",
                "Server = smtp server name, required",
                "SSL = yes or no - enable/disable ssl, optional, default - no",
                "Login = login, optional, default - empty",
                "Password = password, optional, default - empty",
                "Port = smtp port, optional, default - 25 or 465",
                "Timeout = connection timeout, default not set",
                "TLS = yes or no - require tls on connection, default - no"
        };
        for (String item : helpMessage)
            System.out.println(item);
        System.exit(0);
    }


}
