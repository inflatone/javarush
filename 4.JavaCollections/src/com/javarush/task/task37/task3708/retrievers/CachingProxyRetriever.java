package com.javarush.task.task37.task3708.retrievers;

import com.javarush.task.task37.task3708.cache.LRUCache;
import com.javarush.task.task37.task3708.storage.Storage;

public class CachingProxyRetriever implements Retriever {
    private LRUCache<Long, Object> cache;
    private OriginalRetriever original;

    public CachingProxyRetriever(Storage storage) {
        original = new OriginalRetriever(storage);
        cache = new LRUCache<>(100);
    }

    @Override
    public Object retrieve(long id) {
        Object result = cache.find(id);
        if (result == null) {
            result = original.retrieve(id);
            cache.set(id, result);
        }
        return result;
    }
}
