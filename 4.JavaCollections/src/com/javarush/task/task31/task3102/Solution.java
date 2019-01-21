package com.javarush.task.task31.task3102;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* 
Находим все файлы
*/
public class Solution {
    public static List<String> getFileTree(String root) throws IOException {
        List<String> result = new ArrayList<>();
        LinkedList<Path> queue = new LinkedList<>();
        queue.add(Paths.get(root));
        for (Path current = queue.poll(); current != null; current = queue.poll()) {
            if (Files.isRegularFile(current)) {
                result.add(current.toString());
            } else if (Files.isDirectory(current)) {
                Files.newDirectoryStream(current).forEach(queue::add);
            }
        }
        return result;

    }

    public static void main(String[] args) {
    }
}
