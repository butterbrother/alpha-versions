package Files;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class BRReadLine {
	public static void main(String args[]) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Вводите что-нибудь");
		System.out.println("stop/стоп - прекращает ввод");

		String str = "";

		try {
			do {
				str = br.readLine();
				System.out.println(str);
			} while ((str.indexOf("стоп") == -1) && (str.indexOf("stop") == -1));
		} catch (IOException exc) {
			System.out.println("Щрд! " + exc);
		}
	}
}
