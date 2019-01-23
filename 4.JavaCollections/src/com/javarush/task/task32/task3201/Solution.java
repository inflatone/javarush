package com.javarush.task.task32.task3201;

import java.io.IOException;
import java.io.RandomAccessFile;

/*
Запись в существующий файл
*/
public class Solution {
    public static void main(String... args) {
        if (args.length >= 3) {
            String fileName = args[0];
            byte[] textArray = args[2].getBytes();
            try {
                int number = Integer.parseInt(args[1]);
                RandomAccessFile file = new RandomAccessFile(fileName, "rw");
                file.seek(Math.min(file.length(), number));
                file.write(textArray);
                file.close();
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }

    }
}
