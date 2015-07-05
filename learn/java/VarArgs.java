class VarArgs {
	static int copy[];

	static void vatest(int ... v) {
		System.out.println("Число элементов v: " + v.length);

		copy = v;

		System.out.println("Список элементов v:");
		for (int x : copy)
			System.out.print(x + " ");

		System.out.println();
	}

	public static void main(String args[]) throws java.io.IOException {
		vatest();
		vatest(5, 6, 7);
		vatest(30, 40, 50, 60, 70);
	}
}
