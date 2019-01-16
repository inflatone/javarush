package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestOrder extends Order {
    private final static Random RANDOM = new Random();

    public TestOrder(Tablet tablet) throws IOException {
        super(tablet);
    }

    @Override
    protected void initDishes() throws IOException {
        Dish[] dishes = Dish.values();
        this.dishes = IntStream.range(0, RANDOM.nextInt(5))
                .mapToObj(i -> Dish.values()[RANDOM.nextInt(dishes.length)])
                .collect(Collectors.toList());


    }



}
