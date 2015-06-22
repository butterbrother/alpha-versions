class A {
	int i;
}

class B extends A {
	int i;

	B(int a, int b) {
		super.i = a;
		i = b;
	}

	void show() {
		System.out.println("i в суперклассе: " + super.i);
		System.out.println("i в подклассе: " + i);
	}
}

class UseSuper {
	public static void main(String args[]) {
		java.util.Scanner scan = new java.util.Scanner(System.in);
		System.out.print("Введите одно число: ");
		int one = scan.nextInt();
		System.out.print("Введите другое число: ");
		int two = scan.nextInt();

		B subOb = new B(one, two);
		subOb.show();
	}
}
