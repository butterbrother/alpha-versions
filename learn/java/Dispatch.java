class A {
	void callme() {
		System.out.println("Внутри метода callme класса A");
	}
}

class B extends A {
	void callme() {
		System.out.println("Внутри метода callme класса B");
	}
}

class C extends B {
	int b=4;
	void callme() {
		System.out.println("Внутри метода callme класса C");
		System.out.println(b);
	}
}

class Dispatch {
	public static void show(A ob) {
		ob.callme();
	}
	public static void main(String args[]) {
		A a = new A();
		B b = new B();
		C c = new C();

		show(a);
		show(b);
		show(c);
	}
}
