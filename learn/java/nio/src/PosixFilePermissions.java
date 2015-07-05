import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;

/**
 * Created by user on 22.06.2015.
 */
public class PosixFilePermissions {
    public static void main(String args[]) throws IOException {
        Path tst = Paths.get("./tst.txt");
        if (Files.notExists(tst))
            Files.createFile(tst);

        PosixFileAttributes attrib = Files.readAttributes(tst, PosixFileAttributes.class);
        UserPrincipal user = attrib.owner();
        GroupPrincipal group = attrib.group();
        System.out.println("user: " + user);
        System.out.println("group: " + group);
        System.out.println("Permissions:");
        for (PosixFilePermission item : attrib.permissions()) {
            System.out.println(item);
        }

    }
}
