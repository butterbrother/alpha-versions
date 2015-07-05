package Collections;

import java.util.*;

class ArrayDequeDemo {
	public static void main(String args[]) {
		ArrayDeque<Character> adq = new ArrayDeque<Character>();

		for (char c = 'A'; c <= 'F'; c++)
			adq.push(c);

		System.out.print("Выталкиваем из стека: ");

		while(adq.peek() != null)
			System.out.print(adq.pop() + " ");

		System.out.println();
	}
}
