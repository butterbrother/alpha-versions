package com.github.butterbrother.zajdag.sender;

import com.github.butterbrother.zajdag.core.logger;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

/**
 * Выполняет отправку данные в zabbix, как активный агент
 * <p>
 * Спецификация доступна тут:
 * https://www.zabbix.com/documentation/2.0/ru/manual/appendix/items/activepassive
 * https://www.zabbix.org/wiki/Docs/protocols/zabbix_agent/2.0
 */
public class ZabbixSender
        implements Runnable {
    // Собственное имя хоста
    private String hostName;
    // Собственный IP. Если пустое или null, то используется только имя хоста
    private String hostIP;
    // Собственный порт для пассивных проверок. Если 0 - не используется. Иначе
    // используется в сочетарии с hostIP
    private int port;
    // Имя хоста Zabbix-сервера
    private String zabbixHostname;
    // Порт Zabbix-сервера
    private int zabbixPort;
    // Логгер, для выполнения логгирования
    private logger log;
    // Интервал отправки данных
    private int interval;
    // Состояние потока, переключалка
    private boolean active = false;
    // Флаг завершения работ
    private boolean shuttedDown = true;

    // Очередь данных для отправки
    private ItemDataQueue queue;

    /**
     * Инициализация активного агента.
     * Если указан hostIP и порт отличен от 0, то эта пара используется
     * при запросе активной проверки
     *
     * @param hostName     Собственное имя хоста. Может быть пустым и null
     * @param hostIP       Собственный IP
     * @param port         Порт для пассивной проверки
     * @param zabbixServer Имя zabbix-сервера
     * @param log          Логгер
     * @param delay        Пауза между сообщениями, в секундах
     */
    public ZabbixSender(String hostName, String hostIP, int port, String zabbixServer,
                        logger log, int delay) {
        this.hostName = hostName;
        this.hostIP = hostIP;
        this.port = port;
        this.log = log;
        this.interval = delay;
        this.queue = new ItemDataQueue(hostName, hostIP, port, log.getModuleLogger("Item Data Queue"));

        // Разделяем имя сервера и порт
        StringTokenizer destination = new StringTokenizer(zabbixServer, ":");
        zabbixHostname = destination.nextToken();
        zabbixPort = 10050;
        if (destination.hasMoreElements()) {
            try {
                zabbixPort = Integer.parseInt(destination.nextToken());
            } catch (NumberFormatException e) {
                log.warning(e, "Unable to parse port number");
            }
        }
        log.debug("Zabbix hostname set as ", zabbixHostname, ", Zabbix port set as ", zabbixPort);
    }

    /**
     * Получение очереди для отправки данных
     *
     * @return Очередь для отправки данных
     */
    public ItemDataQueue getQueue() {
        return queue;
    }

    /**
     * Формирование данные для передачи в zabbix
     *
     * @param message Сообщение
     * @return Данные для пересылки
     */
    private byte[] buildMessageData(String message) {
        // Данные сообщения, сконвертированные в байты, кодировка - UTF-8
        byte[] messageData = message.getBytes(Charset.forName("UTF-8"));

        // Длина сообщения
        int length = messageData.length;

        // Заголовок сообщения
        byte[] header = {
                'Z', 'B', 'X', 'D', '\1', // Заголовок, "ZBXD\x01", 5 байт
                // DATALEN - длина данных, 8 байт в HEX, 64-битная последовательность
                (byte) (length & 0xFF),
                (byte) (length >> 8 & 0x00FF),
                (byte) (length >> 16 & 0x0000FF),
                (byte) (length >> 24 & 0x000000FF),
                '\0', '\0', '\0', '\0',
        };
        log.debug(header, "Message header");

        // Формируем сообщение
        byte[] readyMessage = new byte[header.length + length];
        System.arraycopy(header, 0, readyMessage, 0, header.length);
        System.arraycopy(messageData, 0, readyMessage, header.length, length);

        return readyMessage;
    }


    /**
     * Создание запроса на получение списка элемента данных.
     * В ответ на это сообщение сервер возвращает список пробников,
     * которые могут мониториться. Либо ошибку.
     *
     * @return Запрос
     */
    private byte[] createActiveChecksRequest() {
        JSONObject activeRequest = new JSONObject()
                .put("host", hostName)
                .put("request", "active checks");

        if (hostIP != null)
            activeRequest.put("ip", hostIP);

        if (port != 0)
            activeRequest.put("port", port);

        log.debug("Active request is ", activeRequest);
        return buildMessageData(activeRequest.toString());
    }

    /**
     * Активизация отправителя данных
     */
    public void activate() {
        log.debug("Zabbix sender activated");
        this.active = true;
        new Thread(this).start();
    }

    /**
     * Деактивация отправителя данных.
     * Не сбрасывает его состояние.
     * Можно продолжить вновь через activate
     */
    public void deactivate() {
        log.debug("Zabbix sender deactivated");
        this.active = false;
    }

    /**
     * Отправка данных в Zabbix
     *
     * @param data Передаваемые данные
     * @return Ответ от сервера, сразу в JSON
     */
    private JSONObject sendDataToZabbix(byte[] data) {
        // Билдер для ответа от сервера
        StringBuilder responseBuilder = new StringBuilder();

        // Создаём подключение
        log.debug("Opening connection to ", zabbixHostname, ":", zabbixPort);
        try (Socket connection = new Socket(zabbixHostname, zabbixPort);
             InputStream connectionInput = connection.getInputStream();
             BufferedOutputStream connectionOutput = new BufferedOutputStream(connection.getOutputStream())
        ) {
            // Запрашиваем информацию о доступных элементах данных
            log.debug("Writing data");
            connectionOutput.write(data);
            connectionOutput.flush();

            // Ждём ответные данные
            log.debug("Waiting response");
            while (connectionInput.available() == 0 && active) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException intE) {
                    log.debug("Interrupted");
                    active = false;
                }
            }

            // Пропускаем первые 13 байт с заголовком
            log.debug("Skipping 13 bytes");
            long skipped = connectionInput.skip(13);
            log.debug("Skipped ", skipped, " bytes");
            // Считываем остальной ответ сервера
            String buffer;
            try (BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(connectionInput, Charset.forName("UTF-8")))) {
                while ((buffer = responseBuffer.readLine()) != null)
                    responseBuilder.append(buffer);
            }
        } catch (UnknownHostException e) {
            log.error(e, "Unknown host: " + zabbixHostname + ":" + zabbixPort);
        } catch (IOException e) {
            log.error(e, "Connection error");
        }
        log.debug("Connection closed");

        return new JSONObject(responseBuilder.toString());
    }

    /**
     * Проверка успешности передачи данных на отсутствие фатальных ошибок
     *
     * @param response Полученные данные от сервера
     * @return Отсутствие фатальных ошибок
     */
    private boolean checkResponseResult(JSONObject response) {
        // Смотрим, есть ли сообщения об ошибке.
        log.debug("Checking response");
        String responseResult = response.getString("response");
        if (responseResult.equals("failed")) {
            log.warning(response.get("info"));
            return false;
        } else {
            log.debug("Success response:");
            log.debug(response.get("data"));
            return true;
        }
    }

    /**
     * Отправка активного запроса.
     * Ответ от сервера не анализируется, только поиск неудачного ответа.
     * Поэтому выполняется однократно, при старте.
     * Далее просто упорно отсылаем данные.
     */
    private void sendActiveRequest() {
        // Отправляем запрос
        log.debug("Sending active request");
        JSONObject response = sendDataToZabbix(createActiveChecksRequest());
        log.debug("Server response: ", response);
        // Просто проверяем успешность передачи, дальше не парсим
        checkResponseResult(response);
    }

    /**
     * Отправка накопленных данных
     */
    private void sendItemsData() {
        // Формируем запрос
        log.debug("Building JSON-format items data");
        JSONObject request = new JSONObject()
                .put("request", "agent data")
                .put("data", queue.getJSONdata())
                .put("clock", System.currentTimeMillis() / 1000L);
        log.debug("Request: ", request);

        // Отправляем
        log.debug("Sending data");
        JSONObject response = sendDataToZabbix(buildMessageData(request.toString()));
        log.debug("Server response: ", response);

        // Проверяем, есть ли фатальные ошибки, если нет - выводим результат отправки в лог
        if (checkResponseResult(response))
            log.info(response.get("info"));
    }

    /**
     * Основной рабочий поток.
     * Запуск через activate
     * Останове через deactivate
     * Важно, что deactivate не сбрасывает состояние агента
     */
    @Override
    public void run() {
        shuttedDown = false;
        // Выполняем запрос на активный мониторинг
        sendActiveRequest();

        while (active) {
            // Далее пауза, в ходе которой мы собираем данные
            try {
                log.debug("Sleeping " + interval * 1000 + " ms.");
                Thread.sleep(interval * 1000);
            } catch (InterruptedException e) {
                active = false;
            }
            // Отправка данных. Отправляем данные только если есть, что отправлять
            if (active && queue.size() > 0) {
                sendItemsData();
            }
        }

        log.debug("Zabbix sender switched down");
        shuttedDown = true;
    }

    /**
     * Получение статуса отправителя
     *
     * @return  Статус отправителя
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Получение статуса, что отправитель остановлен
     *
     * @return  состояние
     */
    public boolean isShuttedDown() {
        return shuttedDown;
    }
}
