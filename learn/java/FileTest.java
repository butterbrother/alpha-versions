import java.io.FileReader;
import java.io.FileNotFoundException;

class FileTest {
	public static void main(String args[]) {
		FileReader flr;
		try {
			flr = new FileReader("test.txt");
		} catch (FileNotFoundException exc) {
			System.out.println("Щрд. Файл не найден");
		}
	}
}
