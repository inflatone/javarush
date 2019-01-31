package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery, QLQuery {
    private final Path logDir;
    private final List<LogEvent> logEvents = new ArrayList<>();

    public LogParser(Path logDir) {
        this.logDir = logDir;
        initLogs();
    }

    @Override
    public Set<Object> execute(String query) {
        return new Executor(query).executeQuery();
    }

    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        return getUniqueIPs(after, before).size();
    }

    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        return getFilteredIPSet(after, before, null);
    }

    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        return getFilteredIPSet(after, before, log -> log.name.equals(user));
    }

    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        return getFilteredIPSet(after, before, log -> log.event == event);
    }

    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        return getFilteredIPSet(after, before, log -> log.status == status);
    }

    @Override
    public Set<String> getAllUsers() {
        return getFilteredUserSet(null, null, null);
    }

    @Override
    public int getNumberOfUsers(Date after, Date before) {
        return getFilteredUserSet(
                after, before, null
        ).size();
    }

    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        return getFilteredAndMappedLogEventSet(
                after, before, log -> log.name.equals(user), log -> log.event
        ).size();
    }

    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        return getFilteredUserSet(after, before, log -> log.ip.equals(ip));
    }

    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        return getEventFilteredUserSet(after, before, Event.LOGIN);
    }

    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        return getEventFilteredUserSet(after, before, Event.DOWNLOAD_PLUGIN);
    }

    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        return getEventFilteredUserSet(after, before, Event.WRITE_MESSAGE);
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        return getEventFilteredUserSet(after, before, Event.SOLVE_TASK);
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        return getEventFilteredUserSet(after, before, Event.SOLVE_TASK, task);
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        return getEventFilteredUserSet(after, before, Event.DONE_TASK);
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {
        return getEventFilteredUserSet(after, before, Event.DONE_TASK, task);
    }

    @Override
    public Set<Date> getDatesForUserAndEvent(String user, Event event, Date after, Date before) {
        return getFilteredDateSet(after, before, log -> log.name.equals(user) && log.event == event);
    }

    @Override
    public Set<Date> getDatesWhenSomethingFailed(Date after, Date before) {
        return getFilteredDateSet(after, before, log -> log.status == Status.FAILED);
    }

    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {
        return getFilteredDateSet(after, before, log -> log.status == Status.ERROR);
    }

    @Override
    public Date getDateWhenUserLoggedFirstTime(String user, Date after, Date before) {
        return getUserFirstTimeDate(after, before, user, Event.LOGIN, null);
    }

    @Override
    public Date getDateWhenUserSolvedTask(String user, int task, Date after, Date before) {
        return getUserFirstTimeDate(after, before, user, Event.SOLVE_TASK, task);
    }

    @Override
    public Date getDateWhenUserDoneTask(String user, int task, Date after, Date before) {
        return getUserFirstTimeDate(after, before, user, Event.DONE_TASK, task);
    }

    @Override
    public Set<Date> getDatesWhenUserWroteMessage(String user, Date after, Date before) {
        return getDatesForUserAndEvent(user, Event.WRITE_MESSAGE, after, before);
    }

    @Override
    public Set<Date> getDatesWhenUserDownloadedPlugin(String user, Date after, Date before) {
        return getDatesForUserAndEvent(user, Event.DOWNLOAD_PLUGIN, after, before);
    }

    @Override
    public int getNumberOfAllEvents(Date after, Date before) {
        return getAllEvents(after, before).size();
    }

    @Override
    public Set<Event> getAllEvents(Date after, Date before) {
        return getFilteredAndMappedLogEventSet(
                after, before, null, log -> log.event
        );
    }

    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {
        return getFilteredAndMappedLogEventSet(
                after, before, log -> log.ip.equals(ip), log -> log.event
        );
    }

    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {
        return getFilteredAndMappedLogEventSet(
                after, before, log -> log.name.equals(user), log -> log.event
        );
    }

    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {
        return getFilteredAndMappedLogEventSet(
                after, before, log -> log.status == Status.FAILED, log -> log.event
        );
    }

    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {
        return getFilteredAndMappedLogEventSet(
                after, before, log -> log.status == Status.ERROR, log -> log.event
        );
    }

    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {
        return getEventFilteredLogsCount(after, before, Event.SOLVE_TASK, task);
    }

    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {
        return getEventFilteredLogsCount(after, before, Event.DONE_TASK, task);
    }

    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        return getTasksStatsByEvent(after, before, Event.SOLVE_TASK);
    }

    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {
        return getTasksStatsByEvent(after, before, Event.DONE_TASK);
    }

    private int getEventFilteredLogsCount(Date after, Date before, Event event, int eventTask) {
        return (int) getFilteredLogEventStream(
                after, before, log -> log.event == event && log.eventTask == eventTask
        ).count();
    }

    private Map<Integer, Integer> getTasksStatsByEvent(Date after, Date before, Event event) {
        return getFilteredLogEventStream(
                after, before, log -> log.event == event
        ).collect(
                Collectors.groupingBy(
                        log -> log.eventTask,
                        Collectors.summingInt(log -> 1)
                )
        );
    }

    private Date getUserFirstTimeDate(Date after, Date before, String user, Event event, Integer eventTask) {
        return getFilteredAndMappedStream(
                after, before,
                getFilterByUserAndEvent(user, event, eventTask),
                log -> log.date
        ).min(Date::compareTo)
                .orElse(null);
    }

    private Predicate<LogEvent> getFilterByUserAndEvent(String user, Event event, Integer eventTask) {
        Predicate<LogEvent> result = log -> log.event == event && log.name.equals(user);
        if (eventTask != null) {
            result = result.and(log -> log.eventTask == eventTask);
        }
        return result;
    }

    private Set<String> getFilteredIPSet(Date after, Date before, Predicate<LogEvent> filter) {
        return getFilteredAndMappedLogEventSet(after, before, filter, log -> log.ip);
    }

    private Set<String> getFilteredUserSet(Date after, Date before, Predicate<LogEvent> filter) {
        return getFilteredAndMappedLogEventSet(after, before, filter, log -> log.name);
    }

    private Set<Date> getFilteredDateSet(Date after, Date before, Predicate<LogEvent> filter) {
        return getFilteredAndMappedLogEventSet(after, before, filter, log -> log.date);
    }

    private Set<String> getEventFilteredUserSet(Date after, Date before, Event event) {
        return getFilteredUserSet(after, before, log -> log.event == event);
    }

    private Set<String> getEventFilteredUserSet(Date after, Date before, Event event, int eventTask) {
        return getFilteredUserSet(after, before, log -> log.event == event && log.eventTask == eventTask);
    }

    private <T> Set<T> getFilteredAndMappedLogEventSet(
            Date after, Date before, Predicate<LogEvent> filter, Function<LogEvent, T> mapper
    ) {
        return getFilteredAndMappedStream(after, before, filter, mapper)
                .collect(Collectors.toSet());
    }

    private <T> Stream<T> getFilteredAndMappedStream(
            Date after, Date before, Predicate<LogEvent> filter, Function<LogEvent, T> mapper
    ) {
        return getFilteredLogEventStream(after, before, filter)
                .map(mapper);
    }

    private Stream<LogEvent> getFilteredLogEventStream(Date after, Date before, Predicate<LogEvent> additionalFilter) {
        Stream<LogEvent> result = logEvents.stream();
        if (before != null) {
            result = result.filter(event -> event.date.before(before));
        }
        if (after != null) {
            result = result.filter(event -> event.date.after(after));
        }
        if (additionalFilter != null) {
            result = result.filter(additionalFilter);
        }
        return result;
    }

    private void initLogs() {
        try (DirectoryStream<Path> files = Files.newDirectoryStream(logDir, "*.log")) {
            files.forEach(this::initFromSingleFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void initFromSingleFile(Path file) {
        try (Stream<String> lines = Files.lines(file)) {
            lines.forEach(this::addLogEvent);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void addLogEvent(String line) {


        String[] parts = line.split("\t");
        Event event = LogParserHelper.chooseEvent(parts[3]);
        Status status = LogParserHelper.chooseStatus(parts[4]);
        Date date = LogParserHelper.getDate(parts[2]);
        int eventTask = LogParserHelper.getEventIndex(event, parts[3]);
        if (event != null && status != null && date != null) {
            logEvents.add(
                    new LogEvent(parts[0], parts[1], date, event, status, eventTask)
            );
        }
    }

    private enum Query {
        IP("ip", Function.identity(), log -> log.ip),
        USER("user", Function.identity(), log -> log.name),
        DATE("date", LogParserHelper::getDate, log -> log.date),
        EVENT("event", LogParserHelper::chooseEvent, log -> log.event),
        STATUS("status", LogParserHelper::chooseStatus, log -> log.status);

        private static final String VALUES_REGEX_PART;

        static {
            VALUES_REGEX_PART = Arrays.stream(values())
                    .map(v -> v.name)
                    .collect(Collectors.joining("|"));
        }

        private final String name;
        private final Function<String, ?> stringMapper;
        private final Function<LogEvent, ?> fieldMapper;

        Query(String name, Function<String, ?> stringMapper, Function<LogEvent, ?> fieldMapper) {
            this.name = name;
            this.stringMapper = stringMapper;
            this.fieldMapper = fieldMapper;
        }

        private boolean validate(LogEvent log, String temp) {
            return fieldMapper.apply(log).equals(
                    stringMapper.apply(temp)
            );
        }

        @SuppressWarnings("unchecked")
        private <T> Function<LogEvent, T> fieldMapper() {
            return (Function<LogEvent, T>) fieldMapper;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    //get field
    //get field1 for field2 = "value1"
    //get field1 for field2 = "value1" and field3 = "value2"
    //get field for date between = "after" and "before"
    //get field1 for field2 = "value1" and date between = "after" and "before"
    //get field1 for field2 = "value1" and field3 = "value2" and date between = "after" and "before"
    private static class QueryParser {
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
        private Predicate<LogEvent> filter;
        private Date after;
        private Date before;

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

        private Query parseForPart(int num) {
            return LogParserHelper.chooseEnumValue(
                    Query.values(),
                    matcher.group("for" + num)
            );
        }

        private Predicate<LogEvent> composeFilter(Predicate<LogEvent> currentFilter , int num, int amount) {
            Predicate<LogEvent> newPart = getNextPartFilter(num);
            currentFilter = currentFilter == null
                    ? newPart
                    : newPart == null ? currentFilter : currentFilter.and(newPart);
           return currentFilter == null || num == amount
                   ? currentFilter
                   : composeFilter(currentFilter, num + 1, amount);
        }

        private Predicate<LogEvent> getNextPartFilter(int num) {
            Query query = parseForPart(num);
            String value = parseValuePart(num);
            return query != null && value != null
                    ? log -> query.validate(log, value)
                    : null;
        }

        private String parseValuePart(int num) {
            return matcher.group("value" + num);
        }

        private Date parseBeforePart() {
            return getDate("before");
        }

        private Date parseAfterPart() {
            return getDate("after");
        }

        private Date getDate(String groupName) {
            String date = matcher.group(groupName);
            return date != null ? LogParserHelper.getDate(date) : null;
        }

    }

    private static class LogParserHelper {
        private final static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");


        private static int getEventIndex(Event event, String eventString) {
            return event == Event.SOLVE_TASK || event == Event.DONE_TASK
                    ? Integer.parseInt(eventString.split(" ")[1])
                    : -1;
        }

        private static Date getDate(String dateString) {
            Date result = null;
            try {
                result = FORMATTER.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return result;
        }

        private static Event chooseEvent(String eventString) {
            return chooseEnumValue(Event.values(), eventString);
        }

        private static Status chooseStatus(String statusString) {
            return chooseEnumValue(Status.values(), statusString);
        }

        private static <T extends Enum> T chooseEnumValue(T[] values, String representation) {
            return representation == null ? null : Arrays.stream(values)
                    .filter(name -> representation.startsWith(name.toString()))
                    .findAny()
                    .orElse(null);
        }
    }

    private static class LogEvent {
        private String ip;
        private String name;
        private Date date;
        private Event event;
        private int eventTask;
        private Status status;

        LogEvent(String ip, String name, Date date, Event event, Status status, int eventTask) {
            this.ip = ip;
            this.name = name;
            this.date = date;
            this.event = event;
            this.status = status;
            this.eventTask = eventTask;
        }
    }

    private class Executor {
        private final Map<Query, Function<QueryParser, Set<Object>>> commands;
        private String query;

        {
            commands = Arrays.stream(Query.values())
                    .collect(Collectors.toMap(
                            Function.identity(),
                            query ->
                                    p -> getSet(query.fieldMapper(), p)
                    ));
        }

        Executor(String query) {
            this.query = query;
        }

        Set<Object> executeQuery() {
            QueryParser parser = new QueryParser(query);
            return commands.getOrDefault(
                    parser.getPart,
                    p -> Collections.emptySet()
            ).apply(parser);
        }

        private <T> Set<T> getSet(Function<LogEvent, T> mapper, QueryParser queryParser) {
            return getFilteredStream(queryParser)
                    .map(mapper)
                    .collect(Collectors.toSet());
        }

        private Stream<LogEvent> getFilteredStream(QueryParser parser) {
            return getFilteredLogEventStream(parser.after, parser.before, parser.filter);
        }
    }
}