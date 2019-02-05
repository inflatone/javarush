package com.javarush.task.task40.task4009;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/* 
Buon Compleanno!
*/

public class Solution {
    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d.M.y");

    public static void main(String[] args) {

        System.out.println(weekDayOfBirthday("1.12.2015", "2016"));
    }

    public static String weekDayOfBirthday(String birthday, String year) {
        //напишите тут ваш код
        return LocalDate.parse(birthday, DATE_FORMAT)
                .with(Year.parse(year))
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.ITALIAN);

    }
}
