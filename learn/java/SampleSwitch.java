class SampleSwitsh {
	public static void main(String args[]) {
		for (int i=0; i<6; i++)
			switch(i) {
				case 0:
					System.out.println("i равен нулю.");
					break;
				case 1:
					System.out.println("i равен единице.");
					break;
				case 2:
					System.out.println("i равно двум.");
					break;
				case 3:
					System.out.println("i равно трём.");
					break;
				default:
					System.out.println("i больше 3.");
			}
	}
}
