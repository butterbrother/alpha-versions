class Client implements Callback {
	public void callback(int p) {
		System.out.println("Метод callback, вызванный со значением " + p);
	}
}
