package Files;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class BRRead {
	public static void main(String args[]) {
		char c;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Вводите символы, 'q' - для выхода.");

		try {

			do {
				c = (char) br.read();
				System.out.println(c);
			} while (c != 'q' );
		} catch (IOException exc) {
			System.out.println("XX");
		}
	}
}
