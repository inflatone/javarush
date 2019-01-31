package com.javarush.task.task39.task3913;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

class LogParserHelper {
    private final static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");


    static int getEventIndex(Event event, String eventString) {
        return event == Event.SOLVE_TASK || event == Event.DONE_TASK
                ? Integer.parseInt(eventString.split(" ")[1])
                : -1;
    }

    static Date getDate(String dateString) {
        Date result = null;
        try {
            result = FORMATTER.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    static Event chooseEvent(String eventString) {
        return chooseEnumValue(Event.values(), eventString);
    }

    static Status chooseStatus(String statusString) {
        return chooseEnumValue(Status.values(), statusString);
    }

    static <T extends Enum> T chooseEnumValue(T[] values, String representation) {
        return representation == null ? null : Arrays.stream(values)
                .filter(name -> representation.startsWith(name.toString()))
                .findAny()
                .orElse(null);
    }
}