class Box {
	double width;
	double height;
	double depth;

	Box(Box ob) {
		this.width = ob.width;
		this.height = ob.height;
		this.depth = ob.depth;
	}

	Box(double height, double width, double depth) {
		this.height = height;
		this.width = width;
		this.depth = depth;
	}

	Box() {
		this.width = -1;
		this.height = -1;
		this.depth = -1;
	}

	Box(double len) {
		width = height = depth = len;
	}

	double volume() {
		return depth*height*width;
	}
}

class OverloadConst2 {
	public static void main(String args[]) throws java.io.IOException {
		Box mybox1 = new Box(10, 20, 15);
		Box mybox2 = new Box();
		Box mycube = new Box(7);

		Box myclone = new Box(mybox1);

		System.out.println("Объём mybox1 равен " + mybox1.volume());
		System.out.println("Объём mybox2 равен " + mybox2.volume());
		System.out.println("Объём mycube равен " + mycube.volume());
		System.out.println("Объём myclone равен " + myclone.volume());
	}
}
