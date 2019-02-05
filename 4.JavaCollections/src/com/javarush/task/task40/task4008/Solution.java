package com.javarush.task.task40.task4008;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

/* 
Работа с Java 8 DateTime API
*/

public class Solution {
    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d.M.y");
    private final static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:m:s");

    public static void main(String[] args) {
        printDate("9.10.2017 5:56:45");
        System.out.println();
        printDate("21.4.2014");
        System.out.println();
        printDate("17:33:40");
    }

    public static void printDate(String date) {
        //напишите тут ваш код
        try {
            for (String part : date.split(" ")) {
                printPart(part);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void printPart(String date) throws ParseException {
        if (date.indexOf('.') != -1) {
            printDatePart(date);
        } else if (date.indexOf(':') != -1) {
            printTimePart(date);
        }
    }

    private static void printDatePart(String date) throws ParseException {
        LocalDate instance = LocalDate.parse(date, DATE_FORMAT);
        System.out.printf("День: %s%n", instance.getDayOfMonth());
        System.out.printf("День недели: %s%n", instance.getDayOfWeek().getValue());
        System.out.printf("День месяца: %s%n", instance.getDayOfMonth());
        System.out.printf("День года: %s%n", instance.getDayOfYear());
        System.out.printf("Неделя месяца: %s%n", instance.get(ChronoField.ALIGNED_WEEK_OF_MONTH));
        System.out.printf("Неделя года: %s%n", instance.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
        System.out.printf("Месяц: %s%n", instance.getMonthValue());
        System.out.printf("Год: %s%n", instance.getYear());
    }

    private static void printTimePart(String time) throws ParseException {
        LocalTime instance = LocalTime.parse(time, TIME_FORMAT);
        System.out.printf("AM или PM: %s%n", instance.get(ChronoField.AMPM_OF_DAY) == 0 ? "AM" : "PM");
        System.out.printf("Часы: %s%n", instance.get(ChronoField.HOUR_OF_AMPM));
        System.out.printf("Часы дня: %s%n", instance.getHour());
        System.out.printf("Минуты: %s%n", instance.getMinute());
        System.out.printf("Секунды: %s%n", instance.getSecond());
    }
}
