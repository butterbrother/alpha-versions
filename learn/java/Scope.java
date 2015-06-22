class Scope {
	public static void main(String args[]) {
		int x = 10;

		if (x == 10) {
			int y = 20;

			{
				int z = 5;
				System.out.println("z = " + 5);
			}
			System.out.println("x и y: " + x + '\t' + y);
			x = y * 2;
		}

		System.out.println("x = " + x);
	}
}
