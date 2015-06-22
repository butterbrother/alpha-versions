package Threads;

class NewThread implements Runnable {
	String name;
	Thread t;

	NewThread(String threadname) {
		name = threadname;
		t = new Thread(this, name);
		System.out.println("Новый поток: " + t);
		t.start();
	}

	public void run() {
		try {
			for(int i = 5; i > 0; i--) {
				System.out.println(name + ": " + i);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			System.out.println(name + " прерван.");
		}
		System.out.println(name + " завершён.");
	}
}

class DemoJoin {
	public static void main(String args[]) {
		NewThread ob1 = new NewThread("Один");
		NewThread ob2 = new NewThread("Два");
		NewThread ob3 = new NewThread("Три");

		System.out.println("Поток " + ob1.t.getName() + " запущен: " + ob1.t.isAlive());
		System.out.println("Поток " + ob2.t.getName() + " запущен: " + ob2.t.isAlive());
		System.out.println("Поток " + ob3.t.getName() + " запущен: " + ob3.t.isAlive());

		try {
			System.out.println("Ожидание завершения потоков.");
			ob1.t.join();
			ob2.t.join();
			ob3.t.join();
		} catch (InterruptedException e) {
			System.out.println("Главный поток прерван");
		}

		System.out.println("Поток " + ob1.t.getName() + " запущен: " + ob1.t.isAlive());
		System.out.println("Поток " + ob2.t.getName() + " запущен: " + ob2.t.isAlive());
		System.out.println("Поток " + ob3.t.getName() + " запущен: " + ob3.t.isAlive());

		System.out.println("Главный поток завершён");
	}
}
