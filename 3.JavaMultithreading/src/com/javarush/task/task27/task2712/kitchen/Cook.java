package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;

import java.util.Observable;
import java.util.Observer;

public class Cook extends Observable implements Observer {
    private final String name;

    public Cook(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public void update(Observable o, Object arg) {
        ConsoleHelper.writeMessage(
                String.format(
                        "Start cooking - %s, cooking time %smin",
                        arg,
                        ((Order) arg).getTotalCookingTime())
        );
        setChanged();
        notifyObservers(arg);
    }
}
