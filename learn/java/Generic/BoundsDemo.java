package Generic;

class Stats<T extends Number> {
	T[] nums;

	Stats(T[] o) {
		nums = o;
	}

	double average() {
		double sum =0.0;

		for (int i=0; i < nums.length; i++)
			sum += nums[i].doubleValue();

		return nums.length > 0 ? sum / nums.length : 0;
	}
}

class BoundsDemo {
	public static void main(String args[]) {
		Integer inums[] = { 1, 2, 3, 4, 5 };
		Stats<Integer> iob = new Stats<Integer>(inums);
		double v = iob.average();
		System.out.println("Среднее значение iob равно " + v);

		Double dnums[] = { 1.1, 1.2, 1.3, 1.4, 1.5 };
		Stats<Double> dob = new Stats<Double>(dnums);
		double w = dob.average();
		System.out.println("Среднее значение dob равно " + w);
	}
}
