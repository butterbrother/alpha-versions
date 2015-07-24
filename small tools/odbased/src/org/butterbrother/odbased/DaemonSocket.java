package org.butterbrother.odbased;

import java.io.*;
import java.net.*;

/**
 * Сокет сервиса, обрабатывающего локальные подключения
 */
public class DaemonSocket implements Runnable, Closeable, AutoCloseable {
    private ServerSocket daemon;

    boolean activity = true;
    boolean isStopped = false;

    public DaemonSocket() throws IOException {
        try {
            daemon = new ServerSocket(10123, 10, InetAddress.getLocalHost());
        } catch (UnknownHostException ignore) {}
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("Started");
        while (activity) {
            System.out.println("Listening...");
            try (Socket serverConnection = daemon.accept()) {
                System.out.println("Accepted");
                try (BufferedReader clientChannel = new BufferedReader(new InputStreamReader(serverConnection.getInputStream(), "UTF-8"));
                PrintWriter serverChannel = new PrintWriter(new OutputStreamWriter(serverConnection.getOutputStream(), "UTF-8"), true)) {
                    while (serverConnection.getInputStream().available() <= 0) {
                        try {
                            System.out.println("Waiting...");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String buffer;
                    StringBuilder command = new StringBuilder();
                    while ((buffer = clientChannel.readLine()) != null) {
                        if (buffer.equalsIgnoreCase("//<exit>;")) break;
                        command.append(buffer).append('\n');
                    }
                    System.out.println("getted: " + command);    // Здесь будет передача запроса на исполнение
                    serverChannel.println("Hello");
                }
            } catch (SocketTimeoutException ignore) {
                // Ждём снова
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Closed");
        }
        try {
            daemon.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isStopped = true;
    }

    @Override
    public void close() throws IOException {
        activity = false;
        while (!isStopped) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                daemon.close();
            }
        }
    }

    /*
    private class ClientServerExchanger implements Runnable, AutoCloseable {
        private Socket client;
        public ClientServerExchanger(Socket client) {
            this.client = client;
            new Thread(this).start();
        }

        public void run() {

        }
    }
    */
}
