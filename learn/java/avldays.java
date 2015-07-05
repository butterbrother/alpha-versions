class avldays {
	public static void main(String args[]) {
		double dPlus, dFreespace;
		int iAvl_month, iAvl_days;
		if (args.length >= 2) {
			System.out.println("plus 1 day in mb\tFreeSpace\tAvaliableMonth\tAvaliableDays");
			for (int i=0; i<(args.length/2)*2; i+=2) {
				dPlus = Double.parseDouble(args[i]);
				dFreespace = Double.parseDouble(args[i+1]);
				iAvl_month = (int)(dFreespace / dPlus / 31);
				iAvl_days = (int)(dFreespace / dPlus) - iAvl_month * 31;
				System.out.print(dPlus + "\t" + dFreespace + "\t");
				System.out.println(iAvl_month + "\t" + iAvl_days);
			}
		}
	}
}
