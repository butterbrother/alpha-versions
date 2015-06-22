package io.SequenceInputStream;

import java.io.*;
import java.util.*;

class InputStreamEnumerator implements Enumeration<FileInputStream> {
	private Enumeration<String> files;

	public InputStreamEnumerator(Vector<String> files) {
		this.files = files.elements();
	}

	public boolean hasMoreElements() {
		return files.hasMoreElements();
	}

	public FileInputStream nextElement() {
		try {
			return new FileInputStream(files.nextElement().toString());
		} catch (IOException e) {
			return null;
		}
	}
}

class SequenceInputStreamDemo {
	public static void main(String args[]) {
		int c;
		Vector<String> files = new Vector<String>();

		files.addElement("file1.txt");
		files.addElement("file2.txt");
		files.addElement("file3.txt");
		try (InputStream input = new SequenceInputStream(new InputStreamEnumerator(files))) {
			while ((c = input.read()) != -1)
				System.out.print((char) c);
		} catch (NullPointerException e) {
			System.out.println("Error opening file.");
		} catch (IOException e) {
			System.out.println("I/O error: " + e);
		}
	}
}
