class BoolTest {
	public static void main(String args[]) {
		boolean b;

		b = false;
		System.out.println("b = " + b);

		b = true;
		System.out.println("b = " + b);

		if (b) System.out.println("Вы это видите");
		b = false;
		if (b) System.out.println("А это нет");

		System.out.println("10 > 9 равно " + ( 10 > 9 ));
	}
}
