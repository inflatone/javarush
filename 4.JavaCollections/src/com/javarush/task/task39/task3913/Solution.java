package com.javarush.task.task39.task3913;

import java.nio.file.Paths;

public class Solution {
    public static void main(String[] args) {
        LogParser logParser = new LogParser(Paths.get("D:\\YandexDisk\\!!javarush\\JavaRushTasks\\4.JavaCollections\\src\\com\\javarush\\task\\task39\\task3913\\logs"));
        long start = System.currentTimeMillis();
        System.out.println(logParser.execute("get event for status = \"OK\" and event = \"DONE_TASK\" and date between \"11.12.2010 0:00:00\" and \"03.01.2014 23:59:59\""));
        System.out.println(System.currentTimeMillis() - start);
    }
}