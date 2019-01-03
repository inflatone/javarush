package com.javarush.task.task25.task2502;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* 
Машину на СТО не повезем!
*/
public class Solution {
    public static enum Wheel {
        FRONT_LEFT,
        FRONT_RIGHT,
        BACK_LEFT,
        BACK_RIGHT
    }

    public static class Car {
        protected List<Wheel> wheels;

        public Car() {
            //init wheels here
            List<Wheel> result = new ArrayList<>();
            List<String> data = new ArrayList<>(Arrays.asList(loadWheelNamesFromDB()));
            for (Wheel wheel : Wheel.values()) {
                if (data.remove(wheel.name())) {
                    result.add(wheel);
                }
            }
            if (result.size() != 4 || !data.isEmpty()) {
                throw new RuntimeException();
            }
            this.wheels = result;
        }

        protected String[] loadWheelNamesFromDB() {
            //this method returns mock data
            return new String[]{"FRONT_LEFT", "FRONT_RIGHT", "BACK_LEFT", "BACK_RIGHT", "BACK_RIGHT"};
        }
    }

    public static void main(String[] args) {
        new Car();
    }
}
