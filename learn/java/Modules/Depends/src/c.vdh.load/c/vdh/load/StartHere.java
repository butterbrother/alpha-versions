package c.vdh.load;

import c.vdh.utils.ZipUtils;

import java.io.IOException;
import java.nio.file.Path;

public class StartHere {

    public static void main(String ...args) {
        final var zipUtils = new ZipUtils();
        for (var item: args) {
            try {
                zipUtils.doZip(Path.of(item), Path.of(item + ".zip"));
            } catch (IOException err) {
                System.err.format("Unable compress \"%s\": %s", item, err);
            }
        }
    }
}
