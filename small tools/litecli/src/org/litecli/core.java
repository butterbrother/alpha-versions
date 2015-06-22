package org.litecli;

import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * ���������� CLI-������ ��� JDBC-��������
 */
public class core {

    /**
     * ������
     */
    public static void main(String args[]) {
        /*
        ������ ������� ���������
         */
        // ��������:
        boolean nextConfig = false; // ��������� �������� ����� ��������� ����� ������������
        boolean nextSQL = false;    // ��������� �������� ����� ��������� ����� � ���������
        boolean nextOut = false;    // ��������� �������� ����� �������� ��������� �����
        boolean nextDelim = false;  // ��������� �������� ����� ��������� ����������� ��������

        boolean showHeaders = false; // ���������� ��������� �������� ����������� ��������
        String configFile = "litecli.properties"; // ��� ����� ������������
        String sqlFile = "";    // ��� ����� � ���������
        String outFile = "";    // ��� ����� � �������
        String delimiter = ";"; // ����������� ��������

        // ��������������� ������ ���������
        for (String item : args)
            switch (item) {
                case "-c":
                case "--config":
                    nextConfig = true;
                    break;
                case "-s":
                case "--sql":
                    nextSQL = true;
                    break;
                case "-o":
                case "--output":
                    nextOut = true;
                    break;
                case "-d":
                case "--delimiter":
                    nextDelim = true;
                    break;
                case "-e":
                case "--headers":
                    showHeaders = true;
                    break;
                default:
                    if (nextConfig) {
                        configFile = item;
                        nextConfig = false;
                    } else if (nextSQL) {
                        sqlFile = item;
                        nextSQL = false;
                    } else if (nextOut) {
                        outFile = item;
                        nextOut = false;
                    } else if (nextDelim) {
                        delimiter = item;
                        nextDelim = false;
                    } else {
                        System.err.println("Unable to parse parameter: " + item);
                        System.exit(1);
                    }
            }

        // ������ ������
        Properties params = new Properties();
        try (FileInputStream inputFile = new FileInputStream(new File(configFile))) {
            params.load(inputFile);
        } catch (IOException err) {
            System.err.println("Unable to read config file:");
            err.printStackTrace();
            System.exit(2);
        }

        // ������������ �������, ������� �� ����������
        connector(params, sqlReader(sqlFile), delimiter, outFile, showHeaders);

    }

