package Utils;

import java.util.*;

class Watcher1 implements Observer {
	public void update(Observable obj, Object arg) {
		System.out.println("update() вызван, count равен " + ((Integer)arg).intValue());
	}
}

class Watcher2 implements Observer {
	public void update(Observable obj, Object arg) {
		if (((Integer)arg).intValue() == 0)
			System.out.println("Готово" + '\7');
	}
}

class BeingWatched extends Observable {
	void counter(int period) {
		for ( ; period >= 0; period--) {
			setChanged();
			notifyObservers(new Integer(period));

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("Ожидание прервано");
			}
		}
	}
}

class TwoObservers {
	public static void main(String args[]) {
		BeingWatched observed = new BeingWatched();
		Watcher1 observing1 = new Watcher1();
		Watcher2 observing2 = new Watcher2();

		observed.addObserver(observing1);
		observed.addObserver(observing2);
		observed.counter(10);
	}
}
