package com.javarush.task.task33.task3310.tests;

import com.javarush.task.task33.task3310.Helper;
import com.javarush.task.task33.task3310.Shortener;
import com.javarush.task.task33.task3310.strategy.HashBiMapStorageStrategy;
import com.javarush.task.task33.task3310.strategy.HashMapStorageStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpeedTest {
    @Test
    public void testHashMapStorage() {
        Set<String> origStrings = Stream.generate(Helper::generateRandomString)
                .limit(10000)
                .collect(Collectors.toSet());
        Shortener shortener1 = new Shortener(new HashMapStorageStrategy());
        Shortener shortener2 = new Shortener(new HashBiMapStorageStrategy());

        Set<Long> ids1 = new HashSet<>();
        Set<Long> ids2 = new HashSet<>();

        long time1 = getTimeToGetIds(shortener1, origStrings, ids1);
        long time2 = getTimeToGetIds(shortener2, origStrings, ids2);
        Assert.assertTrue(time1 > time2);

        long time3 = getTimeToGetStrings(shortener1, ids1, new HashSet<>());
        long time4 = getTimeToGetStrings(shortener2, ids2, new HashSet<>());
        Assert.assertEquals(time3, time4, 30);
    }

    public long getTimeToGetIds(Shortener shortener, Set<String> strings, Set<Long> ids) {
        long start = System.currentTimeMillis();
        ids = strings.stream().map(shortener::getId).collect(Collectors.toSet());
        return System.currentTimeMillis() - start;
    }

    public long getTimeToGetStrings(Shortener shortener, Set<Long> ids, Set<String> strings) {
        long start = System.currentTimeMillis();
        strings = ids.stream().map(shortener::getString).collect(Collectors.toSet());
        return System.currentTimeMillis() - start;
    }

}
