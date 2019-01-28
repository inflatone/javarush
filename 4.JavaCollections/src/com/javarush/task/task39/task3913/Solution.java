package com.javarush.task.task39.task3913;

import java.nio.file.Paths;
import java.util.Date;

public class Solution {
    public static void main(String[] args) {
        LogParser logParser = new LogParser(Paths.get("D:\\YandexDisk\\!!javarush\\JavaRushTasks\\4.JavaCollections\\src\\com\\javarush\\task\\task39\\task3913\\logs"));
        System.out.println(logParser.getNumberOfUniqueIPs(null, new Date()));
        System.out.println(logParser.getUniqueIPs(null, new Date()));
        System.out.println(logParser.getIPsForEvent(Event.DOWNLOAD_PLUGIN, null, new Date()));
        System.out.println(logParser.getIPsForEvent(Event.LOGIN, new Date(), null));
        System.out.println(logParser.getIPsForEvent(Event.LOGIN, new Date(), null));
    }
}