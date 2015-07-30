package org.butterbrother;

import oracle.jdbc.pool.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ��������� �������� ����������� � �� Oracle(c)
 */
public class jtnsping {
    public static void main(String args[]) {
        // ���� tns-��� �� 1�� ���������
        if (args.length == 0) {
            System.err.println("Usage: java -Doracle.net.tns_admin=path_contains_tnsnames.ora jtnsping.jar TNS_NAME");
            System.exit(1);
        }

        // ��������� URL
        String URL = "jdbc:oracle:thin:@" + args[0];

        // ������������
        Connection sqlCon = null;
        try {
            OracleDataSource ods = new OracleDataSource();
            ods.setURL(URL);
            sqlCon = ods.getConnection();
        } catch (SQLException e) {
            // ��� ��������� ������ - ����������� � ���������� ������
            switch (e.getErrorCode()) {
                // �� �������� ����
                case 12514 :
                    System.out.println("Listener is UP but database is DOWN");
                    break;
                // �� �������� listener
                case 17002 :
                    System.out.println("Listener is DOWN");
                    break;
                // User-logon denied, ���� ����
                case 1017 :
                    System.out.println("Listener is UP and database is UP");
                    break;
                default:
                    System.out.println(e.getMessage());
            }
        } finally {
            // �����������
            if (sqlCon != null)
                try {
                    sqlCon.close();
                } catch (SQLException ignore) {}
        }
    }
}
