package com.javarush.task.task31.task3111;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class SearchFileVisitor extends SimpleFileVisitor<Path> {
    private String partOfName;
    private String partOfContent;
    private int minSize;
    private int maxSize;
    private List<Path> foundFiles = new ArrayList<>();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        byte[] content = Files.readAllBytes(file); // размер файла: content.length
        if (isValid(file, content)) {
            foundFiles.add(file);
        }
        return super.visitFile(file, attrs);
    }

    public void setPartOfName(String partOfName) {
        this.partOfName = partOfName;
    }

    public void setPartOfContent(String partOfContent) {
        this.partOfContent = partOfContent;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public List<Path> getFoundFiles() {
        return foundFiles;
    }

    private boolean hasPartOfContent(byte[] array, String target) {
        char[] targetChars = target.toCharArray();
        outer:
        for (int i = 0; i < array.length - target.length() + 1; i++) {
            for (int j = 0; j < targetChars.length; j++) {
                if (array[i + j] != targetChars[j]) {
                    continue outer;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isValid(Path file, byte[] content) {
        return (partOfName == null || file.getFileName().toString().contains(partOfName))
                && (minSize == 0 || content.length >= minSize)
                && (maxSize == 0 || content.length <= maxSize)
                && (partOfContent == null || hasPartOfContent(content, partOfContent));
    }
}
