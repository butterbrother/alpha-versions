class Foo {
	int a=0;
}

class arrtest {
	public static void main(String args[]) {
		Foo test[] = new Foo[4];
		Foo b = new Foo();
		test[3]=b;
		System.out.println(test[3].a);
	}
}
