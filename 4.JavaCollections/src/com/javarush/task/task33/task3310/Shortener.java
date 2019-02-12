package com.javarush.task.task33.task3310;

import com.javarush.task.task33.task3310.strategy.StorageStrategy;

public class Shortener {
    private Long lastId = 0L;
    private StorageStrategy storageStrategy;

    public Shortener(StorageStrategy storageStrategy) {
        this.storageStrategy = storageStrategy;
    }

    public synchronized Long getId(String string) {
        Long result;
        if (storageStrategy.containsValue(string)) {
            result = storageStrategy.getKey(string);
        } else {
            storageStrategy.put(++lastId, string);
            result = lastId;
        }
        return result;
    }

    public synchronized String getString(Long id) {
        return storageStrategy.getValue(id);
    }
}
