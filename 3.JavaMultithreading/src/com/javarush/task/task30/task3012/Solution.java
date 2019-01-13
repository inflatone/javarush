package com.javarush.task.task30.task3012;

/* 
Получи заданное число
*/

public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.createExpression(74);
    }

    public void createExpression(int number) {
        //напишите тут ваш код
        StringBuilder buffer = new StringBuilder();
        buffer.append(number).append(" = ");
        for (int i = 1; number > 0; i *= 3) {
            int mod = number % 3;
            number = number / 3;
            if (mod == 1) {
                buffer.append(" + ").append(i);
            } else if (mod == 2) {
                buffer.append(" - ").append(i);
                number++;
            }
        }
        System.out.println(buffer.toString());

    }
}