class Box {
	private double width, height, depth;

	Box(Box ob) {
		this.width = ob.width;
		this.height = ob.height;
		this.depth = ob.depth;
	}

	Box() {
		this.width = -1;
		this.height = -1;
		this.depth = -1;
	}

	Box(double length) {
		this.width = this.height = this.depth = length;
	}

	Box(double width, double height, double depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	public void setsizes(double ... sz) {
		switch (sz.length) {
			case 1: this.width = this.height = this.depth = sz[0];
				break;
			case 2: this.width = sz[0];
				this.height = sz[1];
				break;
			case 3: this.width = sz[0];
				this.height = sz[1];
				this.depth = sz[2];
				break;
			default: System.out.println("Invalid parameters");
		}
	}

	public double volume() {
		return this.width * this.height * this.depth;
	}

	public double getwidth() {
		return this.width;
	}

	public double getheight() {
		return this.height;
	}

	public double getdepth() {
		return this.depth;
	}
}

class BoxWeight extends Box {
	private double weight;

	BoxWeight(BoxWeight ob) {
		super(ob);
		this.weight = ob.weight;
	}

	BoxWeight() {
		super();
		this.weight = 0;
	}

	BoxWeight(double width, double height, double depth, double weight) {
		super(width, height, depth);
		this.weight = weight;
	}

	public void setweight(double weight) {
		this.weight = weight;
	}

	public void setparams(double ... sz) {
		switch (sz.length) {
			case 1: super.setsizes(sz[0]);
				break;
			case 2: super.setsizes(sz[0], sz[1]);
				break;
			case 3: super.setsizes(sz[0], sz[1], sz[2]);
				break;
			case 4: super.setsizes(sz[0], sz[1], sz[2]);
				this.weight = sz[3];
				break;
			default: System.out.println("Invalid parameters");
		}
	}

	public double getweight() {
		return this.weight;
	}
}

class DemoSuper {
	public static void main(String args[]) {
		System.out.print("");
		BoxWeight test1 = new BoxWeight();
		BoxWeight test2 = new BoxWeight(12, 15, 18, 5.0);

		test2.setparams(14, 25);

		test1.setsizes(4);
	}
}
