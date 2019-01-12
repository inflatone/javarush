package com.javarush.task.task30.task3009;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/* 
Палиндром?
*/

public class Solution {
    public static void main(String[] args) {
        System.out.println(getRadix("112"));        //expected output: [3, 27, 13, 15]
        System.out.println(getRadix("123"));        //expected output: [6]
        System.out.println(getRadix("5321"));       //expected output: []
        System.out.println(getRadix("1A"));         //expected output: []
    }

    private static Set<Integer> getRadix(String number) {
        Set<Integer> result;
        try {
            final int num = Integer.parseInt(number);
            result = IntStream.rangeClosed(2, 36)
                    .filter(i -> isPalindrome(Integer.toString(num, i)))
                    .boxed()
                    .collect(Collectors.toSet());
        } catch (Exception ignored) {
            result = new HashSet<>();
        }
        return result;
    }

    private static boolean isPalindrome(String number) {
        boolean result = true;
        for (int i = 0; i < number.length() / 2; i++) {
            if (number.charAt(i) != number.charAt(number.length() - i - 1)) {
                result = false;
                break;
            }
        }
        return result;
    }
}