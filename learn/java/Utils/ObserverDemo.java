package Utils;

import java.util.*;

class Watcher implements Observer {
	public void update(Observable obj, Object arg) {
		System.out.println("update() вызван, count равен " + ((Integer)arg).intValue());
	}
}

class BeignWatched extends Observable {
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

class ObserverDemo {
	public static void main(String args[]) {
		BeignWatched observed = new BeignWatched();
		Watcher observing = new Watcher();

		observed.addObserver(observing);
		observed.counter(10);
	}
}

