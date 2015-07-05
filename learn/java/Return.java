class Return {
	public static void main(String args[]) {
		boolean t = true;

		System.out.println("До выполнения возврата.");

		if (t) return;

		System.out.println("Этот оператор выполняться не будет");
	}
}
