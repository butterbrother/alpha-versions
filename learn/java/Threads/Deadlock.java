package Threads;

class A {
	synchronized void foo(B b) {
		String name = Thread.currentThread().getName();

		System.out.println(name + " вошёл в A.foo");

		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			System.out.println("Щрд " + e);
		}
		System.out.println(name + " пытается вызвать B.last()");
		b.last();
	}

	synchronized void last() {
		System.out.println("внутри A.last");
	}
}

class B {
	synchronized void bar(A a) {
		String name = Thread.currentThread().getName();
		System.out.println(name + " вошёл в B.bar");

		try {
			Thread.sleep(1000);
		} catch(Exception e) {
			System.out.println("Щрд" + e);
			e.printStackTrace();
		}

		System.out.println(name + " пытается вызвать A.last()");
		a.last();
	}

	synchronized void last() {
		System.out.println("внутри A.last");
	}
}

class Deadlock implements Runnable {
	A a = new A();
	B b = new B();

	Deadlock() {
		Thread.currentThread().setName("MainThread");
		Thread t = new Thread(this, "RacingThread");
		t.start();

		a.foo(b);
		System.out.println("Назад в главный поток");
	}

	public void run() {
		b.bar(a);
		System.out.println("Назад в другой поток");
	}

	public static void main(String args[]) {
		new Deadlock();
	}
}
