class TestClone implements Cloneable {
	int a;
	double b;

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Клонирование невозможно.");
			return this;
		}
	}
}

class CloneDemo2 {
	public static void main(String args[]) {
		TestClone x1 = new TestClone();
		TestClone x2;
		x1.a = 10;
		x1.b = 20.98;

		x2 = (TestClone) x1.clone();
		System.out.println("x1: " + x1.a + " " + x1.b);
		System.out.println("x2: " + x2.a + " " + x2.b);
	}
}
