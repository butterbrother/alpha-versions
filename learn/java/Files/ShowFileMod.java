package Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;

class ShowFileMod {
	public static void main(String args[]) {
		int i;
		FileInputStream fin = null;

		if (args.length != 1) {
			System.out.println("Использование: ShowFile Файл");
			return;
		}

		try {
			File fl = new File(args[0]);
			if (! fl.exists() || ! fl.canRead()) {
				System.out.println("Файл не существует или не может быть прочитан");
				return;
			}

			fin = new FileInputStream(fl);

			do {
				i = fin.read();
				if (i != -1)
					System.out.print((char) i);
			} while (i != -1);
		} catch (FileNotFoundException exc) {
			System.out.println("Файл не существует: " + exc);
		} catch (IOException exc) {
			System.out.println("Ошибка I/O: " + exc);
		} finally {
			if (fin != null)
				try {
					fin.close();
				} catch (IOException exc) {
					System.out.println("Ошибка закрытия файла");
				}
		}
	}
}
