import java.util.Map;

class EnvReader {
	public static void main(String args[]) {
		//Map<String,String> environment = System.getenv();
		String editor = null;
		try {
			//editor = environment.get("EDITOR");
			editor = System.getenv("EDITOR");
		} catch (Exception exc) {
			System.out.println("Error get environment variable: " + exc);
		}
		if (editor != null)
			System.out.println("Editor environment = " + editor);
		else
			System.out.println("Editor environment not set");
	}
}
