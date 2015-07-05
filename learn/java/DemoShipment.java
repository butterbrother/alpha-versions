class Box {
	double width = -1, height = -1, depth = -1;

	Box(Box ob) {
		this.width = ob.width;
		this.height = ob.height;
		this.depth = ob.depth;
	}

	Box(double width, double height, double depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		System.out.println("Box debug: width = " + width + ", height = " + height + ", depth = " + depth);
	}

	Box() {
	}

	Box(double len) {
		width = height = depth = len;
	}

	public double volume() {
		return width * height * depth;
	}
}

class BoxWeight extends Box {
	double weight = -1;

	BoxWeight(BoxWeight ob) {
		super(ob);
		this.weight = ob.weight;
	}

	BoxWeight(double width, double height, double depth, double weight) {
		super(width, height, depth);
		this.weight = weight;
	}

	BoxWeight() {
	}

	BoxWeight(double len, double m) {
		super(len);
		this.weight = m;
	}
}

class Shipment extends BoxWeight {
	double cost = -1;

	Shipment(Shipment ob) {
		super(ob);
		this.cost = ob.cost;
	}

	Shipment() {
	}

	Shipment(double width, double height, double depth, double weight, double cost) {
		super(width, height, depth, weight);
		System.out.println("Shipment debug: width = " + width + ", height = " + height + ", depth = " + depth);
		System.out.println("Shipment debug: weight = " + weight);
		this.cost = cost;
	}

	Shipment(double len, double mass, double cost) {
		super(len, mass);
		this.cost = cost;
	}
}

class DemoShipment {
	public static void main(String args[]) {
		Shipment shipment1 = new Shipment(10, 20, 15, 10, 3.41);
		Shipment shipment2 = new Shipment(2, 3, 4, 0.76, 1.28);

		System.out.println("Объём shipment1 равен " + shipment1.volume());
		System.out.println("Вес shipment1 равен " + shipment1.weight);
		System.out.println("Стоимость доставки: $" + shipment1.cost);

		System.out.println("Объём shipment2 равен " + shipment2.volume());
		System.out.println("Вес shipment2 равен " + shipment2.weight);
		System.out.println("Стоимость доставки: $" + shipment2.cost);
	}
}

