package com.javarush.task.task33.task3310.strategy;

import java.util.Objects;

public class OurHashMapStorageStrategy implements StorageStrategy {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Entry[] table = new Entry[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private float loadFactor = DEFAULT_LOAD_FACTOR;


    @Override
    public boolean containsKey(Long key) {
        return getValue(key) != null;
    }

    @Override
    public boolean containsValue(String value) {
        return getKey(value) != null;
    }

    @Override
    public void put(Long key, String value) {
        if (++size >= threshold) {
            resize(table.length * 2);
        }
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        if (table[index] == null) {
            createEntry(hash, key, value, index);
        } else {
            addEntry(hash, key, value, index);
        }
    }

    @Override
    public Long getKey(String value) {
        Long result = null;
        for (int i = 0; i < table.length && result == null; i++) {
            for (
                    Entry current = table[i];
                    current != null;
                    current = current.next
            ) {
                if (current.value.equals(value)) {
                    result = current.key;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public String getValue(Long key) {
        return getEntry(key).value;
    }

    private int hash(Long k) {
        int h = Objects.hashCode(k);
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return (length - 1) & hash;
    }

    private Entry getEntry(Long key) {
        Entry result = null;
        for (
                Entry current = table[indexFor(hash(key), table.length)];
                current != null;
                current = current.next
        ) {
            if (current.key.equals(key)) {
                result = current;
                break;
            }
        }
        return result;
    }

    private void resize(int newCapacity) {
        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    private void transfer(Entry[] newTable) {
        int len = newTable.length;
        for (int i = 0; i < table.length; i++) {
            for (
                    Entry current = table[i];
                    current != null;
                    current = current.next
            ) {
                int index = indexFor(current.hash, len);
                if (newTable[index] == null) {
                    newTable[index] = new Entry(current.hash, current.key, current.value, null);
                } else {
                    putLastInBucket(newTable[index], current.hash, current.key, current.value);
                }
            }
        }
    }

    private void addEntry(int hash, Long key, String value, int bucketIndex) {
        putLastInBucket(table[bucketIndex], hash, key, value);
    }

    private void createEntry(int hash, Long key, String value, int bucketIndex) {
        table[bucketIndex] = new Entry(hash, key, value, null);
    }

    private void putLastInBucket(Entry bucket, int hash, Long key, String value) {
        while (bucket.next != null) {
            bucket = bucket.next;
        }
        bucket.next = new Entry(hash, key, value, null);
    }
}
