package com.javarush.task.task39.task3910;

/* 
isPowerOfThree
*/
public class Solution {
    public static void main(String[] args) {
        //true
        System.out.println(isPowerOfThree(27));
        System.out.println(isPowerOfThree(6561));
        System.out.println(isPowerOfThree(3));
        System.out.println(isPowerOfThree(81));
        System.out.println(isPowerOfThree(243));
        System.out.println(isPowerOfThree(1));

        //false
        System.out.println(isPowerOfThree(0));
        System.out.println(isPowerOfThree(20));
        System.out.println(isPowerOfThree(100));
        System.out.println(isPowerOfThree(-27));

    }

    public static boolean isPowerOfThree(int n) {
        if (n % 3 == 0) {
            return n == 3 || n != 0 && isPowerOfThree(n / 3);
        }
        return n == 1;
    }
}
