package Collections;

import java.util.*;

class ArrayDemo {
	public static void main(String args[]) {
		int array[] = new int[10];
		for (int i = 0; i < 10; i++)
			array[i] = -3 * i;

		System.out.print("Исходное содержимое: ");
		display(array);

		Arrays.sort(array);
		System.out.print("Отсортированный массив: ");
		display(array);

		Arrays.fill(array, 2, 6, -1);
		System.out.print("После fill(): ");
		display(array);

		Arrays.sort(array);
		System.out.print("После повторной сортировки: ");
		display(array);

		System.out.print("Значение -9 находится в позиции ");
		int index = Arrays.binarySearch(array, -9);
		System.out.println(index);
	}

	static void display(int array[]) {
		for (int i: array)
			System.out.print(i + " ");
		System.out.println();
	}
}
