package io.RandomAccessFile;

import java.io.*;

class RandomAccessFileDemo {
	public static void main(String args[]) {
		try (RandomAccessFile rnd = new RandomAccessFile("test.iof", "rw")) {
			rnd.setLength(1024*1024*1024);
		} catch (IOException e) {
			System.err.println("I/O error: " + e);
		}
	}
}
