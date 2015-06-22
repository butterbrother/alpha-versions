class StaticDemo {
	static int a = 42;
	static int b = 99;

	static void callme() {
		System.out.println("a = " + a);
	}

	static {
		System.out.println("Инициализация статического блока");
	}
}

class StaticByName {
	public static void main(String args[]) throws java.io.IOException {
		StaticDemo.callme();
		System.out.println("b = " + StaticDemo.b);
	}
}
