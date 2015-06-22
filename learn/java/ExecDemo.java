class ExecDemo {
	public static void main(String args[]) {
		Runtime r = Runtime.getRuntime();
		Process p = null;
		try {
			p = System.getProperty("os.name").contains("Windows") ? r.exec("notepad") : r.exec("leafpad");
		} catch (Exception e) {
			System.out.println("Ошибка запуска текстового редактора");
		}
	}
}

