package com.javarush.task.task22.task2212;

import java.util.Arrays;

/*
Проверка номера телефона
*/
public class Solution {
    public static boolean checkTelNumber(String telNumber) {
        boolean result = telNumber != null && telNumber.length() >= 10;
        if (!result) {
            return false;
        }
        int digitCount = telNumber.charAt(0) == '+' ? 12 : 10;
        int hyphenCount = 2;
        int bracketCount = 2;

        for (int i = 0; i < telNumber.length() && result; i++) {
            char c = telNumber.charAt(i);
            if (c > 47 && c < 58 && digitCount > 0) {
                digitCount--;
            } else if (c == '(' && bracketCount == 2 && hyphenCount == 2) {
                bracketCount--;
                result = i < telNumber.length() - 6 && telNumber.charAt(i + 4) == ')';
            } else if (c == ')' && bracketCount == 1) {
                bracketCount--;
            } else if (c == '-' && hyphenCount > 0 && i + 1 != telNumber.length()) {
                hyphenCount--;
                result = telNumber.charAt(i + 1) != '-';
            } else {
                result = i == 0 && c == '+';
            }
        }
        return result && digitCount == 0;
    }

    public static void main(String[] args) {
        Arrays.asList(
                "+380501234567",
                "+38(050)1234567",
                "+38050123-45-67",
                "050123-4567",
                "+38)050(1234567",
                "+38(050)1-23-45-6-7",
                "050ххх4567",
                "050123456",
                "(0)501234567",
                "12345678",
                "+12345678912",
                "",
                null
        ).forEach(v -> System.out.printf("%s - %s%n", v, checkTelNumber(v)));
    }
}
