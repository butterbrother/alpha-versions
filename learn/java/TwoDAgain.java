class TwoDAgain {
	public static void main(String args[]) {
		int k=0;
		int twoD[][] = new int[5][];
		twoD[0] = new int[1];
		twoD[1] = new int[2];
		twoD[2] = new int[3];
		twoD[3] = new int[4];
		twoD[4] = new int[5];

		for (int a=0; a<twoD.length; a++) {
			for (int b=0; b<twoD[a].length; b++) {
				twoD[a][b]=k;
				if (k<10) 
					System.out.print("[" + a + "][" + b + "]:0" + twoD[a][b] + ";\t");
				else
					System.out.print("[" + a + "][" + b + "]:" + twoD[a][b] + ";\t");
				k++;
			}
			System.out.println("");
		}

	}
}
