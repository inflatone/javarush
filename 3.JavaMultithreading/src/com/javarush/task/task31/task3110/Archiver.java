package com.javarush.task.task31.task3110;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class Archiver {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Absolute path of archive?");
        ZipFileManager manager = new ZipFileManager(Paths.get(reader.readLine()));
        System.out.println("Absolute path of archived items?");
        manager.createZip(Paths.get(reader.readLine()));


    }
}
