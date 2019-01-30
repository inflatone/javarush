package com.javarush.task.task39.task3913;

import java.nio.file.Paths;

public class Solution {
    public static void main(String[] args) {
        LogParser logParser = new LogParser(Paths.get("D:\\YandexDisk\\!!javarush\\JavaRushTasks\\4.JavaCollections\\src\\com\\javarush\\task\\task39\\task3913\\logs"));
        long start = System.currentTimeMillis();
        System.out.println(logParser.execute("get date"));
        System.out.println(System.currentTimeMillis() - start);
    }
}