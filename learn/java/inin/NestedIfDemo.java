package inin;

class NestedIfDemo {
	public static void main(String args[]) {

		A.NestedIf nif = new B();

		if (nif.isNotNegative(10))
			System.out.println("10 не является отрицательным");
		if (nif.isNotNegative(-12))
			System.out.println("O.o");
	}
}
