package com.javarush.task.task31.task3109;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/* 
Читаем конфиги
*/
public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        Properties properties = solution.getProperties("4.JavaCollections/src/com/javarush/task/task31/task3109/properties.xml");
        properties.list(System.out);

        properties = solution.getProperties("4.JavaCollections/src/com/javarush/task/task31/task3109/properties.txt");
        properties.list(System.out);

        properties = solution.getProperties("4.JavaCollections/src/com/javarush/task/task31/task3109/notExists");
        properties.list(System.out);

    }

    public Properties getProperties(String fileName) {
        Properties result = new Properties();
        try {
            InputStream is = new FileInputStream(fileName);
            if (fileName.endsWith(".xml")) {
                result.loadFromXML(is);
            } else {
                result.load(is);
            }
        } catch (IOException ignored) {
        }
        return result;
    }
}
