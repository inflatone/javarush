package com.javarush.task.task31.task3106;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/*
Разархивируем файл
*/
public class Solution {
    public static void main(String[] args) {
        List<Exception> exceptions = new ArrayList<>();
        Vector<InputStream> archiveParts = Arrays.stream(args)
                .skip(1)
                .sorted(Comparator.comparing(s -> s.substring(s.lastIndexOf('.' + 1))))
                .map(name -> {
                    InputStream result = null;
                    try {
                        result = new FileInputStream(name);
                    } catch (FileNotFoundException e) {
                        exceptions.add(e);
                    }
                    return result;
                })
                .collect(Collectors.toCollection(Vector::new));
        if (exceptions.isEmpty() && args.length != 0) {
            try {
                ZipInputStream zipIn = new ZipInputStream(new SequenceInputStream(archiveParts.elements()));
                OutputStream fileOut = new FileOutputStream(args[0]);
                zipIn.getNextEntry();
                byte[] buffer = new byte[8 * 1024];
                for (int len = zipIn.read(buffer); len > 0; len = zipIn.read(buffer)) {
                    fileOut.write(buffer, 0, len);
                }
                zipIn.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

