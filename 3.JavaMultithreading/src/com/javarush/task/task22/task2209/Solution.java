package com.javarush.task.task22.task2209;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
Составить цепочку слов
*/
public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        //...
        Scanner console = new Scanner(System.in);
        String fileName = console.nextLine();
        console.close();

        Scanner fromFile = new Scanner(new File(fileName));
        StringBuilder result = getLine(
                fromFile.hasNextLine() ? fromFile.nextLine().split(" ") : new String[0]
        );
        fromFile.close();
        System.out.println(result.toString());
    }

    public static StringBuilder getLine(String... words) {
        StringBuilder buffer = new StringBuilder();
        if (words.length < 1) {
            return buffer;
        }
        List<String> list = new ArrayList<>(Arrays.asList(words));

        buffer.append(list.remove(0));

        for (int i = 0; i < list.size(); i++) {
            if (tryMerge(buffer, list.get(i))) {
                list.remove(i);
                i = -1;
            }
        }
        return buffer;
    }

    private static boolean tryMerge(StringBuilder buffer, String word) {
        int size = buffer.length();
        if (Character.toLowerCase(buffer.charAt(0))
                == Character.toLowerCase(word.charAt(word.length() - 1))) {
            buffer.insert(0, ' ').insert(0, word);
        } else if (Character.toLowerCase(word.charAt(0))
                == Character.toLowerCase(buffer.charAt(buffer.length() - 1))) {
            buffer.append(' ').append(word);
        }
        return size != buffer.length();
    }
}
