class Nested {
	public static void main(String args[]) throws java.io.IOException {
		int count=10;

		for (int i=0; i<count; i++) {
			for (int j=i; j<count; j++)
				System.out.print(".");
			System.out.println();
		}
	}
}
