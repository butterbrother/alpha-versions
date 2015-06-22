class Box {
	double width;
	double height;
	double depth;

	Box(Box ob) {
		this.width = ob.width;
		this.height = ob.height;
		this.depth = ob.depth;
	}

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
		return this.width * this.height * this.depth;
	}
}

class BoxWeight extends Box {
	double weight;

	BoxWeight(double w, double h, double d, double m) {
		this.width = w;
		this.height = h;
		this.depth = d;
		this.weight = m;
	}
}

class DemoBoxWeith {
	public static void main(String args[]) {
		BoxWeight mybox1 = new BoxWeight(10, 20, 15, 34.3);
		BoxWeight mybox2 = new BoxWeight(2, 3, 4, 0.076);

		System.out.println("Объём mybox1 равен " + mybox1.volume());
		System.out.println("Вес mybox1 равен " + mybox1.weight);
		System.out.println();

		System.out.println("Объём mybox2 равен " + mybox2.volume());
		System.out.println("Вес mybox2 равен " + mybox2.weight);
	}
}
