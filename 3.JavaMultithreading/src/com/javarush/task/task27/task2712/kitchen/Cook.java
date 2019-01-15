package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;

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
        Order order = (Order) arg;
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
        setChanged();
        notifyObservers(arg);

    }
}
