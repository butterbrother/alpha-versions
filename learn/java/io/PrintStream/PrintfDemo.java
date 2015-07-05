package io.PrintStream;

class PrintfDemo {
	public static void main(String args[]) {
		System.out.println("Ниже следуют некоторые числовые значения в различных форматах.\n");

		System.out.printf("Различные целочисленные форматы: ");
		System.out.printf("%d %(d %+d %05d\n", 3, -3, 3, 3);

		System.out.println();
		System.out.printf("Формат с плавающей точкой по умолчанию: %f\n", 1234567.123);
		System.out.printf("Плавающая точка с запятыми: %,f\n", 1234567.123);
		System.out.printf("Отрицательная плавающая точка по умолчанию: %,f\n", -1234567.123);
		System.out.printf("Параметры отрицательной плавающей точки: %,(f\n", -1234567.123);

		System.out.println();

		System.out.printf("Строка из положительных и отрицательных значений:\n");
		System.out.printf("% ,.2f\n% ,.2f\n", 1234567.123, -1234567.123);
	}
}
