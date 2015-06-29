import java.io.*;
import java.nio.file.*;

class ShowFile {
	public static void main(String args[]) {
		int i;

		if (args.length != 1) {
			System.out.println("Usage: ShowFile filename");
			return;
		}

		try (InputStream fin = Files.newInputStream(Paths.get(args[0]))) {
			while ((i = fin.read()) != -1) {
				System.out.print((char) i);
			}
		} catch (InvalidPathException e) {
			System.out.println("Path error: " + e);
		} catch (IOException e) {
			System.out.println("I/O error: " + e);
		}
	}
}
