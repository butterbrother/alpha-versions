package c.vdh.utils;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

public class ZipUtils {

    public void doZip(final Path input, final Path outputZip) throws IOException {
        final var uri = URI.create("jar:file:" + outputZip.toUri().getPath());
        final var params = Map.of("create", "true");
        Files.deleteIfExists(outputZip);
        try (var fs = FileSystems.newFileSystem(uri, params)) {
            var root = fs.getPath("/");
            if (Files.isRegularFile(input)) {
                var inner = root.resolve(input.getFileName());
                Files.copy(input, inner);
            } else if (Files.isDirectory(input)) {
                recursiveAdd(fs, root, input);
            }
        }
    }

    private void recursiveAdd(final FileSystem fs, final Path zipFsDir, final Path realDir) throws IOException {
        final var innerDir = fs.getPath(zipFsDir.toString(), realDir.getFileName().toString());
        Files.createDirectory(innerDir);
        copyAttributes(realDir, innerDir);
        try (var dirStream = Files.newDirectoryStream(realDir)) {
            for (var item : dirStream) {
                if (Files.isDirectory(item)) {
                    recursiveAdd(fs, innerDir, item);
                } else {
                    final var innerFile = fs.getPath(innerDir.toString(), item.getFileName().toString());
                    Files.copy(item, innerFile);
                    copyAttributes(item, innerFile);
                }
            }
        }
    }

    private void copyAttributes(final Path source, final Path destination) throws IOException {
        var sourceAttr = Files.readAttributes(source, BasicFileAttributes.class);
        var destAttr = Files.getFileAttributeView(destination, BasicFileAttributeView.class);
        destAttr.setTimes(sourceAttr.lastModifiedTime(), sourceAttr.lastAccessTime(), sourceAttr.creationTime());
    }
}

