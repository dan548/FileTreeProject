package ge.sibraine.filetreeproject;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public class Main {

    public static void main(String[] args) {
        final var rootPath = "src/main/resources";
        final var myVisitor = new MyFileVisitor();

        var root = Paths.get(rootPath);
        try {
            Files.walkFileTree(root, myVisitor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var sortedPaths = myVisitor.sortedPaths();
        try (var writeChannel = FileChannel.open(Path.of(rootPath, "res.txt"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            sortedPaths.forEach(path -> {
                try (var readChannel = FileChannel.open(path, StandardOpenOption.READ)) {
                    readChannel.transferTo(0, readChannel.size(), writeChannel);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
