package Enums;

enum Position {
	UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
}

class Test {
	public static void main (String args[]) {
		Position pos = Position.DOWN;

		pos = Position.UP;
		System.out.println("Изменённое - " + pos);

		System.out.println("Сравниваем, например, с " + Positions.UP);
		if (pos == Position.UP)
			System.out.println("Таки равны");
		else
			System.out.println("Таки не совсем равны");

		switch (pos) {
			case UP: System.out.println("pos в фазе UP");
				 break;
			case DOWN: System.out.println("pos в фазе DOWN");
				   break;
			default: System.out.println("Остальные фазы расписывать лень");
		}
	}
}

