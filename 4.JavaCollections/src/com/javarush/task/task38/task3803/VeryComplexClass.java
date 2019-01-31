package com.javarush.task.task38.task3803;

/* 
Runtime исключения (unchecked exception)
*/

public class VeryComplexClass {
    public void methodThrowsClassCastException() {
        Integer[] copy = (Integer[]) getIntegerArray();
    }

    public void methodThrowsNullPointerException() {
        Integer[] array = new Integer[5];
        for (int i = 0; i < 4; i++) {
            array[i] = i;
        }
        System.out.println(array[4] / 2);
    }

    public static void main(String[] args) {

    }

    private Object getIntegerArray() {
        return new int[10];
    }
}
