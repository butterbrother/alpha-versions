class Box {
	double width, depth, height;

	double volume() {
		return width * depth * height;
	}

	void setDim(double w, double h, double d) {
		width = w;
		height = h;
		depth = d;
	}
}

class BoxDemo5 {
	public static void main(String args[]) {
		Box mybox1 = new Box();
		Box mybox2 = new Box();

		mybox1.setDim(10, 15, 20);
		mybox2.setDim(3, 6, 9);

		System.out.println("Объём равен: " + mybox1.volume());
		System.out.println("Объём равен: " + mybox2.volume());
	}
}
