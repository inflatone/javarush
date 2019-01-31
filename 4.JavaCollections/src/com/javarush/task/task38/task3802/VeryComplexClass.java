package com.javarush.task.task38.task3802;

/* 
Проверяемые исключения (checked exception)
*/

import java.io.FileInputStream;
import java.io.InputStream;

public class VeryComplexClass {
    public void veryComplexMethod() throws Exception {
        //напишите тут ваш код
        InputStream in = new FileInputStream("");
        in.close();
    }

    public static void main(String[] args) {

    }
}
