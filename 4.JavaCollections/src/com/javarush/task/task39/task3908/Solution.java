package com.javarush.task.task39.task3908;

import java.util.Map;
import java.util.stream.Collectors;

/*
Возможен ли палиндром?
*/
public class Solution {
    public static void main(String[] args) {

    }

    public static boolean isPalindromePermutation(String s) {
        return lineToMap(s).values().stream().filter(count -> count % 2 != 0).count() <= 1;
    }

    private static Map<Character, Integer> lineToMap(String s) {
        return s.toUpperCase().chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toMap(c -> c, c -> 1, (old, fresh) -> old + fresh));
    }
}
