package Except;

class ThrowDemo {
	static void demoproc() {
		try {
			throw new NullPointerException("demo");
		} catch(NullPointerException e) {
			System.out.println("Перехвачено внутри demoproc." + e);
			throw e;
		}
	}
	public static void main(String args[]) {
		try {
			demoproc();
		} catch(NullPointerException e) {
			System.out.println("Повторный перехват: " + e);
		}
	}
}
