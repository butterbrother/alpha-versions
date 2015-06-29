import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;

public class NIOCopy {
	public static void main(String args[]) {
		if (args.length < 2) {
			System.out.println("Usage: from to");
			return;
		}

		try {
			Path fromFile = Paths.get(args[0]);
			Path toFile = Paths.get(args[1]);

			Files.copy(fromFile, toFile, StandardCopyOption.REPLACE_EXISTING);
		} catch (InvalidPathException e) {
			System.out.println("Path Error " + e);
		} catch (IOException e) {
			System.out.println("I/O error " + e);
		}
	}
}
