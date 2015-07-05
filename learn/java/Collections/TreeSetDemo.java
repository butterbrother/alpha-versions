package Collections;

import java.util.*;

class ThreeSetDemo {
	public static void main(String args[]) {
		TreeSet<Character> ts = new TreeSet<Character>();

		for (char c = 'A'; c <= 'F'; c++)
			ts.add(c);

		System.out.println(ts);
		System.out.println(ts.subSet('C', 'F'));
	}
}
