package Files;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

class ShowFile {
	public static void main(String args[]) {
		int i;
		FileInputStream fin;

		if (args.length != 1) {
			System.out.println("Использование: ShowFile файл");
			return;
		}

		try {
			fin = new FileInputStream(args[0]);
		} catch (FileNotFoundException exc) {
			System.out.println("Не могу открыть файл: " + exc);
			return;
		}

		try {
			do {
				i = fin.read();
				if (i != -1)
					System.out.print((char)i);
			} while (i != -1);
		} catch (IOException exc) {
			System.out.println("Ошибка чтения: " + exc);
		} finally {
			try {
				fin.close();
			} catch (IOException exc) {
				System.out.println("Ошибка закрытия файла: " + exc);
			}
		}
	}
}
