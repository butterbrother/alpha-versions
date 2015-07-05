class Test {
	void meth(int i, int j) {
		i *= 2;
		i /= 2;
	}
}

class CallByValue {
	public static void main(String args[]) {
		Test ob = new Test();

		int a = 15, b = 20;

		System.out.println("a и b перед вызовом: " + a + " " + b);
		ob.meth(a,b);

		System.out.println("a и b после вызова: " + a + " " + b);
	}
}
