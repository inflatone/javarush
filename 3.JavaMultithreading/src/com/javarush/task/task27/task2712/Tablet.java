package com.javarush.task.task27.task2712;

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
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Console is unavailable.");
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
