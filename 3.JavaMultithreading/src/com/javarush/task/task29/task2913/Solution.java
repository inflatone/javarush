package com.javarush.task.task29.task2913;

import java.util.Random;

/* 
Замена рекурсии
*/

public class Solution {
    private static int numberA;
    private static int numberB;

    public static String getAllNumbersBetween(int a, int b) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(a);
        if (a > b) {
            for (int i = a - 1; i >= b; i--) {
                buffer.append(' ').append(i);
            }
        } else {
            for (int i = a + 1; i <= b; i++) {
                buffer.append(' ').append(i);
            }
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        Random random = new Random();
        numberA = random.nextInt(100);
        numberB = random.nextInt(1000);
        System.out.println(getAllNumbersBetween(numberA, numberB));
        System.out.println(getAllNumbersBetween(numberB, numberA));
    }
}