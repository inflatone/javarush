package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConsoleHelper {
    private final static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws IOException {
        return reader.readLine();
    }

    public static List<Dish> getAllDishesForOrder() throws IOException {
        List<Dish> dishesForOder = new ArrayList<>();
        writeMessage(String.format("Check dishes: %s", Dish.allDishesToString()));
        for (
                String input = readString();
                !"exit".equals(input);
                input = readString()) {
            Dish selectedDish = getDishByName(input);
            if (selectedDish != null) {
                dishesForOder.add(selectedDish);
            } else {
                writeMessage("Error. The dish you're trying to order is not found. Try again.");
            }
        }
        return dishesForOder;
    }

    private static Dish getDishByName(String dishName) {
        Dish result = null;
        for (Dish dish : Dish.values()) {
            if (dish.name().equals(dishName)) {
                result = dish;
                break;
            }
        }
        return result;
    }
}
