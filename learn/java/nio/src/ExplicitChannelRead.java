import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;

public class ExplicitChannelRead {
	public static void main(String args[]) {
		int count;
		Path filepath = null;

		try {
			filepath = Paths.get("test.txt");
		} catch (InvalidPathException e) {
			System.err.println("Path Error " + e);
			return;
		}

		try (SeekableByteChannel fChan = Files.newByteChannel(filepath)) {
			ByteBuffer mBuf = ByteBuffer.allocate(512);
			do {
				count = fChan.read(mBuf);
				if (count != -1) {
					mBuf.rewind();
					for (int i=0; i < count; i++)
						System.out.print((char)mBuf.get());
				}
			} while (count != -1);

			System.out.println();
		} catch (IOException e) {
			System.out.println("I/O error: " + e);
		}
	}
}
