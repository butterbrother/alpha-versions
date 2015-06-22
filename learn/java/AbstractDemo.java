abstract class A {
	public void demo() {
		System.out.println("Demmmmo!");
	}

	abstract public void callme();
}

class B extends A {
	public void callme() {
		System.out.println("Test");
	}
}

class AbstractDemo {
	public static void main(String args[]) {
		B b = new B();
		b.callme();
		b.demo();
	}
}
