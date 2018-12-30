package com.javarush.task.task22.task2208;

import java.util.HashMap;
import java.util.Map;

/* 
Формируем WHERE
*/
public class Solution {
    public static void main(String[] args) {

        Map<String, String> params = new HashMap<>();
        params.put("name", null);
        params.put("country", null);
        params.put("city", null);
        params.put("age", "sdf");
        System.out.println(getQuery(params));


    }

    public static String getQuery(Map<String, String> params) {
        StringBuilder buffer = new StringBuilder();

        params.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .forEachOrdered(
                        entry -> buffer.append(" and ")
                                .append(entry.getKey())
                                .append(" = ")
                                .append('\'')
                                .append(entry.getValue())
                                .append('\'')
                );
        return buffer.length() > 5 ? buffer.substring(5) : "";
    }
}
