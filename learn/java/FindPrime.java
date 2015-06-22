class FindPrime {
	public static void main(String args[]) {
		for (int num=1; num<=10; num++) {
			boolean isPrime;

			if (num < 2) isPrime = false;
			else isPrime = true;

			for (int i = 2; i <= num/i; i++) {
				if ((num % i) == 0) {
					isPrime = false;
					break;
				}
			}
			if (isPrime) System.out.println("Число " + num + " простое");
			else System.out.println("Число " + num + " не простое");
		}
	}
}
