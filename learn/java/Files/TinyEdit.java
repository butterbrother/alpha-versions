package Files;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class TinyEdit {
	public static void main(String args[]) {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

		String strbuf[] = new String[100];
		System.out.println("Вводите текст");
		System.out.println("Ввод stop либо стоп прекратит ввод");

		try {
			for (int pos = 0; pos < 100; pos++) {
				strbuf[pos] = bf.readLine();
				if (strbuf[pos].indexOf("стоп") != -1)
					break;
				if (strbuf[pos].indexOf("stop") != -1)
					break;
			}
		} catch (IOException exc) {
			System.out.println("Ввод внезапно оборвался: " + exc);
		}

		System.out.println("\nВы вводили это:");
		for (int pos = 0; pos < 100; pos++) {
			if (strbuf[pos].indexOf("стоп") != -1)
				break;
			if (strbuf[pos].indexOf("stop") != -1)
				break;
			System.out.println(strbuf[pos]);
		}
	}
}
