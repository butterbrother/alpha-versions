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

class Gen2<T> extends Gen<T> {
	Gen2(T o) {
		super(o);
	}
}

class HierDemo3 {
	public static void main(String argsp[]) {
		Gen<Integer> iOb = new Gen<Integer>(88);
		Gen2<Integer> iOb2 = new Gen2<Integer>(99);
		Gen2<String> strOb2 = new Gen2<String>("Обобщённый текст");

		if (iOb2 instanceof Gen2<?>)
			System.out.println("iOb2 является экземпляром Gen2");

		if (iOb2 instanceof Gen<?>)
			System.out.println("iOb2 является экземпляром Gen");

		System.out.println();

		if (strOb2 instanceof Gen2<?>)
			System.out.println("strOb2 является экземпляром Gen2");

		if (strOb2 instanceof Gen<?>)
			System.out.println("strOb2 является экземпляром Gen");

		System.out.println();

		if (iOb instanceof Gen2<?>)
			System.out.println("iOb является экземпляром Gen2");

		if (iOb instanceof Gen<?>)
			System.out.println("iOb является экземпляром Gen");

		/*
		 * if (iOb2 instanceof Gen2<Integer>
		 * так нельзя
		 */
	}
}
