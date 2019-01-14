package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.AdvertisementManager;
import com.javarush.task.task27.task2712.ad.NoVideoAvailableException;
import com.javarush.task.task27.task2712.kitchen.Order;

import java.io.IOException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tablet extends Observable {
    private static Logger logger = Logger.getLogger(Tablet.class.getName());
    final int number;

    public Tablet(int number) {
        this.number = number;
    }

    public Order createOrder() {
        Order result = null;
        try {
            result = new Order(this);
            ConsoleHelper.writeMessage(result.toString());
            if (!result.isEmpty()) {
                setChanged();
                notifyObservers(result);
                new AdvertisementManager(result.getTotalCookingTime() * 60)
                        .processVideos();
            }
            int x = 1;
        } catch (IOException e) {
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
}
