package Threads;

class One implements Runnable {
	Sta s;
	One(Sta s) {
		this.s = s;
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		try {
			while(true) {
				s.test(Thread.currentThread().getName());
				Thread.sleep(200);
			}
		} catch (InterruptedException ext) {
			System.out.println("ЩРД");
		}
	}
}

class Two implements Runnable {
	Sta s;
	Two(Sta s) {
		this.s = s;
		new Thread(this).start();
	}

	public void run() {
		try {
			while(true) {
				for (int i=0; i<10; i++) {
					s.point(Thread.currentThread().getName());
					Thread.sleep(200);
				}
				s.test(Thread.currentThread().getName());
				Thread.sleep(200);
			}
		} catch (InterruptedException ext) {
			System.out.println("ЩРД");
		}
	}
}
class Sta {
	synchronized void test(String name) {
		System.out.println("Запустил синхронизированный, " + name);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException exc) {
			System.out.println("ЩРД!");
		}
	}
	void point(String name) {
		System.out.println("Запустил несинхронизированный." + name);
	}
}

class SyncTest {
	public static void main(String args[]) {
		Sta s = new Sta();
		new One(s);
		new Two(s);
	}
}
