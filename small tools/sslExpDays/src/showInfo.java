import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.Date;
import java.security.cert.X509Certificate;

/**
 */
public class showInfo {
    public static void main(String args[]) {
        // ������ ��������� ��������� ������
        String hostName = "127.0.0.1";
        int port = 443;
        boolean nextIsHostname = true;
        boolean nextIsPort = false;
        for (String arg : args) {
            if (nextIsHostname) {
                hostName = arg;
                nextIsHostname = false;
                nextIsPort = true;
                continue;
            }
            if (nextIsPort) {
                try {
                    port = Integer.parseInt(arg);
                } catch (NumberFormatException ignore) {
                    port = 443;
                }
                nextIsPort = false;
            }
        }

        // ��������� �����
        try {
            InetAddress remoteHost = InetAddress.getByName(hostName);
            // ���� ��������� ��-��������� - ������ ����
            long expireDate = 0;

            // �������� ���� ��������� �����������
            // ����� ������������� ������������, ������ http://www.nakov.com/blog/2009/07/16/disable-certificate-validation-in-java-ssl-connections/
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            // ������������� ����������� �������
            SSLSocketFactory sslFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
            SSLSocket sslSocket = (SSLSocket) sslFactory.createSocket(remoteHost, port);
                sslSocket.startHandshake();
                SSLSession session = sslSocket.getSession();
                Certificate serverCerts[] = session.getPeerCertificates();
                if (serverCerts.length > 0)
                    expireDate = ((X509Certificate) serverCerts[0]).getNotAfter().getTime();
            sslSocket.close();

            // �������� ������� ����� � ����
            long curentTime = new Date().getTime();

            // ����������, �������� ��. � ���
            System.out.println((expireDate - curentTime) / 1000 / 3600 / 24);
        } catch (UnknownHostException hostGetErr) {
            System.out.println(0);
            System.exit(1);
        } catch (IOException ioErr) {
            System.out.println(0);
            System.exit(2);
        } catch (NoSuchAlgorithmException ignore) {
            System.out.println(0);
            System.exit(3);
        } catch (KeyManagementException kmError) {
            System.out.println(0);
            System.exit(4);
        }

        System.exit(0);
    }
}
