package nio;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

class DirList {
	public static void main(String args[]) {
		String dirname = "nio";

		try (DirectoryStream<Path> dirstrm = Files.newDirectoryStream(Paths.get(dirname))) {
			System.out.println("Directory of " + dirname);

			for (Path entry : dirstrm) {
				BasicFileAttributes attribs = Files.readAttributes(entry, BasicFileAttributes.class);
				System.out.print(entry.getName(1));
				if (attribs.isDirectory()) {
					System.out.println("/");
				} else {
					System.out.println();
				}
			}
		} catch (InvalidPathException e) {
			System.out.println("Path error " + e);
		} catch (NotDirectoryException e) {
			System.out.println(dirname + " is not a directory");
		} catch (IOException e) {
			System.out.println("I/O error: " + e);
		}
	}
}
