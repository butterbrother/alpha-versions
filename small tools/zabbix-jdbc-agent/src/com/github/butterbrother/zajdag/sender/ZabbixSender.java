package com.github.butterbrother.zajdag.sender;

import com.github.butterbrother.zajdag.core.logger;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.concurrent.SynchronousQueue;

/**
 * Выполняет отправку данные в zabbix, как активный агент
 * <p/>
 * Спецификация доступна тут:
 * https://www.zabbix.com/documentation/2.0/ru/manual/appendix/items/activepassive
 * https://www.zabbix.org/wiki/Docs/protocols/zabbix_agent/2.0
 */
public class ZabbixSender {
    // Собственное имя хоста
    private String hostName;
    // Собственный IP. Если пустое или null, то используется только имя хоста
    private String hostIP;
    // Собственный порт для пассивных проверок. Если 0 - не используется. Иначе
    // используется в сочетарии с hostIP
    private int port;
    // Имена хоста Zabbix-сервера
    private String zabbixServer;
    // Логгер, для выполнения логгирования
    private logger log;
    // Интервал отправки данных
    private int interval;

    // Очередь данных
    private SynchronousQueue<ItemData> dataQueue;

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
     * @param dataQueue    Очередь собранных данных
     */
    public ZabbixSender(String hostName, String hostIP, int port, String zabbixServer,
                        logger log, int delay, SynchronousQueue<ItemData> dataQueue) {
        this.hostName = hostName;
        this.hostIP = hostIP;
        this.port = port;
        this.zabbixServer = zabbixServer;
        this.log = log;
        this.interval = delay;
        this.dataQueue = dataQueue;
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
        if (hostIP != null && port != 0) {
            return buildMessageData(
                    new JSONObject()
                            .put("host", hostName)
                            .put("ip", hostIP)
                            .put("port", port)
                            .put("request", "active checks")
                            .toString());
        } else {
            return buildMessageData(
                    new JSONObject()
                            .put("request", "active checks")
                            .put("host", hostName)
                            .toString());
        }
    }

    /*
    // Это проба отправки данных
    public static void main(String args[]) {
        ZabbixSender sender = new ZabbixSender(new String[0], new String[0]);

        try {
            InetAddress zabbixServer = InetAddress.getByName("enswiki.vimpelcom.ru");
            System.out.println("Opening socket");
            try (Socket connection = new Socket(zabbixServer, 10051)) {
                try (OutputStream reqSender = connection.getOutputStream();
                     InputStream responce = connection.getInputStream()) {
                    System.out.println("Sending request");
                    System.out.println(new String(sender.createActiveChecksRequest("mn-b2b.vimpelcom.ru")));
                    reqSender.write(sender.createActiveChecksRequest("mn-b2b.vimpelcom.ru"));
                    reqSender.flush();
                    System.out.println("Request send");
                    while (responce.available() == 0) {
                        Thread.sleep(100);
                        System.out.print("waiting... ");
                    }
                    Thread.sleep(0);
                    System.out.println();
                    BufferedReader result = new BufferedReader(new InputStreamReader(responce, "UTF-8"));
                    String buffer;
                    while ((buffer = result.readLine()) != null) {
                        System.out.println(buffer);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
                    */
}
