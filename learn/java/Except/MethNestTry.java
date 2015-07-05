package Except;

class Tested {
	public String toString() {
		return "Hello!";
	}
}

class MethNestTry {
	static void nesttry(int a) {
		try {
			if (a==1)
				a = a/(a-a);
			if (a==2) {
				int c[] = { 1 };
				c[42] = 99;
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Индекс за пределами массива: " + e);
		}
	}
	public static void main(String args[]) {
		Tested ob = new Tested();
		System.out.println(ob);

		try {
			int a = args.length;

			int b = 42 / a;
			System.out.println("a = " + a);
			nesttry(a);
		} catch(ArithmeticException e) {
			System.out.println("Деление на 0: " + e);
		}
	}
}
