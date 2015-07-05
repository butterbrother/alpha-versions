// Симулятор жизни
// v 0.0000000a
//
package Threads;

import java.util.Random;

// Хранит и выводит "поле"
class display {
	//Содержимое поля, буфер
	static char[][] matrix = new char[20][40];

	//Инициализация, заполнение пустотами (пока пустотами)
	static void initial() {
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				display.matrix[i][j]=' ';
	}

	// Отображение состояния
	static void show() {
		//Чистим экран
		for (int i=0; i<=25; i++)
			System.out.println("");
		//Верхняя граница
		System.out.println("Press Ctrl+C to stop");
		for (int i=0; i<matrix[0].length; i++)
			System.out.print("_");
		System.out.println("");
		for (char i[]:matrix) {
			System.out.print("|"); //Левый борт
			//Содержимое
			for (char j:i)
				System.out.print(j);
			//Правый борт
			System.out.println("|");
		}
		//Нижняя граница
		for (int i=0; i<matrix[0].length; i++)
			System.out.print("_");
	}
}

//Гуляющий объект
class Pointer implements Runnable {
	int X,Y;			//Текущие координаты
	Random rand = new Random();	//Каждый со своим random-ом...
	char c;				//Отображаемый символ
	//Конструктор с внешним запуском потока...
	Pointer() {
		c = (char)(rand.nextDouble()*10+31);
		X = (int)(rand.nextDouble()*20);
		Y = (int)(rand.nextDouble()*40);
		display.matrix[X][Y]=c;
	}
	//...и самостоятельным. С возможностью создания доп. объектов
	//(в будущем - однотипных)
	Pointer(int copies) {
		//Произвольное положение символа
		c = (char)(rand.nextDouble()*10+31);
		X = (int)(rand.nextDouble()*20);
		Y = (int)(rand.nextDouble()*40);
		display.matrix[X][Y]=c;

		for (int i=0; i<copies; i++)
			new Pointer(0);

		Thread Th = new Thread(this);
		Th.start();
	}

	public void run() {
		
		try {
			while (true) {
				//Симуляция бесцельного шатания
				//Пока что так
				int Cs = (int)(rand.nextDouble()*4);
				display.matrix[X][Y]=' ';
				switch (Cs) {
					case 0: if (X>0)
							X--;
						break;
					case 1: if (X<display.matrix.length-1)
							X++;
						break;
					case 2: if (Y>0)
							Y--;
						break;
					default: if (Y<display.matrix[0].length-1)
							 Y++;
				}
				display.matrix[X][Y]=c;
				//display.show();
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			System.out.println(c + " dead");
		} catch (NullPointerException e) {
			//Вообще я предусмотрел, что бы координаты не
			//выходили за пределы поля
			//Но пусть будет
			X=0;
			Y=0;
		}
	}
}

class Net {
	public static void main(String args[]) {
		//Создаём поле
		display.initial();
		//и объекты = числу в параметре
		if (args.length>0) {
			int num = Integer.parseInt(args[0], 10);
			new Pointer(num-1);
		} else {
			Pointer pt = new Pointer();
			Thread th = new Thread(pt);
			th.start();
		}
		
		//Перерисовка каждые 500 мс
		try {
			while (true) {
				display.show();
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
		}
	}
}
