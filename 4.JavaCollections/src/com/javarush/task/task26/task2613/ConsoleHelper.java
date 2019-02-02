package com.javarush.task.task26.task2613;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.Predicate;

public class ConsoleHelper {
    private final static BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));
    private final static String TWO_POSITIVE_NUMBER_REGEX = "^[1-9]\\d*" + "\\s+" + "[1-9]\\d*" + "\\s*$";

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() {
        String result = "";
        try {
            result = bis.readLine();
        } catch (IOException ignore) {
        }
        return result;
    }

    public static String askCurrencyCode() {
        writeMessage("Currency code?");
        String code;
        do {
            code = readString();
        } while (isInvalidCurrencyCode(code));
        return code.toUpperCase();
    }

    public static String[] getValidTwoDigits(String currencyCode) {
        writeMessage("Denomination and amount?");
        String line;
        do {
            line = readString();
        } while (isNAN(line));
        return line.split("\\s+");
    }

    private static boolean isInvalidCurrencyCode(String currencyCode) {
        return validate(currencyCode, line -> line.length() != 3);
    }

    private static boolean isNAN(String number) {
        return validate(number, line -> !line.matches(TWO_POSITIVE_NUMBER_REGEX));
    }

    private static boolean validate(String line, Predicate<String> checker) {
        boolean result = checker.test(line);
        if (result) {
            writeMessage("invalid, try again");
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(getValidTwoDigits("")));
    }
}
