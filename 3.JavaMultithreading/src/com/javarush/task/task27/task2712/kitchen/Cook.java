package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;

import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

public class Cook extends Observable implements Runnable {
    private final String name;
    private boolean busy;
    private LinkedBlockingQueue<Order> queue;

    public Cook(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Order order = queue.poll();
            if (order != null) {
                startCookingOrder(order);
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void startCookingOrder(Order order) {
        busy = true;
        StatisticManager.getInstance().register(new CookedOrderEventDataRow(
                order.getTablet().toString(),
                name,
                order.getTotalCookingTime() * 60,
                order.getDishes()
        ));
        ConsoleHelper.writeMessage(
                String.format(
                        "Start cooking - %s, cooking time %smin",
                        order,
                        order.getTotalCookingTime())
        );
        try {
            Thread.sleep(order.getTotalCookingTime() * 10);
        } catch (InterruptedException ignored) {
        }
        setChanged();
        notifyObservers(order);
        busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }
}
