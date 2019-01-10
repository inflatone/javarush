package com.javarush.task.task28.task2805;

import java.util.concurrent.atomic.AtomicInteger;

public class MyThread extends Thread {
    private final static AtomicInteger priorityCounter = new AtomicInteger(Thread.MIN_PRIORITY);

    public MyThread() {
        setPriority();
    }

    public MyThread(Runnable target) {
        super(target);
        setPriority();
    }

    public MyThread(ThreadGroup group, Runnable target) {
        super(group, target);
        setPriority();
    }

    public MyThread(String name) {
        super(name);
        setPriority();
    }

    public MyThread(ThreadGroup group, String name) {
        super(group, name);
        setPriority();
    }

    public MyThread(Runnable target, String name) {
        super(target, name);
        setPriority();
    }

    public MyThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
        setPriority();
    }

    public MyThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
        setPriority();
    }

    private void setPriority() {
        int priority = priorityCounter.getAndAccumulate(
                1,
                (x, dx) -> x == Thread.MAX_PRIORITY ? Thread.MIN_PRIORITY : x + dx);
        ThreadGroup group = getThreadGroup();
        setPriority(group != null && priority > group.getMaxPriority() ? group.getMaxPriority() : priority);
    }

}
