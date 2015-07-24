package org.butterbrother.odbased;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Клиентский сокет
 */
public class ClientSocket implements Cloneable, AutoCloseable {
    private Socket client;
    private InputStream serverData;
    private BufferedReader serverResponse;
    private PrintWriter clientRequests;

    public ClientSocket() throws IOException {
        client = new Socket(InetAddress.getLocalHost(), 10123);
        serverData = client.getInputStream();
        serverResponse = new BufferedReader(new InputStreamReader(serverData, "UTF-8"));
        clientRequests = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"), true);
    }

    public boolean haveResponse() throws IOException {
        return serverData.available() > 0;
    }

    public void sendRequest(String message) throws IOException {
        clientRequests.println(message);
        clientRequests.println("//<exit>;");
    }

    public String getResponse() throws IOException {
        if (haveResponse()) {
            String buffer;
            StringBuilder response = new StringBuilder();
            while ((buffer = serverResponse.readLine()) != null) {
                if (buffer.equalsIgnoreCase("//<exit>;")) break;
                response.append(buffer).append('\n');
            }
            return response.toString();
        }

        return "";
    }

    @Override
    public void close() throws IOException {
        clientRequests.close();
        serverResponse.close();
        client.close();
    }
}
