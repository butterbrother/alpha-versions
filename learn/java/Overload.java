class OverloadDemo {
	void test() {
		System.out.println("Параметры отсутствуют");
	}

	void test(int a) {
		System.out.println("a: " + a);
	}

	void test(int a, int b) {
		System.out.println("a и b: " + a + " " + b);
	}

	double test(double a) {
		System.out.println("double a: " + a);
		return a*a;
	}
	
}

class Overload {
	public static void main(String args[]) {
		OverloadDemo ob = new OverloadDemo();

		ob.test();
		ob.test(10);
		ob.test(10, 20);
		double result = ob.test(123.25);
		System.out.println("Результат ob.test(123.25): " + result);
	}
}

