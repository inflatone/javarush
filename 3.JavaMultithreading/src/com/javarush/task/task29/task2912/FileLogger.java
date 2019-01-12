package com.javarush.task.task29.task2912;

public class FileLogger extends AbstractLogger {
    public FileLogger(int level) {
        super(level);
    }

    @Override
    String getAction() {
        return "Logging to file";
    }
}