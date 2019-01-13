package com.javarush.task.task30.task3004;

import java.util.concurrent.RecursiveTask;

public class BinaryRepresentationTask extends RecursiveTask<String> {
    private int i;

    public BinaryRepresentationTask(int i) {
        this.i = i;
    }

    @Override
    protected String compute() {
        int mod = i % 2;
        int div = i / 2;
        String result;
        if (div > 0) {
            BinaryRepresentationTask task = new BinaryRepresentationTask(div);
            task.fork();
            result = task.join() + mod;
        } else {
            result = String.valueOf(mod);
        }
        return result;
    }

}
