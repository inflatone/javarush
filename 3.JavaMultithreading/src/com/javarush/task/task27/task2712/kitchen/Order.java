package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private final Tablet tablet;
    protected List<Dish> dishes;

    public Order(Tablet tablet) throws IOException {
        this.tablet = tablet;
        this.dishes = ConsoleHelper.getAllDishesForOrder();
    }

    @Override
    public String toString() {
        return dishes.isEmpty()
                ? ""
                : String
                .format(
                        "Your order: [%s] of Tablet{number=%s}",
                        dishes.stream().map(Enum::name).collect(Collectors.joining(", ")),
                        tablet.toString()
                );
    }
}
