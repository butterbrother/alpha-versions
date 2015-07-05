class ExecDemoFini {
	public static void main(String args[]) {
		Runtime r = Runtime.getRuntime();
		Process p = null;

		try {
			p = System.getProperty("os.name").contains("Windows") ? r.exec("notepad") : r.exec("leafpad");
			p.waitFor();
		} catch (Exception e) {
			System.out.println("Ошибка запуска notepad.");
		}

		System.out.println("Текстовый редактор возвратил " + p.exitValue());
	}
}
