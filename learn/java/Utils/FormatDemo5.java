package Utils;

import java.util.*;

class FormatDemo5 {
	public static void main(String args[]) {
		try (Formatter fmt = new Formatter()) {

			fmt.format("% d %n", -100);
			fmt.format("% d %n", 100);
			fmt.format("% d %n", -200);
			fmt.format("% d %n", 200);
			fmt.format("%(d %n", -100);
			fmt.format("%(d %n", 100);
			fmt.format("%,.2f %n", 4356783497.34);

			System.out.print(fmt);
		} catch (Exception ignore) {}
	}
}
