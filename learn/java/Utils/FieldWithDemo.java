package Utils;

import java.util.*;

class FieldWithDemo {
	public static void main(String args[]) {
		Formatter fmt = new Formatter();

		for (int i = 1; i <= 10; i++) {
			fmt.format("%4d %4d %4d%n", i, i*i, i*i*i);
		}
		System.out.print(fmt);
		fmt.close();
	}
}
