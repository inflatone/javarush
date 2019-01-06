package com.javarush.task.task30.task3008;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Вспомогательный класс, для чтения или записи в консоль.
 *
 * @author Alexander Savchenko
 * @version 1.0
 * @since 2019-01-05
 */
public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() {
        String result = null;
        do {
            try {
                result = reader.readLine();
            } catch (IOException ioe) {
                writeMessage("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
            }
        } while (result == null);
        return result;
    }

    public static int readInt() {
        int result;
        do {
            try {
                result = Integer.parseInt(readString());
                return result;
            } catch (NumberFormatException nfe) {
                writeMessage("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
            }
        } while (true);
    }
}
