class Box {
	double width, height, depth;

	Box(double w, double h, double d) {
		width = w;
		height = h;
		depth = d;
	}

	double volume() {
		return width * height * depth;
	}
}

class BoxDemo7 {
	public static void main(String args[]) {
		Box mybox1 = new Box(10, 20, 15);
		Box mybox2 = new Box(3, 6, 9);

		System.out.println("Объём равен " + mybox1.volume());
		System.out.println("Объём равен " + mybox2.volume());
	}
}
