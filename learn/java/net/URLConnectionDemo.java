import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.List;

class URLConnectDemo {
	public static void main(String args[]) throws Exception {
		int c;
		URL hp = new URL("http://www.internic.net");
		URLConnection hpCon = hp.openConnection();

		long d = hpCon.getDate();
		if (d == 0)
			System.out.println("No date information");
		else
			System.out.println("Date: " + new Date(d));

		System.out.println("Content type: " + hpCon.getContentType());

		d = hpCon.getExpiration();
		if (d ==0)
			System.out.println("No expiration date");
		else
			System.out.println("Expiration: " + new Date(d));

		long len = hpCon.getContentLengthLong();
		if (len == -1)
			System.out.println("Content length unvaliable");
		else
			System.out.println("Content length: " + len);

		System.out.println("Another headers:");
		for (Map.Entry<String, List<String>> head : hpCon.getHeaderFields().entrySet()) {
			System.out.println("\t"+ head.getKey());
			for (String field: head.getValue())
				System.out.println("\t\t" + field);
		}

		if (len != 0) {
			System.out.println("=== Page content: ===");
			InputStream input = hpCon.getInputStream();
			while (((c = input.read()) != -1)) {
				System.out.print((char) c);
			}
			input.close();
		} else {
			System.out.println("Page content unvaliabe");
		}
	}
}
