import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

class PathDemo {
	public static void main(String args[]) {
		Path filepath = Paths.get("/home/user/.ssh");
		if (Files.exists(filepath)) {
			StringBuilder fileName = new StringBuilder();
			fileName.append("File Name: ");
			for (int i=0; i<filepath.getNameCount(); i++)
				fileName.append("[").append(filepath.getName(i)).append("]");
			System.out.println(fileName);
			System.out.println("Path: " + filepath);
			System.out.println("Absolute Path: " + filepath.toAbsolutePath());
			System.out.println("Parrent: " + filepath.getParent());
			System.out.println("File exists");
			try {
				if (Files.isHidden(filepath))
					System.out.println("File is hidden");
				else
					System.out.println("File is not hidden");
			} catch (IOException e) {
				System.out.println("I/O Error: " + e);
			}

			Files.isWritable(filepath);
			System.out.println("File is writable");

			Files.isReadable(filepath);
			System.out.println("File is readable");

			try {
				BasicFileAttributes attribs = Files.readAttributes(filepath, BasicFileAttributes.class);

				if (attribs.isDirectory())
					System.out.println("The file is a directory");
				else
					System.out.println("File is not a directory");

				if (attribs.isRegularFile())
					System.out.println("File is a normal file");
				else
					System.out.println("File is not a normal fle");

				if (attribs.isSymbolicLink())
					System.out.println("File is a symbolic link");
				else
					System.out.println("File is not a symbolic linc");

				System.out.println("File last modifies: " + attribs.lastModifiedTime());
				System.out.println("File size: " + attribs.size());
			} catch (IOException e) {
				System.out.println("Error reading attributes: " + e);
			}
		}
	}
}
