package Generic;

class Gen<T> {
	T ob;

	Gen(T o) {
		ob = o;
	}

	T getob() {
		return ob;
	}
}

class Gen2 extends Gen<String> {
	Gen2(String o) {
		super(o);
	}

	String getob() {
		System.out.println("Вызван String getob(): ");
		return ob;
	}
}

class BridgeDemo {
	public static void main(String args[]) {
		Gen2 strOb2 = new Gen2("Обобщённый тест");

		System.out.println(strOb2.getob());
	}
}
