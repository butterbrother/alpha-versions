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

class RawDemo {
	public static void main(String args[]) {
		Gen<Integer> iOb = new Gen<Integer>(88);
		Gen<String> strOb = new Gen<String>("Обобщённый тест");
		Gen raw = new Gen(new Double(98.6));

		double d = (Double) raw.getob();
		System.out.println("значение: " + d);

		strOb = raw;
		raw = iOb;
	}
}
