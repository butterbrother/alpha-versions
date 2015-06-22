package Enums;

enum Apple {
	Jonathan, GoldenDel, RedDel, Winesap, Cortland
}

class EnumDemo {
	public static void main(String args[]) {
		Apple ap;

		ap = Apple.RedDel;

		System.out.println("Значение ap: " + ap);
		System.out.println();

		ap = Apple.GoldenDel;

		if (ap == Apple.GoldenDel)
			System.out.println("ap содержит GoldenDel\n");

		switch(ap) {
			case Jonathan:
				System.out.println("Jonathan красные");
				break;
			case GoldenDel:
				System.out.println("Golden Delicious жёлтый");
				break;
			case RedDel:
				System.out.println("Red Delicious красный");
				break;
			case Winesap:
				System.out.println("Winesap красный.");
				break;
		}
	}
}
