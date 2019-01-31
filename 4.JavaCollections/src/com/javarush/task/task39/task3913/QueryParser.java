package com.javarush.task.task39.task3913;


import java.util.Date;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//get field
//get field1 for field2 = "value1"
//get field1 for field2 = "value1" and field3 = "value2"
//get field for date between = "after" and "before"
//get field1 for field2 = "value1" and date between = "after" and "before"
//get field1 for field2 = "value1" and field3 = "value2" and date between = "after" and "before"
class QueryParser {
    private final static String GET_QUERY = String.format("(?<get>%s)", Query.VALUES_REGEX_PART);
    private final static String DATE_BETWEEN_QUERY = "date between \"(?<after>.*?)\" and \"(?<before>.*?)\"";

    private final static Pattern PATTERN = Pattern.compile(
            String.format(
                    "get %s( for %s( and %s)?)?( (?:and|for) %s)?",
                    GET_QUERY, getForQuery(1), getForQuery(2), DATE_BETWEEN_QUERY
            )
    );

    private static String getForQuery(int num) {
        return String.format("(?<for%s>%s) = \"(?<value%1$s>.*?)\"", num, Query.VALUES_REGEX_PART);
    }

    private final Matcher matcher;

    private Query getPart;
    private Predicate<LogStorage.LogEvent> filter;
    private Date after;
    private Date before;

    Query getGetPart() {
        return getPart;
    }

    Predicate<LogStorage.LogEvent> getFilter() {
        return filter;
    }

    Date getAfter() {
        return after;
    }

    Date getBefore() {
        return before;
    }

    QueryParser(String line) {
        matcher = PATTERN.matcher(line);
        initCheck();
    }

    private void initCheck() {
        if (matcher.find()) {
            getPart = parseGetPart();
            filter = composeFilter(null, 1, 2);
            after = parseAfterPart();
            before = parseBeforePart();
        }
    }

    private Query parseGetPart() {
        return LogParserHelper.chooseEnumValue(
                Query.values(),
                matcher.group("get")
        );
    }

    private Predicate<LogStorage.LogEvent> composeFilter(Predicate<LogStorage.LogEvent> currentFilter , int num, int amount) {
        Predicate<LogStorage.LogEvent> newPart = getNextPartFilter(num);
        currentFilter = currentFilter == null
                ? newPart
                : newPart == null ? currentFilter : currentFilter.and(newPart);
        return currentFilter == null || num == amount
                ? currentFilter
                : composeFilter(currentFilter, num + 1, amount);
    }

    private Date parseBeforePart() {
        return getDate("before");
    }

    private Date parseAfterPart() {
        return getDate("after");
    }

    private Predicate<LogStorage.LogEvent> getNextPartFilter(int num) {
        Query query = parseForPart(num);
        String value = parseValuePart(num);
        return query != null && value != null
                ? log -> query.validate(log, value)
                : null;
    }

    private Query parseForPart(int num) {
        return LogParserHelper.chooseEnumValue(
                Query.values(),
                matcher.group("for" + num)
        );
    }

    private String parseValuePart(int num) {
        return matcher.group("value" + num);
    }

    private Date getDate(String groupName) {
        String date = matcher.group(groupName);
        return date != null ? LogParserHelper.getDate(date) : null;
    }
}