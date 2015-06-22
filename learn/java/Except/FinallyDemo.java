package Except;

class FinallyDemo {
	static void procA() {
		try {
			System.out.println("внутри procA");
			throw new RuntimeException("демо");
		} finally {
			System.out.println("блок finally procA");
		}
	}

	static void procB() {
		try {
			System.out.println("внутри procB");
		} finally {
			System.out.println("блок finally procB");
		}
	}

	static void procC() {
		try {
			System.out.println("внутри procC");
		} finally {
			System.out.println("блок finally procC");
		}
	}

	public static void main(String args[]) {
		try {
			procA();
		} catch (Exception e) {
			System.out.println("Исключение перехвачено");
			System.out.println(e);
			e.printStackTrace();
		}

		procB();
		procC();
	}
}
