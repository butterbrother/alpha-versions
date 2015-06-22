class AnotherClient implements Callback {
	public void callback(int p) {
		System.out.println("Ещё одна версия callback");
		System.out.println("p в квадрате равно " + (p*p));
	}
}
