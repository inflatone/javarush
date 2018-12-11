package com.javarush.task.task22.task2210;

import java.util.Arrays;
import java.util.StringTokenizer;

/*
StringTokenizer
*/
public class Solution {
    public static void main(String[] args) {
        Arrays.stream(Solution.getTokens(null, "."))
                .forEach(System.out::println);

    }
    public static String [] getTokens(String query, String delimiter) {
        if (query == null) {
            return new String[0];
        }
        if (delimiter == null) {
            return new String[]{query};
        }

        StringTokenizer tokenizer = new StringTokenizer(query, delimiter);
        String[] result = new String[tokenizer.countTokens()];
        for (int i = 0; i != result.length; i++) {
            result[i] = tokenizer.nextToken();
        }
        return result;
    }
}
