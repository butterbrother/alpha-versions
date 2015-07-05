import java.io.*;
import java.util.Formatter;

/**
 * Реализация простого grep на Java.
 * Возможно, он жрёт тонны памяти
 */
public class jgrep {
    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("Usage: filename phrase1..phraseN");
            System.exit(1);
        }
        String fname = args[0];
        Formatter progressBar = new Formatter(System.out);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fname))), 4096);
            String readed;
            while ((readed = reader.readLine()) != null) {
                for (String item : args) {
                    if (item.equals(fname)) continue;   // Пропускаем имя файла из аргументов
                    if (readed.toLowerCase().contains(item.toLowerCase())) {
                        progressBar.format("-- %s: %s\n", item, readed);
                    }
                }
            }
            reader.close();
        } catch (IOException err) {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException ignore) {}
            System.err.println("I/O error: " + err);
        }
    }
}
