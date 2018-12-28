package com.javarush.task.task23.task2309.vo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

/**
 * Последовательно работающие лампочки.
 *
 * @author Alexander Savchenko
 * @version 1.0
 * @since 2018-12-29
 */
public class Main {
    public static void main(String[] args) {
        Runnable first = new Bulb("first");
        Runnable second = new Bulb("second");

        ExecutorService service = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 100; i++) {
            service.submit(first);
            service.submit(second);
        }


    }
}

class Bulb implements Runnable {

    private final String name;



    Bulb(String name) {
        this.name = name;

    }

    public void run() {
        Thread self = currentThread();


        System.out.println(name + " bulb is on");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            self.interrupt();
        }
        System.out.println(name + " bulb is off");


    }
}