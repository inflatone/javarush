package com.javarush.task.task22.task2211;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/* 
Смена кодировки
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        FileInputStream in = new FileInputStream(args[0]);
        FileOutputStream out = new FileOutputStream(args[1]);
        byte[] buffer = new byte[1000];
        while (in.available() > 0) {
            int num = in.read(buffer);
            String temp = new String(buffer, 0, num, Charset.forName("windows-1251"));
            out.write(temp.getBytes(StandardCharsets.UTF_8));
        }
        out.close();
        in.close();
    }
}
