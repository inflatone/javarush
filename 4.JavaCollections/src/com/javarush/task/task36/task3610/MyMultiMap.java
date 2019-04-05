package com.javarush.task.task36.task3610;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class MyMultiMap<K, V> extends HashMap<K, V> implements Cloneable, Serializable {
    static final long serialVersionUID = 123456789L;
    private HashMap<K, List<V>> map;
    private int repeatCount;

    public MyMultiMap(int repeatCount) {
        this.repeatCount = repeatCount;
        map = new HashMap<>();
    }

    @Override
    public int size() {
        return map.values().stream().mapToInt(List::size).sum();
        //напишите тут ваш код
    }

    @Override
    public V put(K key, V value) {
        List<V> values = map.computeIfAbsent(key, k -> new ArrayList<>());
        if (values.size() == repeatCount) {
            values.remove(0);
        }
        values.add(value);
        return values.size() == 1 ? null : values.get(values.size() - 2);
        //напишите тут ваш код
    }

    @Override
    public V remove(Object key) {
        //напишите тут ваш код
        List<V> values = map.get(key);
        if (values == null) {
            return null;
        }
        V removed = values.remove(0);
        if (values.isEmpty()) {
            map.remove(key);
        }
        return removed;
    }

    @Override
    public Set<K> keySet() {
        //напишите тут ваш код
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        //напишите тут ваш код
        return map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public boolean containsKey(Object key) {
        //напишите тут ваш код
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        //напишите тут ваш код
        return values().contains(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            for (V v : entry.getValue()) {
                sb.append(v);
                sb.append(", ");
            }
        }
        String substring = sb.substring(0, sb.length() - 2);
        return substring + "}";
    }
}