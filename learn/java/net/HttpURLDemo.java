import java.net.*;
import java.io.*;
import java.util.*;
class HttpURLDemo {
	public static void main(String args[]) throws IOException {
		URL hp = new URL("http://www.google.com");
		HttpURLConnection hpCon = (HttpURLConnection) hp.openConnection();

		System.out.println("Request method: " + hpCon.getRequestMethod());
		System.out.println("Response code: " + hpCon.getResponseCode());
		System.out.println("ResponseMessage: " + hpCon.getResponseMessage());

		System.out.println("Headers:");
		for (String k : hpCon.getHeaderFields().keySet()) {
			System.out.println("Key: " + k + ", Value: " + hpCon.getHeaderFields().get(k));
		}
	}
}
