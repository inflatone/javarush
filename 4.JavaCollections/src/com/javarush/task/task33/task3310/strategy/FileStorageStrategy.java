package com.javarush.task.task33.task3310.strategy;

import java.util.Objects;

public class FileStorageStrategy implements StorageStrategy {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final long DEFAULT_BUCKET_SIZE_LIMIT = 2000;

    private FileBucket[] table = new FileBucket[DEFAULT_INITIAL_CAPACITY];
    private long bucketSizeLimit = DEFAULT_BUCKET_SIZE_LIMIT;

    private int size;
    private long maxBucketSize;


    public long getBucketSizeLimit() {
        return bucketSizeLimit;
    }

    public void setBucketSizeLimit(long bucketSizeLimit) {
        this.bucketSizeLimit = bucketSizeLimit;
    }

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
        size++;
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        if (table[index] == null) {
            createEntry(hash, key, value, index);
        } else {
            addEntry(hash, key, value, index);
        }
        maxBucketSize = Math.max(maxBucketSize, table[index].getFileSize());
        if (maxBucketSize > bucketSizeLimit) {
            resize(2 * table.length);
        }
    }

    @Override
    public Long getKey(String value) {
        Long result = null;
        for (int i = 0; i < table.length && result == null; i++) {
            FileBucket bucket = table[i];
            if (bucket == null) {
                continue;
            }
            for (
                    Entry current = table[i].getEntry();
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

    private int hash(Long key) {
        int h = Objects.hashCode(key);
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return (length - 1) & hash;
    }

    private Entry getEntry(Long key) {
        Entry result = null;
        FileBucket bucket = table[indexFor(hash(key), table.length)];
        if (bucket != null) {
            for (
                    Entry current = bucket.getEntry();
                    current != null;
                    current = current.next
            ) {
                if (current.key.equals(key)) {
                    result = current;
                    break;
                }
            }
        }
        return result;
    }

    private void resize(int newCapacity) {
        FileBucket[] newTable = new FileBucket[newCapacity];
        transfer(newTable);
        for (FileBucket bucket : table) {
            bucket.remove();
        }
        table = newTable;
        maxBucketSize = 0L;
    }

    private void transfer(FileBucket[] newTable) {
        int len = newTable.length;
        for (FileBucket bucket : table) {
            if (bucket != null) {
                for (
                        Entry current = bucket.getEntry();
                        current != null;
                        current = current.next
                ) {
                    int index = indexFor(current.hash, len);
                    if (newTable[index] == null) {
                        newTable[index] = new FileBucket();
                        newTable[index].putEntry(current);
                    } else {
                        putLastInBucket(newTable[index], current.hash, current.key, current.value);
                    }
                }
            }
        }
    }

    private void addEntry(int hash, Long key, String value, int bucketIndex) {
        putLastInBucket(table[bucketIndex], hash, key, value);
    }

    private void createEntry(int hash, Long key, String value, int bucketIndex) {
        FileBucket temp = new FileBucket();
        temp.putEntry(new Entry(hash, key, value, null));
        table[bucketIndex] = temp;
    }

    private void putLastInBucket(FileBucket fileBucket, int hash, Long key, String value) {
        Entry first = fileBucket.getEntry();
        Entry current = first;
        while (current.next != null) {
            current = current.next;
        }
        current.next = new Entry(hash, key, value, null);
        fileBucket.putEntry(first);
    }
}
