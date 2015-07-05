class PBDemo {
	public static void main(String args[]) {
		try {
			ProcessBuilder proc = System.getProperty("os.name").contains("Windows") ? new ProcessBuilder("notepad.exe", "testfile") : new ProcessBuilder("leafpad", "testfile");
			proc.start();
		} catch (Exception e) {
			System.out.println("Ошибка запуска текстового редактора");
		}
	}
}
