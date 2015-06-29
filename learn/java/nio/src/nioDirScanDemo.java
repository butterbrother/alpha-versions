import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by user on 29.06.2015.
 */
public class nioDirScanDemo {
    public static void main(String args[]) {
        NIODirectoryScanner dirscan = new NIODirectoryScanner();
        String includes[] = { "*.java" };
        dirscan.addIncludedList(includes);
        try {
            dirscan.scan();
        } catch (IOException err) {
            System.out.println("IO error: " + err);
        }
        System.out.println("dir list");
        for (Path item : dirscan.getFoundedDirs()) {
            System.out.println(item);
        }
        System.out.println("files list");
        for (Path item : dirscan.getFoundedFiles()) {
            System.out.println(item);
        }
    }
}
