package com.javarush.task.task31.task3101;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
Проход по дереву файлов
*/
public class Solution {
    public static void main(String[] args) {
        String path = args[0];
        File resultFileAbsolutePath = new File(args[1]);
        File result  = new File(resultFileAbsolutePath.getParent() + "\\allFilesContent.txt");
        renameFile(resultFileAbsolutePath, result);
        List<Path> fileList = new ArrayList<>();
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(result));
            collectAllFiles(Paths.get(path), fileList);
            fileList.sort(Comparator.comparing(Path::getFileName));
            for (Path file : fileList) {
                writeFile(bos, file);
            }
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void renameFile(File source, File destination) {
        if (FileUtils.isExist(destination)) {
            FileUtils.deleteFile(destination);
        }
        FileUtils.renameFile(source, destination);

    }

    private static void collectAllFiles(Path path, List<Path> fileList) throws IOException {
        if (Files.isRegularFile(path) && Files.size(path) <= 50) {
            fileList.add(path);
        }
        if (Files.isDirectory(path)) {
            for (Path file : Files.newDirectoryStream(path)) {
                collectAllFiles(file, fileList);
            }
        }
    }

    private static void writeFile(BufferedOutputStream bos, Path path) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(path))) {
            byte[] buffer = new byte[8 * 1024];
            for (int len = bis.read(buffer); len > 0; len = bis.read(buffer)) {
                bos.write(buffer, 0, len);
            }
            bos.write('\n');
        }
    }
}
