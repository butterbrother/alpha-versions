package Collections;

import java.util.*;

class LinkedHashSetDemo {
	public static void main(String args[]) {
		LinkedHashSet<Character> lhs = new LinkedHashSet<Character>(6);

		for (char c = 'A'; c <= 'F'; c++)
			lhs.add(c);

		System.out.println(lhs);
	}
}
