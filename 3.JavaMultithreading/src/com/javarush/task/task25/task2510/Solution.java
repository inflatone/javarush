package com.javarush.task.task25.task2510;

/* 
Поживем - увидим
*/
public class Solution extends Thread {

    public Solution() {
        this.setUncaughtExceptionHandler((thread, throwable) -> System.out.println(
                throwable instanceof Error ? "Нельзя дальше работать"
                        : throwable instanceof Exception ? "Надо обработать" : "Поживем - увидим"
        ));
    }

    public static void main(String[] args) {
    }
}
