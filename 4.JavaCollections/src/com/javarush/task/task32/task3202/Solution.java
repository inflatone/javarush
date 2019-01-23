package com.javarush.task.task32.task3202;

import java.io.*;

/* 
Читаем из потока
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        StringWriter writer = getAllDataFromInputStream(new FileInputStream(new File("D:/projects/output.txt")));
        System.out.println(writer.toString());
    }

    public static StringWriter getAllDataFromInputStream(InputStream is) throws IOException {
        StringWriter result = new StringWriter();
        if (is != null) {
            BufferedWriter writer = new BufferedWriter(result);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            char[] buffer = new char[8 * 1024];
            for (int len = reader.read(buffer); len > -1; len = reader.read(buffer)) {
                writer.write(buffer, 0, len);
            }
            writer.close();
            reader.close();
        }
        return result;
    }
}