package p1;

class Derived extends Protection {
	Derived() {
		System.out.println("конструктор подкласса");
		System.out.println("n = " + n);

		System.out.println("n_pri доступен только для класса");

		System.out.println("n_pro = " + n_pro);
		System.out.println("n_pub = " + n_pub);
	}
}

