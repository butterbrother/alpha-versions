package org.butterbrother.odbased;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Сокет сервиса, обрабатывающего локальные подключения
 */
public class serviceSocket implements Runnable, staticValues {
    // Сервисный сокет
    private ServerSocket daemon;
    // Клиентские сокеты
    private ConcurrentLinkedDeque<Socket> clients = new ConcurrentLinkedDeque<>();

    // Исполнитель запросов
    private DBExecutor dbExecutor;

    // Статус активности
    private boolean activity = false;
    private ThreadGroup clientsSockets = new ThreadGroup("Client sockets");
    private Thread masterThread;

    // Кодовая фраза, для авторизации
    private String codePhrase;
    /**
     * Подкласс, обрабатывающий клиентское соединение.
     * Обработка в отдельном потоке
     */
    private class clientHandler implements Runnable {
        // Наше клиентское соединение
        private Socket client;

        // Разрешение для обработки соединения
        // Переключается после получения от клиента кодовой фразы
        private boolean accessGranted = false;

        /**
         * Инициализация и запуск
         *
         * @param client    Клиентский сокет
         */
        public clientHandler(Socket client) {
            this.client = client;
            clients.add(client);
            new Thread(clientsSockets, this).start();
        }

        /**
         * Обработка запроса клиента, запрос к БД
         */
        @Override
        public void run() {
            System.out.println("Found inbound request");
            work_proc:
            {
                try (
                        PrintWriter response = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"), true);
                        BufferedReader request = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"), 512)
                ) {
                    InputStream rawResponseStream = client.getInputStream();

                    // Считываем SQL-запрос, пока не найдём контрольное слово с разрывом соединения, либо null
                    StringBuilder userRequestBuilder = new StringBuilder();
                    String buffer;
                    // Ожидаем данные
                    System.out.println("Awaiting inbound query...");
                    while (activity && rawResponseStream.available() <= 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // Если прервали завершением, разъединяемся и покидаем поток
                            break work_proc;
                        }
                    }

                    System.out.println("Getting inbound query...");
                    // Данные поступили - собираем запрос, пока не получим завершающую строку
                    while (activity && (buffer = request.readLine()) != null) {
                        if (buffer.equals(BREAK_STRING)) break;
                        // Вначале обрабатываем первую строку, ищем кодовую фразу
                        // Если есть - собираем запрос
                        // Иначе скидываем соединение
                        // Кодовая фраза не входит в запрос и обрабатывается отдельно
                        if (accessGranted) {
                            userRequestBuilder.append(buffer).append("\n");
                        } else {
                            if (buffer.equals(codePhrase)) {
                                accessGranted = true;
                            } else {
                                System.out.println("Unauthorized inbound connection");
                                break work_proc;
                            }
                        }
                    }

                    // Ищем в запросе коды на останов и перезапуск
                    if (userRequestBuilder.toString().startsWith(STOP_STRING)) {
                        //TODO: здесь посылать сингал стоп
                    } else if (userRequestBuilder.toString().startsWith(RESTART_STRING)) {
                        //TODO: анилогично, рестарт
                    } else {
                        System.out.println("Executing inbound query: " + userRequestBuilder);
                        // Выполняем запрос, ждём результат
                        String result;
                        if (activity)
                            result = dbExecutor.executeQuery(userRequestBuilder.toString());
                        else
                            result = "";

                        if (activity) {
                            // Отправляем результат обратно
                            System.out.println("Sending results: " + result);
                            response.println(result);
                        }
                    }

                    // Отправка служебного сообщения об окончании передачи данных
                    response.println(BREAK_STRING);
                } catch (IOException e) {
                    System.out.println("Error: " + e);
                }
            }

            System.out.println("Disconnecting...");
            // Разрываем соединение.
            try {
                client.close();
            } catch (IOException closeE) {
                System.out.println("Warning: cannot close connection: " + closeE);
            }
            Iterator<Socket> iter = clients.iterator();

            // Удаляем сокет из списка
            while (iter.hasNext()) {
                Socket diff = iter.next();
                if (diff == client) {
                    iter.remove();
                    break;
                }
            }

            System.out.println("Done");
        }
    }
    /**
     * Инициализация сервисного сокета.
     * Запуск его потока.
     *
     * @param dbExecutor    Исполнитель SQL-запросов
     * @param port     Порт для обработки локальных соединений
     * @param codePhrase   Кодовая фраза для управления
     *
     * @throws IOException
     */
    public serviceSocket(DBExecutor dbExecutor, int port, String codePhrase) throws IOException {
        this.dbExecutor = dbExecutor;
        try {
            daemon = new ServerSocket(port, 10, InetAddress.getLocalHost());
        } catch (UnknownHostException ignore) {}

        this.codePhrase = codePhrase;

        masterThread = new Thread(this, "Master Thread");
        masterThread.start();
    }

    /**
     * Мастер-подпроцесс, управляет соединениями
     */
    @Override
    public void run() {
        System.out.println("Starting service");
        while (activity) {
            System.out.println("Wainting connection...");
            // Ожидаем соединение
            try (Socket serverConnection = daemon.accept()) {
                System.out.println("Accepted");
                // Обрабатываем соединение
                if (activity) new clientHandler(serverConnection);
            } catch (SocketTimeoutException ignore) {
                // Ждём снова
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
            System.out.println("Closed");
        }
        try {
            daemon.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    /**
     * Выключение сервиса
     */
    synchronized public void switchOff() {
        System.out.println("Switch off service");
        // Отключаем активность, вырубаем все клиентские соединения
        activity = false;
        for (Socket clientSocket : clients) {
            try {
                clientSocket.shutdownInput();
                clientSocket.shutdownOutput();
            } catch (IOException e) {
                System.out.println("Warning: " + e);
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("Warning: " + e);
                }
            }
        }

        // Вырубаем клиентские подпроцессы, если такие остались
        Thread[] clientThreads = new Thread[clientsSockets.activeCount()];
        clientsSockets.enumerate(clientThreads);
        for (Thread item : clientThreads) {
            item.interrupt();
        }

        // Закрываем сервисный сокет
        System.out.println("Closing service connector");
        try {
            daemon.close();
        } catch (IOException e) {
            System.out.println("Warning: " + e);
        }

        // Ожидаем завершения работы сервисного сокета
        try {
            masterThread.join();
        } catch (InterruptedException e) {
            System.exit(EXIT_INTERRUPTED);
        }
    }
}
