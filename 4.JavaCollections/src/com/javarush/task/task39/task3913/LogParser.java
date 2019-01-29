package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.DateQuery;
import com.javarush.task.task39.task3913.query.EventQuery;
import com.javarush.task.task39.task3913.query.IPQuery;
import com.javarush.task.task39.task3913.query.UserQuery;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery {
    private Path logDir;
    private List<LogEvent> logEvents = new ArrayList<>();

    public LogParser(Path logDir) {
        this.logDir = logDir;
        initLogs();
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
            result = result.filter(event -> event.date.getTime() <= before.getTime());
        }
        if (after != null) {
            result = result.filter(event -> event.date.getTime() >= after.getTime());
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

    private static class LogParserHelper {
        private final static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        private static int getEventIndex(Event event, String eventString) {
            return event == Event.SOLVE_TASK || event == Event.DONE_TASK
                    ? Integer.parseInt(eventString.split(" ")[1])
                    : -1;
        }

        private static Date getDate(String dateString) {
            Date result = null;

            try {
                result = formatter.parse(dateString);
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
            T result = null;
            for (T value : values) {
                if (representation.startsWith(value.name())) {
                    result = value;
                    break;
                }
            }
            return result;
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
}