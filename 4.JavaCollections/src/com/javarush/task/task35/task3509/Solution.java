package com.javarush.task.task35.task3509;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/* 
Collections & Generics
*/
public class Solution {

    public static void main(String[] args) {
    }

    public static <T> ArrayList<T> newArrayList(T... elements) {
        //напишите тут ваш код
        return new ArrayList<>(Arrays.asList(elements));
    }

    public static <T> HashSet<T> newHashSet(T... elements) {
        //напишите тут ваш код
        return new HashSet<>(Arrays.asList(elements));
    }

    public static <K, V> HashMap<K, V> newHashMap(List<? extends K> keys, List<? extends V> values) throws IllegalAccessException {
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException();
        }
        return IntStream.range(0, keys.size()).boxed().collect(
                Collectors.toMap(
                        keys::get,
                        values::get,
                        (old, fresh) -> fresh,
                        HashMap::new
                )
        );
    }
}
