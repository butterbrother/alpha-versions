package Collections;

import java.util.*;

class NewStyle {
	public static void main(String args[]) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("один");
		list.add("два");
		list.add("три");
		list.add("четыре");

		Iterator<String> itr = list.iterator();

		while (itr.hasNext()) {
			String str = itr.next();
			System.out.println(str + " имеет длину " + str.length() + " символов.");
		}
	}
}
