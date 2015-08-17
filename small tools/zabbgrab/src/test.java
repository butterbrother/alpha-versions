import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <EMPTY HEADER, PLEASE EDIT>
 * Created by user on 15.08.15.
 */
public class test {
    public static void main(String args[]) {
        try (CloseableHttpClient browser = HttpClients.createDefault()) {
            System.out.println("Try auth");

            List<NameValuePair> authForm = new LinkedList<>();
            authForm.add(new BasicNameValuePair("request", ""));
            authForm.add(new BasicNameValuePair("name", "OBobukh"));
            authForm.add(new BasicNameValuePair("password", "OBobukh"));
            authForm.add(new BasicNameValuePair("autologin", "1"));
            authForm.add(new BasicNameValuePair("enter", "Sign+in"));
            UrlEncodedFormEntity authEncoder = new UrlEncodedFormEntity(authForm);
            HttpPost authValue = new HttpPost("http://enswiki.vimpelcom.ru/zabbix/index.php");
            authValue.setEntity(authEncoder);
            authValue.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:38.0) Gecko/20100101 Firefox/38.0");
            authValue.addHeader("Referer", "http://enswiki.vimpelcom.ru/zabbix/index.php");

            try (CloseableHttpResponse response = browser.execute(authValue)) {
                System.out.println("Result: " + response.getStatusLine());
                Header[] allHeaders = response.getAllHeaders();
                System.out.println("Headers:");
                for (Header head : allHeaders) {
                    System.out.println(head);
                }
                System.out.println("Page:");
                HttpEntity result = response.getEntity();
                if (result != null)
                    System.out.println(EntityUtils.toString(result));
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}
