package com.javarush.task.task33.task3310;

import com.javarush.task.task33.task3310.strategy.HashMapStorageStrategy;
import com.javarush.task.task33.task3310.strategy.StorageStrategy;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Solution {
    public static void main(String[] args) {
        testStrategy(new HashMapStorageStrategy(), 10000);
    }

    public static Set<Long> getIds(Shortener shortener, Set<String> strings) {
        return strings.stream().map(shortener::getId).collect(Collectors.toSet());
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys) {
        return keys.stream().map(shortener::getString).collect(Collectors.toSet());
    }

    public static void testStrategy(StorageStrategy strategy, long elementsNumber) {
        Helper.printMessage(strategy.getClass().getSimpleName());
        Set<String> strings = LongStream.range(0, elementsNumber)
                .mapToObj(i -> Helper.generateRandomString())
                .collect(Collectors.toSet());
        Shortener shortener = new Shortener(strategy);
        Date start = new Date();
        Set<Long> ids = getIds(shortener, strings);
        Helper.printMessage(
                Long.toString(new Date().getTime() - start.getTime())
        );
        start = new Date();
        Set<String> result = getStrings(shortener, ids);
        Helper.printMessage(
                Long.toString(new Date().getTime() - start.getTime())
        );
        Helper.printMessage(result.equals(strings) ? "Тест пройден." : "Тест не пройден.");
    }
}