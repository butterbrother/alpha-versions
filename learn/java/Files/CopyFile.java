package Files;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

class CopyFile {
	public static boolean checkfile(String filename) throws FileNotFoundException, IOException {
		File fl = new File(filename);
		if (! fl.exists()) {
			System.out.println("Файл " + filename + " не существует");
			return false;
		} 
		if (! fl.canRead()) {
			System.out.println("Файл " + filename + " не может быть прочитан");
			return false;
		}
		return true;
	}
	public static void main(String args[]) {
		int i;
		FileInputStream fin = null;
		FileOutputStream fout = null;

		if (args.length != 2) {
			System.out.println("Использование: CopyFile Файл_1 Файл_2");
			return;
		}

		try {
			if (! checkfile(args[0]))
				return;
			checkfile(args[1]);

			fin = new FileInputStream(args[0]);
			fout = new FileOutputStream(args[1]);

			do {
				i = fin.read();
				if (i != -1)
					fout.write(i);
			} while (i != -1);

			fin.close();
			fout.close();
		} catch (FileNotFoundException exc) {
			System.out.println("Файл не найден: " + exc);
		} catch (IOException exc) {
			System.out.println("Ошибка I/O: " + exc);
		} finally {
			try {
				if (fin != null)
					fin.close();
			} catch (IOException exc) {
				System.out.println("Файл-источник не может быть закрыт: " + exc);
			}
			try {
				if (fout != null)
					fout.close();
			} catch (IOException exc) {
				System.out.println("Файл-приёмник не может быть закрыт: " + exc);
			}
		}
	}
}
