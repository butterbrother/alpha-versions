import java.io.Console;
import java.io.IOError;


class passinput {
	public static void main(String args[]) throws IOError{
		Console cons;
		cons = System.console();
		char[] passwd = cons.readPassword("[%s]");
		System.out.println(new String(passwd));
	}
}
