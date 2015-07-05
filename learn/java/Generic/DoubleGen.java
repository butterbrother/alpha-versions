package Generic;

class TwoGen<T, V> {
	T ob1;
	V ob2;

	TwoGen(T o1, V o2) {
		ob1 = o1;
		ob2 = o2;
	}

	void showTypes() {
		System.out.println("Тип T: " + ob1.getClass().getName());
		System.out.println("Тип V: " + ob2.getClass().getName());
	}

	T getob1() {
		return ob1;
	}

	V getob2() {
		return ob2;
	}
}

class DoubleGen {
	public static void main(String args[]) {
		TwoGen<Integer, String> tgObj = new TwoGen<Integer, String>(88, "Обобщения");

		tgObj.showTypes();

		int v = tgObj.getob1();
		System.out.println("Значение: " + v);

		String str = tgObj.getob2();
		System.out.println("Значение: " + v);
	}
}
