package com.javarush.task.task25.task2512;

import java.util.LinkedList;

/*
Живем своим умом
*/
public class Solution implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        t.interrupt();
        LinkedList<Throwable> exceptions = new LinkedList<>();
        Throwable cursor = e;
        while (cursor != null) {
            exceptions.add(cursor);
            cursor = cursor.getCause();
        }
        while((cursor = exceptions.pollLast()) != null) {
            System.out.printf("%s: %s%n", cursor.getClass().getName(), cursor.getMessage());
        }
    }

    public static void main(String[] args) {
        new Solution().uncaughtException(
                new Thread(),
                new Exception("ABC", new RuntimeException("DEF", new IllegalAccessException("GHI")))
        );
    }
}
