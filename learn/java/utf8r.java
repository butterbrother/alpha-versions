import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

class utf8r {
	public static void main(String args[]) {
		if (args.length != 1) {
			System.out.println("DEBUG: no parameters");
			return;
		}

		System.out.println("DEBUG: try to open file " + args[0]);
		try (BufferedReader inR = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));
		     BufferedWriter ouW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[0] + ".1251"), "WINDOWS-1251")))
		{
			int i = 0;
			System.out.println("DEBUG: count initialized, try readUTF");
			String lline = null;
			while((lline = inR.readLine()) != null) {
				System.out.println("DEBUG: reading line " + i);
				System.out.println(lline);
				System.out.println("DEBUG: write line " + i);
				ouW.append(lline);
				ouW.append("\r\n");
				i++;
			}
		} catch (FileNotFoundException exc) {
			System.out.println("DEBUG: File not found: " + exc);
		} catch (IOException exc) {
			System.out.println("DEBUG: IO Exception: " + exc);
		}
	}
}
