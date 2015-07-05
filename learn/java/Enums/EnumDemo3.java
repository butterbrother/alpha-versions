package Enums;

enum Apple {
	Jonathan(10), GoldenDel(9), RedDel, Winesap(15), Cortland(8);

	private int price;

	Apple(int p) {
		price = p;
		System.out.println("Создана константа перечисления");
	}

	Apple() {
		price = -1;
		System.out.println("Создана константа перечисления");
	}

	int getPrice() {
		return price;
	}
}

class EnumDemo3 {
	public static void main(String args[]) {
		//Apple ap;

		System.out.println("Момент до первого обращения к элементу перечисления");
		System.out.println("Winesap стоит " + Apple.Winesap.getPrice() + " центов.\n");

		System.out.println("Все цены яблок:");
		for (Apple a : Apple.values())
			System.out.println(a + " стоит " + a.getPrice() + " центов.");
	}
}
