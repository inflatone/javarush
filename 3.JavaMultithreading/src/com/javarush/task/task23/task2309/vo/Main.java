package com.javarush.task.task23.task2309.vo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Последовательно работающие лампочки.
 *
 * @author Alexander Savchenko
 * @version 1.0
 * @since 2018-12-29
 */
public class Main {
    public final static int AMOUNT = 10;


    public static void main(String[] args) {
        Object lock = new Object();
        AtomicInteger counter = new AtomicInteger();
        IntStream.range(0, AMOUNT)
                .mapToObj(i -> new Bulb(String.format("Bulb #%s", i), i, counter, lock))
                .forEach(Thread::start);
    }
}

class Bulb extends Thread {
    private final String name;
    private final Object lock;
    private final int num;
    private final AtomicInteger counter;


    Bulb(String name, int num, AtomicInteger counter, Object lock) {
        this.name = name;
        this.num = num;
        this.counter = counter;
        this.lock = lock;
    }

    public void run() {
        Thread self = Thread.currentThread();
        while (!self.isInterrupted()) {
            synchronized (lock) {
                try {
                    while (counter.get() % Main.AMOUNT != num) {
                        lock.wait();
                    }
                    System.out.println(name + " bulb is on");
                    sleep(1000);
                } catch (InterruptedException e) {
                    self.interrupt();
                }
                System.out.println(name + " bulb is off");
                counter.incrementAndGet();
                lock.notifyAll();
            }
        }
    }
}