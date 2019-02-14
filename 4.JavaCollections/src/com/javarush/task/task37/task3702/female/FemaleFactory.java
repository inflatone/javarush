package com.javarush.task.task37.task3702.female;

import com.javarush.task.task37.task3702.Human;

public class FemaleFactory {
    public Human getPerson(int age) {
        Human result;
        if (age <= KidGirl.MAX_AGE) {
            result = new KidGirl();
        } else if (age <= TeenGirl.MAX_AGE) {
            result = new TeenGirl();
        } else {
            result = new Woman();
        }
        return result;
    }
}
