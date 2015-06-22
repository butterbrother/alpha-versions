package Collections;

import java.util.*;

class LinkedListDemo {
	public static void main(String args[]) {
		LinkedList<String> ll = new LinkedList<String>();

		ll.add("F");
		ll.add("B");
		ll.add("D");
		ll.add("E");
		ll.add("C");
		ll.addLast("Z");
		ll.addFirst("A");

		ll.add(1, "A2");

		System.out.println("Исходное содержимое ll: " + ll);

		ll.remove("F");
		ll.remove(2);
		System.out.println("Содержимое ll после удаления: " + ll);

		ll.removeFirst();
		ll.removeLast();

		System.out.println("ll после удаления первого и последнего: " + ll);

		String val = ll.get(2);
		ll.set(2, val + " Изменён");

		System.out.println("ll после изменения: " + ll);
	}
}
