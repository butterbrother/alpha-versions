package p2;

class Protection2 extends p1.Protection {
	Protection2() {
		System.out.println("унаследованный конструктор другого пакета");

		System.out.println("n доступен только для собственного класса или пакета");

		System.out.println("n_pri доступно только для собственного класса");

		System.out.println("n_pro = " + n_pro);
		System.out.println("n_pub = " + n_pub);
	}
}
