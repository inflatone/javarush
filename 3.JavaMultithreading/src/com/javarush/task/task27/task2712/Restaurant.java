package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.kitchen.Waiter;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private final static int ORDER_CREATING_INTERVAL = 100;

    public static void main(String[] args) {
        Waiter waiter = new Waiter();
        Cook cookA = new Cook("Amigo");
        Cook cookB = new Cook("Armando");
        cookA.addObserver(waiter);
        cookB.addObserver(waiter);
        StatisticManager.getInstance().register(cookA);
        StatisticManager.getInstance().register(cookB);
        List<Tablet> tablets = new ArrayList<>();
        OrderManager orderManager = new OrderManager();
        for (int i = 0; i < 5; i++) {
            Tablet tablet = new Tablet(i);
            tablet.addObserver(orderManager);
            tablet.addObserver(orderManager);
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
