package Collections;

import java.util.*;

class OldStyle {
	public static void main(String args[]) {
		ArrayList list = new ArrayList();

		list.add("один");
		list.add("два");
		list.add("три");
		list.add("четыре");
		Iterator itr = list.iterator();

		while (itr.hasNext()) {
			String str = (String) itr.next();

			System.out.println(str + " имеет длину " + str.length() + " символов.");
		}
	}
}
