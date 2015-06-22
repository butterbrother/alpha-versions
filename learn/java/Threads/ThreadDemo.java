package Threads;

class NewThread implements Runnable {
	NewThread() {
		Thread t = new Thread(this, "Демонстрационный поток");
		System.out.println("Дочерний поток создан: " + t);
		t.start();
	}

	public void run() {
		try {
			for(int i = 5; i > 0; i--) {
				System.out.println("Дочерний поток: " + i);
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			System.out.println("Дочерний поток прерван");
		}
		System.out.println("Дочерний поток завершён");
	}
}

class ThreadDemo {
	public static void main(String args[]) {
		new NewThread();

		try {
			for (int i = 5; i > 0; i--) {
				System.out.println("Главный поток: " + i);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			System.out.println("Главный поток прерван.");
		}
		System.out.println("Главный поток завершён");
	}
}
