class ByteShift {
	public static void main(String args[]) {
		String Decode[] = new String[257];
		byte a = 64, b;
		int i;
		Decode[64]="0 0100 0000";
		Decode[0]="0 0000 0000";
		Decode[256]="1 0000 0000";

		i = a << 2;
		b = (byte)(a << 2);

		System.out.println("Первоначальное значение a: " + Decode[a]);
		System.out.println("i and b: " + Decode[i] + " " + Decode[b]);
	}
}
