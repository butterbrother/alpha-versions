class NoChanges {
	public static void main(String args[]) throws java.io.IOException {
		int num[] = new int[10];
		for (int i=0; i<num.length; i++) num[i]=i+1;

		for (int x : num) {
			System.out.print(x + "-");
			x *= 10;
			System.out.print(x + " ");
		}

		System.out.println();

		for (int x : num)
			System.out.print(x + " ");
		System.out.println();
	}
}
