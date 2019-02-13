package com.javarush.task.task33.task3310.tests;

import com.javarush.task.task33.task3310.Shortener;
import com.javarush.task.task33.task3310.strategy.*;
import org.junit.Assert;
import org.junit.Test;

public class FunctionalTest {
    @Test
    public void testHashMapStorageStrategy() {
        Shortener shortener = new Shortener(new HashMapStorageStrategy());
        testStorage(shortener);
    }

    @Test
    public void testOurHashMapStorageStrategy() {
        Shortener shortener = new Shortener(new OurHashMapStorageStrategy());
        testStorage(shortener);
    }

    @Test
    public void testFileStorageStrategy() {
        Shortener shortener = new Shortener(new FileStorageStrategy());
        testStorage(shortener);
    }

    @Test
    public void testHashBiMapStorageStrategy() {
        Shortener shortener = new Shortener(new HashBiMapStorageStrategy());
        testStorage(shortener);
    }

    @Test
    public void testDualHashBidiMapStorageStrategy() {
        Shortener shortener = new Shortener(new DualHashBidiMapStorageStrategy());
        testStorage(shortener);
    }

    @Test
    public void testOurHashBiMapStorageStrategy() {
        Shortener shortener = new Shortener(new OurHashBiMapStorageStrategy());
        testStorage(shortener);
    }

    public void testStorage(Shortener shortener) {
        String firstLine = "qwerty";
        String secondLine = "asdfgh";
        String thirdLine = "qwerty";

        Long id1 = shortener.getId(firstLine);
        Long id2 = shortener.getId(secondLine);
        Long id3 = shortener.getId(thirdLine);

        Assert.assertNotEquals(id1, id2);
        Assert.assertEquals(id1, id3);

        String first = shortener.getString(id1);
        String second = shortener.getString(id2);
        String third = shortener.getString(id3);

        Assert.assertEquals(first, firstLine);
        Assert.assertEquals(second, secondLine);
        Assert.assertEquals(third, thirdLine);
    }

}