    /**
     * ������ ����� � ���������. ���� ������������� ����/pipe
     * @param sqlFileName   ��� ����� � ���������. ���� ���� - ����������� ������������� ����
     * @return  ������ �������� � ������� LinkedList
     */
    private static LinkedList<String> sqlReader(String sqlFileName) {
        // �������������� ������ ��������
        StringBuilder RAWrequests = new StringBuilder();

        if (!sqlFileName.equals("")) {
            /*
            ��������������� ����, �� �����.
            ��������� ���� � ������
             */
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(sqlFileName))))) {
                String buffer;
                while ((buffer = reader.readLine()) != null) {
                    RAWrequests.append(buffer);
                }
            } catch (FileNotFoundException err) {
                System.err.println("SQL file not found: " + err);
                err.printStackTrace();
                System.exit(2);
            } catch (IOException err) {
                System.err.println("Unable to open or read file: " + err);
                err.printStackTrace();
                System.exit(2);
            }
        } else {
            /*
            ������������� ���� ���� ���� �� pipe
             */
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                // ���� �� pipe (����� ����), �� ������� �����������
                boolean showPrompt = (System.in.available() == 0);

                String buffer;
                if (showPrompt) System.out.print("Enter SQL query (Ctrl+D and Enter or \"EXIT;\" and Enter to end):\n>> ");
                while ((buffer = reader.readLine()) != null) {
                    if (showPrompt && buffer.equalsIgnoreCase("exit;")) break;
                    RAWrequests.append(buffer);
                    if (showPrompt) System.out.print(">> ");
                }
            } catch (IOException err) {
                System.err.println("I/O error: " + err);
                err.printStackTrace();
                System.exit(2);
            }
        }

        /*
        ����� ��������� �� ������� (�������� ������ � �������)
        */
        StringTokenizer separator = new StringTokenizer(RAWrequests.toString(), ";");
        LinkedList<String> batchCommands = new LinkedList<>();
        while (separator.hasMoreElements())
            batchCommands.add(separator.nextToken());

        return batchCommands;
    }

    /**
     * �����������, ������ �����������, � �.�.
     * @param connectionProperties  ��������� ���������� �� �����
     * @param requests              �������
     * @param delimiter             ����������� �������
     * @param outputFile            �������� ����
     */
    private static void connector(Properties connectionProperties, LinkedList<String> requests, String delimiter, String outputFile, boolean showHeaders) {
        // ��������� ������� ����������
        // �������
        String driverName = connectionProperties.getProperty("driver", "");
        if (! driverName.isEmpty()) {
            try {
                DriverManager.registerDriver((Driver) Class.forName(driverName).newInstance());
            } catch (ClassNotFoundException err) {
                System.err.println("Unable to load driver (Not found): " + err);
                err.printStackTrace();
                System.exit(1);
            } catch (InstantiationException err) {
                System.err.println("Unable to load driver (Driver load error): " + err);
                err.printStackTrace();
                System.exit(1);
            } catch (IllegalAccessException err) {
                System.err.println("Unable to load driver (Access error): " + err);
                err.printStackTrace();
                System.exit(1);
            } catch (SQLException err) {
                System.err.println("Unable to load JDBC driver: " + err);
                err.printStackTrace();
                System.exit(1);
            }
        }
        // ������ �����������
        String url = connectionProperties.getProperty("url", "");
        if (url.isEmpty()) {
            System.err.println("Connection url not set");
            System.exit(1);
        }
        String login = connectionProperties.getProperty("login", "");
        String password = connectionProperties.getProperty("password", "");

        /*
        ����������� � ������ ��������
         */
        try (Connection sqlConnect = DriverManager.getConnection(url, login, password);
        Statement statement = sqlConnect.createStatement()) {
            if (! outputFile.isEmpty()) {
                // ������ ����������� � ���� (�������� ���� ������)
                try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFile))))) {
                    for (String query : requests) {
                        out.append(executor(statement, delimiter, query, showHeaders)).append('\n');
                    }
                } catch (IOException err) {
                    System.err.println("Error writing to output file: " + err);
                    err.printStackTrace();
                }
            } else {
                // ������ ����������� � ����������� ����� (�������� ���� �� ������)
                for (String query : requests) {
                    System.out.println(executor(statement, delimiter, query, showHeaders));
                }
            }
        } catch (SQLException err) {
            System.err.println("SQL error: " + err);
            err.printStackTrace();
            System.exit(3);
        }
    }

    /**
     * ���������������� ���������� ��������
     * @param statement ��������� SQL
     * @param delimiter �����������
     * @param query     ������
     * @return          ��������� �������
     * @throws SQLException
     */
    private static String executor(Statement statement, String delimiter, String query, boolean showHeaders) throws SQLException {
        StringBuilder results = new StringBuilder();

        // ��������� ������, �������� ��������� � ����������
        ResultSet queryResults = statement.executeQuery(query);
        ResultSetMetaData queryMetaData = queryResults.getMetaData();

        // ���������� ��������� ������� �������, ���� ������
        if (showHeaders) {
            if (queryMetaData.getColumnCount() != 0)
                // ���������� ���������
                for (int row = 1; row <= queryMetaData.getColumnCount(); row ++) {
                    // � ����� ��� �������
                    results.append(queryMetaData.getColumnName(row));
                    // ������ �����������, ���� ������ ��� �� ��������� �������
                    if (row != queryMetaData.getColumnCount())
                        results.append(delimiter);
                }
            results.append('\n');
        }

        // ���������� ������
        while (queryResults.next()) {
            if (queryMetaData.getColumnCount() != 0)
                // � �������
                for (int row = 1; row <= queryMetaData.getColumnCount(); row++) {
                    // � ���������� ��
                    results.append(queryResults.getString(row));
                    // ������ ����������� ���� ������ ��� �� ��������� �������
                    if (row != queryMetaData.getColumnCount())
                        results.append(delimiter);
                }
            results.append('\n');   // ��������� ������� ����� ������ ������ ������
        }
        return results.toString();
    }
}
