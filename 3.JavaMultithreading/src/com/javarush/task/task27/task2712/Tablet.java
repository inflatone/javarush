package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.AdvertisementManager;
import com.javarush.task.task27.task2712.ad.NoVideoAvailableException;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.TestOrder;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tablet {
    private static Logger logger = Logger.getLogger(Tablet.class.getName());
    final int number;
    private LinkedBlockingQueue<Order> queue;

    public Tablet(int number) {
        this.number = number;
    }

    public Order createOrder() {
        return initOrder(false);
    }

    public void createTestOrder() {
        initOrder(true);
    }

    private Order initOrder(boolean shouldBeMock) {
        Order result = null;
        try {
            result = shouldBeMock ? new TestOrder(this) : new Order(this);
            if (!result.isEmpty()) {
                queue.add(result);
                new AdvertisementManager(result.getTotalCookingTime() * 60)
                        .processVideos();
            }
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "Console is unavailable.");
        } catch (NoVideoAvailableException nvae) {
            logger.log(Level.INFO, "No video is available for the order " + result);
        }
        return result;
    }



    @Override
    public String toString() {
        return "Tablet{" +
                "number=" + number +
                '}';
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }
}
