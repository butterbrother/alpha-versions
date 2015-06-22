package Files;

import java.io.FileInputStream;
import java.io.IOException;

class ShowFileWS {
	public static void main(String args[]) {
		if (args.length != 1)
			return;

		try (FileInputStream fi = new FileInputStream(args[0])) {
			int i;
			do {
				i = fi.read();
				if (i != -1)
					System.out.print((char) i);
			} while (i != -1);
		} catch (IOException exc) {
			System.out.println("I/O exception: " + exc);
		}
	}
}
