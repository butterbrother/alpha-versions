class Matrix {
	public static void main(String args[]) {
		double m[][] = {
			{ 0*0, 1*0, 2*0, 3*0 },
			{ 0*0, 1*1, 2*1, 3*1 },
			{ 0*0, 1*1, 2*2, 3*2 },
			{ 0*3, 1*3, 2*3, 3*3 }
		};
		for (int i=0; i<m.length; i++) {
			for (int j=0; j<m[i].length; j++)
				System.out.print(m[i][j] + " ");
			System.out.println();
		}
	}
}
