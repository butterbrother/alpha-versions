package io.DataIOStream;

import java.io.*;

class DataIODemo {
	public static void main(String args[]) {
		try (DataOutputStream dout = new DataOutputStream(new FileOutputStream("test.dat"))) {
			dout.writeDouble(98.6);
			dout.writeInt(1000);
			dout.writeBoolean(true);
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open output file");
			return;
		} catch (IOException e) {
			System.err.println("I/O error: " + e);
		}

		try (DataInputStream din = new DataInputStream(new FileInputStream("test.dat"))) {
			double d = din.readDouble();
			int i = din.readInt();
			boolean b = din.readBoolean();

			System.out.println("Значения: " + d + " " + i + " " + b);
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open input file");
			return;
		} catch (IOException e) {
			System.err.println("I/O error: " + e);
		}
	}
}
