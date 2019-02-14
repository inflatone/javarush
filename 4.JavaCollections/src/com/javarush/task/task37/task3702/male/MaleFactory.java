package com.javarush.task.task37.task3702.male;

import com.javarush.task.task37.task3702.AbstractFactory;
import com.javarush.task.task37.task3702.Human;

public class MaleFactory implements AbstractFactory {

    @Override
    public Human getPerson(int age) {
        Human result;
        if (age <= KidBoy.MAX_AGE) {
            result = new KidBoy();
        } else if (age <= TeenBoy.MAX_AGE) {
            result = new TeenBoy();
        } else {
            result = new Man();
        }
        return result;
    }
}
