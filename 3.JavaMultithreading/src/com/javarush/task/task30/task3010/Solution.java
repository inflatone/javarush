package com.javarush.task.task30.task3010;

/* 
Минимальное допустимое основание системы счисления
*/

public class Solution {
    public static void main(String[] args) {
        //напишите тут ваш код
        String result = "incorrect";
        try {
            int radix = -1;
            for (int i = 0; i < args[0].length(); i++) {
                int code = args[0].charAt(i);
                if (code > 47 && code < 58) {
                    radix = Integer.max(radix, code - 47);
                } else if (code > 64 && code < 91) {
                    radix = Integer.max(radix, code - 65 + 11);
                } else if (code > 96 && code < 123) {
                    radix = Integer.max(radix, code - 97 + 11);
                } else {
                    radix = -1;
                    break;
                }
            }
            result = radix == -1 ? result : String.valueOf(Integer.max(2, radix));

        } catch (Exception ignored) {
        }
        System.out.println(result);
    }

}