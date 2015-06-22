package Collections;

import java.util.*;

class MyComp implements Comparator<String> {
	public int compare(String a, String b) {
		String aStr, bStr;
		aStr = a;
		bStr = b;

		return bStr.compareTo(aStr);
	}
}

class CompDemo {
	public static void main(String args[]) {
		TreeSet<String> ts = new TreeSet<String>(new MyComp());
		ts.add("C");
		ts.add("A");
		ts.add("B");
		ts.add("E");
		ts.add("F");
		ts.add("D");

		for (String element : ts)
			System.out.print(element + " ");
		System.out.println();
	}
}
