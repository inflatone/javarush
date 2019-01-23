package com.javarush.task.task32.task3210;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Solution {
    public static void main(String... args) {
        if (args.length >= 3) {
            try {
                RandomAccessFile file = new RandomAccessFile(args[0], "rw");
                int number = Integer.parseInt(args[1]);
                String matcher = args[2];
                byte[] buffer = new byte[matcher.length()];
                file.seek(number);
                file.read(buffer, 0, matcher.length());
                file.seek(file.length());
                file.write(
                        Boolean.toString(
                                matcher.equals(new String(buffer))
                        ).getBytes()
                );
            } catch (IOException | NumberFormatException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
