import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class OldMappedChannelRead {
	public static void main(String args[]) {
		FileInputStream fIn = null;
		FileChannel fChan = null;
		long fSize;
		MappedByteBuffer mBuf;

		try {
			fIn = new FileInputStream("test.txt");
			fChan = fIn.getChannel();
			fSize = fChan.size();
			mBuf = fChan.map(FileChannel.MapMode.READ_ONLY, 0, fSize);

			for (int i = 0; i < fSize; i++)
				System.out.print((char)mBuf.get());
		} catch (IOException e) {
			System.out.println("I/O error " + e);
		} finally {
			try {
				if (fChan != null) fChan.close();
			} catch (IOException e) {
				System.out.println("Error closing channel");
			}
			try {
				if (fIn != null) fIn.close();
			} catch (IOException e) {
				System.out.println("Error closing file");
			}
		}
	}
}
