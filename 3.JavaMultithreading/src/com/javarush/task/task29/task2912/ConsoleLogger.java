package com.javarush.task.task29.task2912;

public class ConsoleLogger extends AbstractLogger {
    public ConsoleLogger(int level) {
        super(level);
    }

    @Override
    String getAction() {
        return "Logging to console";
    }
}