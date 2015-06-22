class IFTest3 {
	private static void push(IntStack ob, int start, int end) {
		for (int i=start; i<end; i++)
			ob.push(i);
	}
	private static void pop(IntStack ob, int start, int end) {
		for (int i=start; i<end; i++)
			System.out.println(ob.pop());
	}
	public static void main(String args[]) {
		IntStack ds = new DynStack(5);
		IntStack fs = new FixedStack(8);

		push(ds, 0, 12);
		push(fs, 0, 8);

		System.out.println("Значения в динамическом стеке:");
		pop(ds, 0, 12);

		System.out.println("Значения в фиксированном стеке:");
		pop(fs, 0, 8);
	}
}
