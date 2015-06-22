package Utils;

import java.util.*;

class AvgNums {
	public static void main(String args[]) {
		Scanner conin = new Scanner(System.in);

		int count = 0;
		double sum = 0.0;

		System.out.println("Введите числа для подсчёта среднего.");

		while (conin.hasNext()) {
			if (conin.hasNextDouble()) {
				sum += conin.nextDouble();
				count++;
			} else {
				String str = conin.next();
				if (str.equals("exit")) break;
				else {
					System.out.println("Ошибка формата данных.");
					return;
				}
			}
		}
		conin.close();
		System.out.println("Среднее значение равно " + (sum / count));
	}
}
