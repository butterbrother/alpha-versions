package Collections;

import java.util.*;

class ArrayListDemo {
	public static void main(String args[]) {
		ArrayList<String> al = new ArrayList<String>();

		System.out.println("Начальный размер al: " + al.size());

		al.add("C");
		al.add("A");
		al.add("E");
		al.add("B");
		al.add("D");
		al.add("F");
		al.add(1, "A2");
		System.out.println("Размер al после вставок: " + al.size());

		System.out.println("Содержимое al: " + al);

		al.remove("F");
		al.remove(2);

		System.out.println("Размер al после удалений: " + al.size());
		System.out.println("Содержимое al: " + al);
	}
}
