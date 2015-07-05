package Collections;

import java.io.*;
import java.util.*;

class Phonebook {
	public static void main(String args[]) throws IOException {
		Properties ht = new Properties();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String name, number;
		FileInputStream fin = null;
		boolean changed = false;

		try {
			fin = new FileInputStream("phonebook.dat");
		} catch (FileNotFoundException ignore) {}

		try {
			if (fin != null) {
				ht.load(fin);
				fin.close();
			}
		} catch (IOException e) {
			System.out.println("Ошибка чтения файла.");
			e.printStackTrace();
		}

		do {
			System.out.println("Введите имя (\"выход\" для останова): ");
			name = br.readLine();
			if (name.equals("выход")) continue;
			System.out.println("Введите номер: ");
			number = br.readLine();
			ht.put(name, number);
			changed = true;
		} while (!name.equals("выход"));

		if (changed) {
			FileOutputStream fout = new FileOutputStream("phonebook.dat");
			ht.store(fout, "Телефонная книга");
			fout.close();
		}
		
		do {
			System.out.println("Введите имя для поиска (\"выход\" для останова): ");
			name = br.readLine();
			if (name.equals("выход")) continue;
			number = (String) ht.get(name);
			System.out.println(number);
		} while (!name.equals("выход"));
	}
}
