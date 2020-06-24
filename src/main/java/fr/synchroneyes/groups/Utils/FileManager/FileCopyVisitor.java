package fr.synchroneyes.groups.Utils.FileManager;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileCopyVisitor extends SimpleFileVisitor<Path> {

    private Path fromPath;
    private Path toPath;
    private StandardCopyOption copyOption;


    public FileCopyVisitor(Path fromPath, Path toPath, StandardCopyOption copyOption) {
        this.fromPath = fromPath;
        this.toPath = toPath;
        this.copyOption = copyOption;
    }

    public FileCopyVisitor(Path fromPath, Path toPath) {
        this(fromPath, toPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            throws IOException {

        Path targetPath = toPath.resolve(fromPath.relativize(dir));
        if (!Files.exists(targetPath)) {
            Files.createDirectory(targetPath);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {

        Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);
        return FileVisitResult.CONTINUE;
    }
}
