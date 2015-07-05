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
			for(int i = 15; i > 0; i--) {
				System.out.println(name + ": " + i);
				Thread.sleep(200);
			}
		} catch (InterruptedException e) {
			System.out.println(name + " прерван.");
		}
		System.out.println(name + " завершён");
	}
}

class SuspendResume {
	public static void main(String args[]) {
		NewThread ob1 = new NewThread("Один");
		NewThread ob2 = new NewThread("Два");

		try {
			Thread.sleep(1000);
			ob1.t.suspend();
			System.out.println("Приостановка потока Один");
			Thread.sleep(1000);
			ob1.t.resume();
			System.out.println("Возобновление потока Один");
			ob2.t.suspend();
			System.out.println("Приостановка потока Два");
			Thread.sleep(1000);
			ob2.t.resume();
			System.out.println("Возобновление потока Два");
		} catch (InterruptedException e) {
			System.out.println("Главный поток прерван");
		}

		try {
			System.out.println("Ожидание завершения потоков");
			ob1.t.join();
			ob2.t.join();
		} catch(InterruptedException e) {
			System.out.println("Главный поток прерван");
		}
		System.out.println("Главный поток завершён");
	}
}
