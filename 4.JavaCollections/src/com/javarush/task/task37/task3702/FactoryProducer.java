package com.javarush.task.task37.task3702;

import com.javarush.task.task37.task3702.female.FemaleFactory;
import com.javarush.task.task37.task3702.male.MaleFactory;

public class FactoryProducer {
    public static AbstractFactory getFactory(HumanFactoryType type) {
        AbstractFactory result;
        if (type == HumanFactoryType.MALE) {
            result = new MaleFactory();
        } else {
            result = new FemaleFactory();
        }
        return result;
    }




    public enum HumanFactoryType {
        MALE,
        FEMALE
    }
}
