package Utils;

import java.util.*;

class PrecisionDemo {
	public static void main(String args[]) {
		Formatter fmt = new Formatter();

		fmt.format("%.4f %n", 123.1234567);
		fmt.format("%16.2e %n", 123.1234567);
		fmt.format("%.15s",
				"Форматировать в Java теперь очень просто.");
		System.out.println(fmt);
		fmt.close();
	}
}
