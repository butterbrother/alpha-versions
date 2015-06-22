package Utils.Scanner;

import java.util.*;
import java.io.*;

class SetDelimiters {
	public static void main(String args[]) {
		int count = 0;
		double sum = 0.0;

		try (FileWriter fout = new FileWriter("test.txt")) {
			fout.append("2; 3,4; 5; 6; 7,4; 9,1; 10,5; готово");
		} catch (IOException exc) {
			exc.printStackTrace();
			System.exit(1);
		}

		try (Scanner src = new Scanner(new FileReader("test.txt"))) {
			src.useDelimiter("; *");

			while (src.hasNext()) {
				if (src.hasNextDouble()) {
					sum += src.nextDouble();
					count ++;
				} else {
					String str = src.next();
					if (str.equals("готово"))
						break;
					else {
						System.err.println("Ошибка формата файла.");
						return;
					}
				}
			}
		} catch (IOException exc) {
			exc.printStackTrace();
			System.exit(1);
		}

		System.out.println("Среднее равно " + sum / count);
	}
}
