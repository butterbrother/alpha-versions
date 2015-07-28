package org.butterbrother.odbased;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Клиентский сокет
 */
public class ClientSocket implements Cloneable, AutoCloseable, staticValues {
    private Socket client;
    private InputStream serverData;
    private BufferedReader serverResponse;
    private PrintWriter clientRequests;

    /**
     * Инициализация
     *
     * @param port Порт сервиса
     * @throws IOException
     */
    public ClientSocket(int port) throws IOException {
        client = new Socket(InetAddress.getLocalHost(), port);
        serverData = client.getInputStream();
        serverResponse = new BufferedReader(new InputStreamReader(serverData, "UTF-8"));
        clientRequests = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"), true);
    }

    /**
     * Получение информации, есть ли ответные данные
     *
     * @return Наличие данных от сервиса
     * @throws IOException
     */
    public boolean haveResponse() throws IOException {
        return serverData.available() > 0;
    }

    /**
     * Отправка и досыл запроса. Для завершения запроса необходимо
     * запустить requestsDone.
     *
     * @param message Сообщение либо часть сообщения
     * @throws IOException
     */
    public void sendRequest(String message) throws IOException {
        clientRequests.println(message);
    }

    /**
     * Отправка служебного сообщения, что запрос полностью передана
     *
     * @throws IOException
     */
    public void requestDone() throws IOException {
        clientRequests.println(BREAK_STRING);
    }

    /**
     * Получение ответа от сервера. Предварительно желательно проверить
     * на наличие ответа с помощью haveResponse
     * Если ответа нет, может быть возвращена пустая строка.
     * Ожидается служебное сообщение, что ответ полностью передан
     *
     * @return Ответ сервиса
     * @throws IOException
     */
    public String getResponse() throws IOException {
        if (haveResponse()) {
            String buffer;
            StringBuilder response = new StringBuilder();
            while ((buffer = serverResponse.readLine()) != null) {
                if (buffer.equalsIgnoreCase(BREAK_STRING)) break;
                response.append(buffer).append('\n');
            }
            return response.toString();
        }

        return "";
    }

    /**
     * Закрытие соединения
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        clientRequests.close();
        serverResponse.close();
        client.close();
    }
}
