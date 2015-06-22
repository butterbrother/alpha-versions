package Collections;

import java.util.*;

class ForEachDemo {
	public static void main(String args[]) {
		ArrayList<Integer> vals = new ArrayList<Integer>();

		for (int i=1; i<=5; i++)
			vals.add(i);

		System.out.print("Исходное содержимое vals: ");
		for(int v : vals)
			System.out.print(v + " ");
		System.out.println();

		int sum = 0;
		for (int v : vals)
			sum += v;

		System.out.println("Сумма значений: " + sum);
	}
}
