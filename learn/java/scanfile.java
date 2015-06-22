import java.util.Scanner;
import java.io.File;
import java.util.NoSuchElementException;
import java.io.FileNotFoundException;

class scanfile {
	public static void main(String args[]) throws NoSuchElementException {
		File tst = new File("config.ini");
		boolean cR = false;
		//Тестируем доступ к файлу
		try {
			if (tst.exists()) {
				if (tst.canRead()) {
					System.out.println("Файл " + tst + " успешно может быть прочитан");
					cR = true;
				} else {
					System.out.println("Файл не может быть прочитан");
				}
			} else {
				System.out.println("Файл не существует");
			}
				
		} catch (SecurityException ext) {
			System.out.println("Доступ к файлу запрещён, " + ext);
		}
		//Пытаемся считывать строки
		if (cR) 
		try {
				Scanner sc = new Scanner(tst);
				String Login, Password, Link;
			try {
				System.out.println("Содержимое " + tst);
				//Тут играемся со stringbuffer
				StringBuffer sB = new StringBuffer("");;
				sB.replace(0, sB.length(), "test"); //отсчёт как обычно
				sB.replace(0, sB.length(), "Some of text"); //реально меняет
				while(sc.hasNextLine()) {
					sB.replace(0, sB.length(), sc.nextLine());
					System.out.println(sB);
					// Пытаемся отыскать логин
					if (sB.indexOf("DBUSER") >= 0) {
						Login=sB.substring(sB.indexOf("DBUSER")+7, sB.length());
						System.out.println("Найден параметр логина = " + Login);
					}
				}
			} catch (IllegalStateException ext) {
				System.out.print("");
			} catch (NoSuchElementException ext) {
				System.out.print("");
			}
			sc.close();
		} catch (FileNotFoundException exc) {
			System.out.println("Файл куда-то делся O.o");
		}
	}

}


