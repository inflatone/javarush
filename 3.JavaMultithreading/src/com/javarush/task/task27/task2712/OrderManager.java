package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderManager implements Observer {

    private final LinkedBlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

    public OrderManager() {
        Thread daemon = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        if (!orderQueue.isEmpty()) {
                            StatisticManager.getInstance().getCooks().forEach(
                                    cook -> {
                                        if (!cook.isBusy() && !orderQueue.isEmpty()) {
                                            Order order = orderQueue.poll();
                                            new Thread(() -> cook.startCookingOrder(order)).start();
                                        }
                                    }
                            );
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        daemon.setDaemon(true);
        daemon.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        orderQueue.add((Order) arg);
    }
}
