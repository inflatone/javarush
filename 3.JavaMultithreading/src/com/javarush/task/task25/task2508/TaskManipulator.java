package com.javarush.task.task25.task2508;

public class TaskManipulator implements Runnable, CustomThreadManipulator {
    private Thread runner;

    @Override
    public void run() {
        try {
            Thread current = Thread.currentThread();
            while (!current.isInterrupted()) {
                System.out.println(current.getName());
                Thread.sleep(100);
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void start(String threadName) {
        runner = new Thread(this, threadName);
        runner.start();
    }

    @Override
    public void stop() {
        runner.interrupt();
    }
}
