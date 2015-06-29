import java.io.*;
import java.net.*;

public class WhoisAutoClose {
	public static void main(String args[]) throws Exception {
		int c;
		try (Socket s = new Socket("whois.internic.net", 43); InputStream in = s.getInputStream(); OutputStream out = s.getOutputStream()) {
			for (String addr : args) {
				out.write((addr + '\n').getBytes());
				while ((c = in.read()) != -1)
					System.out.print((char) c);
			}
		}
	}
}
