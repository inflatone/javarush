package com.javarush.task.task26.task2601;

import java.util.Arrays;
import java.util.Comparator;

/*
Почитать в инете про медиану выборки
*/
public class Solution {

    public static void main(String[] args) {
    }

    public static Integer[] sort(Integer[] array) {

        Arrays.sort(array);
        int size = array.length;
        double median = size % 2 != 0
                ? array[size / 2]
                : (array[size / 2 - 1] + array[size / 2]) / 2.0;
        Arrays.sort(
                array,
                Comparator.comparingDouble(e -> Math.abs(e - median))
        );
        //implement logic here
        return array;
    }
}
