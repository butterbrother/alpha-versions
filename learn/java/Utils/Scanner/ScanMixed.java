package Utils.Scanner;

import java.util.*;
import java.io.*;

class ScanMixed {
	public static void main(String args[])
		throws IOException {
		int i;
		double d;
		boolean b;
		String str;

		FileWriter fout = new FileWriter("test.txt");
		fout.write("Тестирование Scanner 10 12,2 один true два false");
		fout.close();

		try (Scanner src = new Scanner(new FileReader("test.txt"))) {
			while(src.hasNext()) {
				if (src.hasNextInt()) {
					i = src.nextInt();
					System.out.println("int: " + i);
				} else if (src.hasNextDouble()) {
					d = src.nextDouble();
					System.out.println("double: " + d);
				} else if (src.hasNextBoolean()) {
					b = src.nextBoolean();
					System.out.println("boolean: " + b);
				} else {
					str = src.next();
					System.out.println("String: " + str);
				}
			}
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}
}

