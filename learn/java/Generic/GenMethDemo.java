package Generic;

class GenMethDemo {
	static <T, V extends T> boolean isIn(T x, V[] y) {
		for (int i=0; i<y.length; i++)
			if (x.equals(y[i])) return true;

		return false;
	}

	public static void main(String args[]) {
		Integer nums[] = { 1, 2, 3, 4, 5 };

		if (isIn(2, nums))
			System.out.println("2 содержится в nums");

		if (!isIn(7, nums))
			System.out.println("7 не содержится в nums");

		System.out.println();

		String strs[] = { "один", "два", "три", "четыре", "пять" };

		if(isIn("два", strs))
			System.out.println("два содержится в strs");

		if (!isIn("семь", strs))
			System.out.println("семь содержится в strs");

	}
}
