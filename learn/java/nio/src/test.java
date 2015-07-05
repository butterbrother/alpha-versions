import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.io.*;

class test {
	public static void main(String args[]) {
		Path tpath = Paths.get("test.txt");
		ByteBuffer tbuf = ByteBuffer.allocate(512);
		int pos;
		try (SeekableByteChannel tchan = Files.newByteChannel(tpath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
			while ((pos = tchan.read(tbuf)) != -1) {
			}
		} catch (IOException exc) {}
	}
}
