class Elapsed {
	public static void main(String args[]) {
		long start, end;

		System.out.println("Время перебора от 0 до 100 000 000");

		start = System.currentTimeMillis();
		for (long i = 0; i < 100000000L; i++);
		end = System.currentTimeMillis();

		System.out.println("Время выполнения: " + (end - start));
	}
}
