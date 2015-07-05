class Average {
	public static void main(String args[]) {
		int massive[] = {4,7,6,10,15,12,14,3};
		int middle = 0;

		for (int x = 0; x<massive.length; x++) {
			middle += massive[x];
			System.out.print("x[" + x + "]=" + massive[x] + ' ');
		}

		middle = (int)(middle / massive.length);

		System.out.println("\nСреднее значение - " + middle);
	}
}
