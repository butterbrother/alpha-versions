class Box {
	double width;
	double height;
	double depth;

	Box(double w, double h, double d) {
		this.width = w;
		this.height = h;
		this.depth = d;
	}

	Box() {
		this.width = -1;
		this.height = -1;
		this.depth = -1;
	}

	Box(double len) {
		this.width = this.height = this.depth = len;
	}

	double volume() {
		return width * height * depth;
	}
}

class OverloadConst {
	public static void main(String args[]) {
		Box mybox1 = new Box(10, 20, 15);
		Box mybox2 = new Box();
		Box mycube = new Box(7);

		System.out.println("Объём mybox1 равен " + mybox1.volume());
		System.out.println("Объём mybox2 равен " + mybox2.volume());
		System.out.println("Объём mycube равен " + mycube.volume());
	}
}
