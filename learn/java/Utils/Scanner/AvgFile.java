package Utils.Scanner;

import java.util.*;
import java.io.*;

class AvgFile {
	public static void main(String args[]) throws IOException {
		int count = 0;
		double sum = 0.0;

		FileWriter fout = new FileWriter("test.txt");
		fout.write("2 3,4 5 6 7,4 9,1 10,5 готово");
		fout.close();

		FileReader fin = new FileReader("test.txt");
		Scanner src = new Scanner(fin);

		while(src.hasNext()) {
			if (src.hasNextDouble()) {
				double readed = src.nextDouble();
				sum += readed;
				System.out.printf("[%f]", readed);
				count ++;
			} else {
				String str = src.next();
				System.out.println("[" + str + "]");
				if (str.equals("готово")) break;
				else {
					System.err.println("ОШибка формата файла");
					return;
				}
			}
		}

		src.close();
		System.out.println("Среднее равно " + sum / count);
	}
}
