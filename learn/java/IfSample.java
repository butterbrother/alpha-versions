class IfSample {
	public static void main(String args[]) {
		int x = 10, y = 20;
		System.out.println("x = " + x + ", y = " + y);

		if (x < y) System.out.println("x меньше y");
		
		x *= 2;
		System.out.println("x = " + x + ", y = " + y);
		if (x == y) System.out.println("x теперь равна y");

		x *= 2;
		System.out.println("x = " + x + ", y = " + y);
		if (x > y) System.out.println("y теперь больше x");

		if (x == y) System.out.println("это не отображается");
	}
}
