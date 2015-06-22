class A {
	int i = 0, j = 0;
	A(int a, int b) {
		i = a;
		j = b;
	}

	void show() {
		System.out.println("i и j: " + i + " " + j);
	}
}

class B extends A {
	int k = 0;
	B(int a, int b, int c) {
		super(a, b);
		k = c;
	}

	void show(String msg) {
		System.out.println(msg + k);
	}
}

class Override {
	public static void main(String args[]) {
		B subOb = new B(1, 2, 3);
		subOb.show();
		subOb.show("k: ");
	}
}
