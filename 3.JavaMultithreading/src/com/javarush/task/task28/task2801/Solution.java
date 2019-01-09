package com.javarush.task.task28.task2801;

/* 
Осваиваем switch
*/
public class Solution {
    public static void main(String[] args) {
        Solution.switchTest(E1.C);
        Solution.switchTest(E3.D);
        Solution.switchTest(E2.D);
        /* output
        it's E1.C
        undefined
        it's E2.D
         */
    }

    public static void switchTest(Enum obj) {
        StringBuilder buffer = new StringBuilder();
        switch (obj.getClass().getSimpleName()) {
            case "E1":
                buffer.append("it's E1.").append(obj.name());
                break;
            case "E2":
                buffer.append("it's E2.").append(obj.name());
                break;
            default:
                buffer.append("undefined");
        }
        System.out.println(buffer.toString());
        //add your code here
    }

    public static enum E1 {A, B, C, Y}

    public static enum E2 {D, E, F}

    public static enum E3 {D, E, F}
}
