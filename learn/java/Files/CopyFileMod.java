package Files;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;

class CopyFileMod {
	public static void main(String args[]) {
		int i;

		if (args.length != 2) {
			System.out.println("Использование: Copy из в");
			return;
		}

		try (FileInputStream fin = new FileInputStream(args[0]);
		     FileOutputStream fout = new FileOutputStream(args[1]))
		{
			do {
				i = fin.read();
				if (i != -1) fout.write(i);
			} while(i != -1);
		} catch (IOException exc) {
			System.out.println("Ошибка I/O: " + exc);
		}
	}
}
