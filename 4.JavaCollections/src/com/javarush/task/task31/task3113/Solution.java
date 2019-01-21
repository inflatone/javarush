package com.javarush.task.task31.task3113;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/* 
Что внутри папки?
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String stringPath = reader.readLine();
        reader.close();
        Path path = Paths.get(stringPath);
        if (Files.isDirectory(path)) {
            walk(path);
        } else {
            System.out.println(String.format("%s - не папка", path.toString()));
        }
    }

    private static void walk(Path path) throws IOException{
        class StatisticsCollectingFileVisitor extends SimpleFileVisitor<Path> {
            private long totalBytes;
            private int filesCounter;
            private int directoriesCounter = -1;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                directoriesCounter++;
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                filesCounter++;
                totalBytes += Files.size(file);
                return super.visitFile(file, attrs);
            }
        }
        StatisticsCollectingFileVisitor visitor = new StatisticsCollectingFileVisitor();
        Files.walkFileTree(path, visitor);
        System.out.println(String.format("Всего папок - %s", visitor.directoriesCounter));
        System.out.println(String.format("Всего файлов - %s", visitor.filesCounter));
        System.out.println(String.format("Общий размер - %s", visitor.totalBytes));
    }
}
