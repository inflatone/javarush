package com.javarush.task.task38.task3812;

/* 
Обработка аннотаций
*/

public class Solution {
    public static void main(String[] args) {
        printFullyQualifiedNames(Solution.class);
        printFullyQualifiedNames(SomeTest.class);

        printValues(Solution.class);
        printValues(SomeTest.class);
    }

    public static boolean printFullyQualifiedNames(Class c) {
        boolean result = false;
        if (c.isAnnotationPresent(PrepareMyTest.class)) {
            result = true;
            PrepareMyTest test = (PrepareMyTest) c.getAnnotation(PrepareMyTest.class);
            for (String name : test.fullyQualifiedNames()) {
                System.out.println(name);
            }
        }

        return result;
    }

    public static boolean printValues(Class c) {
        boolean result = false;
        if (c.isAnnotationPresent(PrepareMyTest.class)) {
            result = true;
            PrepareMyTest test = (PrepareMyTest) c.getAnnotation(PrepareMyTest.class);
            for (Class value : test.value()) {
                System.out.println(value.getSimpleName());
            }
        }
        return result;
    }
}
