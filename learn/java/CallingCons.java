class A {
	A() {
		System.out.println("Конструктор A");
	}
}

class B extends A {
	B() {
		System.out.println("Конструктор B");
	}
}

class C extends B {
	C() {
		System.out.println("Конструктор C");
	}
}

class CallingConst {
	public static void main(String args[]) {
		C c = new C();
	}
}
