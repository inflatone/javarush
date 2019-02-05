package com.javarush.task.task40.task4007;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Solution {
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    
    public static void main(String[] args) {
        printDate("21.4.2014 15:56:45");
        System.out.println();
        printDate("21.4.2014");
        System.out.println();
        printDate("17:33:40");
    }

    public static void printDate(String date) {
        //напишите тут ваш код
        try {
            Calendar calendar = Calendar.getInstance();
            for (String part : date.split(" ")) {
                printDate(part, calendar);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void printDate(String date, Calendar instance) throws ParseException {
        if (date.indexOf('.') != -1) {
            printDatePart(date, instance);
        } else if (date.indexOf(':') != -1) {
            printTimePart(date, instance);
        }
    }

    private static void printDatePart(String date, Calendar instance) throws ParseException {
        instance.setTime(DATE_FORMAT.parse(date));
        System.out.printf("День: %s%n", instance.get(Calendar.DATE));
        System.out.printf("День недели: %s%n", getCorrectDayOfWeek(instance));
        System.out.printf("День месяца: %s%n", instance.get(Calendar.DAY_OF_MONTH));
        System.out.printf("День года: %s%n", instance.get(Calendar.DAY_OF_YEAR));
        System.out.printf("Неделя месяца: %s%n", instance.get(Calendar.WEEK_OF_MONTH));
        System.out.printf("Неделя года: %s%n", instance.get(Calendar.WEEK_OF_YEAR));
        System.out.printf("Месяц: %s%n", instance.get(Calendar.MONTH) + 1);
        System.out.printf("Год: %s%n", instance.get(Calendar.YEAR));
    }

    private static void printTimePart(String time, Calendar instance) throws ParseException {
        instance.setTime(TIME_FORMAT.parse(time));
        System.out.printf("AM или PM: %s%n", instance.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM");
        System.out.printf("Часы: %s%n", instance.get(Calendar.HOUR));
        System.out.printf("Часы дня: %s%n", instance.get(Calendar.HOUR_OF_DAY));
        System.out.printf("Минуты: %s%n", instance.get(Calendar.MINUTE));
        System.out.printf("Секунды: %s%n", instance.get(Calendar.SECOND));
    }

    private static int getCorrectDayOfWeek(Calendar instance) {
        int day = instance.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.SUNDAY ? 7 : day - 1;
    }
}
