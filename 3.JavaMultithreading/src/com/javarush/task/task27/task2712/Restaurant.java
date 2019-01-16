package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.Waiter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Restaurant {
    private final static int ORDER_CREATING_INTERVAL = 100;
    private final static LinkedBlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        Waiter waiter = new Waiter();
        Cook cookA = new Cook("Amigo");
        Cook cookB = new Cook("Armando");
        cookA.setQueue(orderQueue);
        cookB.setQueue(orderQueue);
        cookA.addObserver(waiter);
        cookB.addObserver(waiter);
        List<Tablet> tablets = new ArrayList<>();
        Thread cookThreadA = new Thread(cookA);
        Thread cookThreadB = new Thread(cookB);
        cookThreadA.setDaemon(true);
        cookThreadB.setDaemon(true);
        cookThreadA.start();
        cookThreadB.start();

        for (int i = 0; i < 5; i++) {
            Tablet tablet = new Tablet(i);
            tablet.setQueue(orderQueue);
            tablets.add(tablet);
        }
        Thread generator = new Thread(new RandomOrderGeneratorTask(tablets, ORDER_CREATING_INTERVAL));
        generator.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
        generator.interrupt();
        DirectorTablet director = new DirectorTablet();
        director.printAdvertisementProfit();
        director.printCookWorkloading();
        director.printActiveVideoSet();
        director.printArchivedVideoSet();

    }
}
