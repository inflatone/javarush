package com.javarush.task.task38.task3804;

public class ExceptionFactory {
    public static Throwable getException(Enum exceptionMessage) {
        Throwable result = new IllegalArgumentException();
        if (exceptionMessage != null) {
            String message = exceptionMessage.name().substring(0, 1).toUpperCase()
                    .concat(exceptionMessage.name().substring(1).toLowerCase())
                    .replace('_', ' ');
            if (exceptionMessage.getClass() == ExceptionApplicationMessage.class) {
                result = new Exception(message);
            } else if (exceptionMessage.getClass() == ExceptionDBMessage.class) {
                result = new RuntimeException(message);
            } else if (exceptionMessage.getClass() == ExceptionUserMessage.class) {
                result = new Error(message);
            }
        }
        return result;
    }
}
