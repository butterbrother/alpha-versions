package Except;

class ThrowDemo2 {
	static void throwOne() throws IllegalAccessException {
	//static void throwOne() {
		try {
			System.out.println("Внутри throwsOne.");
			throw new IllegalAccessException("demo");
		} catch(IllegalAccessException e) {
			System.out.println("Перехвачено внутри throwsOne");
			throw new IllegalAccessException("test");
		}
	}
	public static void main(String args[]) {
		try {
			throwOne();
		} catch(IllegalAccessException e) {
			System.out.println("Перехвачено " + e);
		}
	}
}
