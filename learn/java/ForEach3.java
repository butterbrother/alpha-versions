class ForEach3 {
	public static void main(String args[]) throws java.io.IOException {
		int sum = 0;

		int nums[][] = new int[3][5];

		for (int i = 0; i < nums.length; i++)
			for (int j = 0; j < nums[i].length; j++)
				nums[i][j] = (i+1)*(j+1);

		for (int x[] : nums) {
			for (int y : x) {
				System.out.println("Значение равно: " + y);
				sum += y;
			}
			x[1]=5;
		}

		System.out.println("Сумма: " + sum);
	}
}
