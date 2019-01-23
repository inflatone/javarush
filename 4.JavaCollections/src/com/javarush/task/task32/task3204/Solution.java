package com.javarush.task.task32.task3204;

import java.io.ByteArrayOutputStream;
import java.util.Random;

/* 
Генератор паролей
*/
public class Solution {
    public static void main(String[] args) {
        ByteArrayOutputStream password = getPassword();
        System.out.println(password.toString());
    }

    public static ByteArrayOutputStream getPassword() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        do {
            baos.reset();
            new Random().ints(48, 123)
                    .filter(i -> i <= 57 || i >= 65 && i <= 90 || i >= 97)
                    .limit(8).forEach(baos::write);
        } while (isNotCorrect(baos));
        return baos;
    }

    private static boolean isNotCorrect(ByteArrayOutputStream baos) {
        String password = baos.toString();
        return !password.matches(".*[0-9].*")
                || !password.matches(".*[A-Z].*")
                || !password.matches(".*[a-z].*");
    }
}