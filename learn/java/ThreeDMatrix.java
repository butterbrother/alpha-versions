class ThreeDMatrix {
	public static void main(String args[]) {
		int M[][][] = new int [3][4][5];

		for (int a = 0; a < M.length; a++) {
			for (int b = 0; b < M[a].length; b++ ) {
				for (int c = 0; c < M[a][b].length; c++) {
					M[a][b][c] = a * b * c;
					if (M[a][b][c] < 10)
						System.out.print("0" + M[a][b][c] + " ");
					else
						System.out.print(M[a][b][c] + " ");
				}
				System.out.println();
			}
			System.out.println();
		}
	}
}
