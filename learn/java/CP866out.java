import java.io.UnsupportedEncodingException;
import java.io.IOException;

public class CP866out {
	public static void main(String args[]) throws UnsupportedEncodingException, IOException {
		for (int i=0; i<=255; i++)  {
			System.out.write(i);
		}
		System.out.println();
		System.out.write("ВАСЯ\n".getBytes("CP866")); 
		System.out.println(System.getProperty("os.name"));
	}
}
