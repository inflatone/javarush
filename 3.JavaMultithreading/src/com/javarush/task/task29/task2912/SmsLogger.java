package com.javarush.task.task29.task2912;

public class SmsLogger extends AbstractLogger {
    public SmsLogger(int level) {
        super(level);
    }

    @Override
    String getAction() {
        return "Send sms to CEO";
    }
}