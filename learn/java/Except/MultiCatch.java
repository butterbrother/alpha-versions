package Except;

class MultiCatch {
	public static void main(String args[]) {
		int a=10, b=0;
		int vals[] = { 1, 2, 3 };

		try {
			switch (args.length) {
				case 1: int result = a/b;
					break;
				default: vals[10] = 19;
			}
		} catch (ArithmeticException | ArrayIndexOutOfBoundsException e) {
			System.out.println("Обрабатывается исключение: " + e);
		}
	}
}
