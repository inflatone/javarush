package com.javarush.task.task30.task3002;

/* 
Осваиваем методы класса Integer
*/
public class Solution {

    public static void main(String[] args) {
        System.out.println(convertToDecimalSystem("0x16")); //22
        System.out.println(convertToDecimalSystem("012"));  //10
        System.out.println(convertToDecimalSystem("0b10")); //2
        System.out.println(convertToDecimalSystem("62"));   //62
    }

    public static String convertToDecimalSystem(String s) {
        //напишите тут ваш код
        int radix = defineRadix(s);
        return Integer.toString(
                Integer.parseInt(
                        s.substring(radix == 10 ? 0 : radix == 8 ? 1 : 2),
                        radix
                )
        );
    }

    private static int defineRadix(String s) {
        int result = 10;
        if (s.charAt(0) == '0') {
            char second = s.charAt(1);
            if (second == 'x' || second == 'X') {
                result = 16;
            } else {
                result = second == 'b' || second == 'B' ? 2 : 8;
            }
        }
       return result;
    }
}
