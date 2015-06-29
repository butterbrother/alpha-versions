import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class OldMappedChannelWrite {
	public static void main(String args[]) {
		RandomAccessFile fOut = null;
		FileChannel fChan = null;
		ByteBuffer mBuf;

		try {
			fOut = new RandomAccessFile("test.txt", "rw");
			fChan = fOut.getChannel();
			mBuf = fChan.map(FileChannel.MapMode.READ_WRITE, 0, 26);
			for (int i = 0; i<26; i++)
				mBuf.put((byte)('A' + i));
		} catch (IOException e) {
			System.out.println("I/O error " + e);
		} finally {
			try {
				if (fChan != null) fChan.close();
			} catch (IOException e) {
				System.out.println("Error closing channel");
			}
			try {
				if (fOut != null) fOut.close();
			} catch (IOException e) {
				System.out.println("Error closing file");
			}
		}
	}
}
