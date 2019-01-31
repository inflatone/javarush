package com.javarush.task.task39.task3913;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class LogStorage {
    private final List<LogEvent> logEvents = new ArrayList<>();
    private Path logDir;

    LogStorage(Path logDir) {
        this.logDir = logDir;
        initLogs();
    }

    Map<Integer, Integer> getTasksStatsByEvent(Date after, Date before, Event event) {
        return getFilteredLogEventStream(
                after, before, log -> log.event == event
        ).collect(
                Collectors.groupingBy(
                        log -> log.eventTask,
                        Collectors.summingInt(log -> 1)
                )
        );
    }

    Stream<LogEvent> getFilteredLogEventStream(Date after, Date before, Predicate<LogEvent> additionalFilter) {
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

    Date getUserFirstTimeDate(Date after, Date before, String user, Event event, Integer eventTask) {
        return getFilteredAndMappedStream(
                after, before,
                getFilterByUserAndEvent(user, event, eventTask),
                log -> log.date
        ).min(Date::compareTo)
                .orElse(null);
    }

    Set<String> getFilteredIPSet(Date after, Date before, Predicate<LogEvent> filter) {
        return getFilteredAndMappedLogEventSet(after, before, filter, log -> log.ip);
    }

    Set<String> getFilteredUserSet(Date after, Date before, Predicate<LogEvent> filter) {
        return getFilteredAndMappedLogEventSet(after, before, filter, log -> log.name);
    }

    Set<Date> getFilteredDateSet(Date after, Date before, Predicate<LogEvent> filter) {
        return getFilteredAndMappedLogEventSet(after, before, filter, log -> log.date);
    }

    Set<String> getEventFilteredUserSet(Date after, Date before, Event event) {
        return getFilteredUserSet(after, before, log -> log.event == event);
    }

    Set<String> getEventFilteredUserSet(Date after, Date before, Event event, int eventTask) {
        return getFilteredUserSet(after, before, log -> log.event == event && log.eventTask == eventTask);
    }

    <T> Set<T> getFilteredAndMappedLogEventSet(
            Date after, Date before, Predicate<LogEvent> filter, Function<LogEvent, T> mapper
    ) {
        return getFilteredAndMappedStream(after, before, filter, mapper)
                .collect(Collectors.toSet());
    }

    int getEventFilteredLogsCount(Date after, Date before, Event event, int eventTask) {
        return (int) getFilteredLogEventStream(
                after, before, log -> log.event == event && log.eventTask == eventTask
        ).count();
    }

    private Predicate<LogEvent> getFilterByUserAndEvent(String user, Event event, Integer eventTask) {
        Predicate<LogEvent> result = log -> log.event == event && log.name.equals(user);
        if (eventTask != null) {
            result = result.and(log -> log.eventTask == eventTask);
        }
        return result;
    }

    private <T> Stream<T> getFilteredAndMappedStream(
            Date after, Date before, Predicate<LogEvent> filter, Function<LogEvent, T> mapper
    ) {
        return getFilteredLogEventStream(after, before, filter)
                .map(mapper);
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

    static class LogEvent {
        String ip;
        String name;
        Date date;
        Event event;
        int eventTask;
        Status status;

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
