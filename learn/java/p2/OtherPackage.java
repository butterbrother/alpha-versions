package p2;

class OtherPackage {
	OtherPackage() {
		p1.Protection p = new p1.Protection();
		System.out.println("конструктор другого пакета");

		System.out.println("n доступен только для собственного класса или пакета");

		System.out.println("n_pri доступен только своему классу");

		System.out.println("n_pro доступен только для своего класса, пакета либо подкласса другого пакета");

		System.out.println("n_pub = " + p.n_pub);
	}
}
