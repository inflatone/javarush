package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.IPQuery;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogParser implements IPQuery {
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
        return getFilteredIPSet(after, before, log -> true);
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

    private Set<String> getFilteredIPSet(Date after, Date before, Predicate<LogEvent> predicate) {
        return getFilteredLogEventStream(after, before)
                .filter(predicate)
                .map(LogEvent::getIp).collect(Collectors.toSet());
    }

    private Stream<LogEvent> getFilteredLogEventStream(Date after, Date before) {
        Stream<LogEvent> result = logEvents.stream();
        if (before != null) {
            result = result.filter(event -> event.date.getTime() <= before.getTime());
        }
        if (after != null) {
            result = result.filter(event -> event.date.getTime() >= after.getTime());
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
        Event event = LogParserUtils.chooseEvent(parts[3]);
        Status status = LogParserUtils.chooseStatus(parts[4]);
        Date date = LogParserUtils.getDate(parts[2]);
        int eventSuffix = LogParserUtils.getEventIndex(event, parts[3]);
        if (event != null && status != null && date != null) {
            logEvents.add(new LogEvent(parts[0],
                    parts[1],
                    date,
                    event,
                    status,
                    eventSuffix)
            );
        }
    }

    private static class LogParserUtils {
        private static int getEventIndex(Event event, String eventString) {
            return event == Event.SOLVE_TASK || event == Event.DONE_TASK
                    ? Integer.parseInt(eventString.split(" ")[1])
                    : -1;
        }

        private static Date getDate(String dateString) {
            Date result = null;
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
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
        private int eventIndex;
        private Status status;

        public LogEvent(String ip, String name, Date date, Event event, Status status, int eventIndex) {
            this.ip = ip;
            this.name = name;
            this.date = date;
            this.event = event;
            this.status = status;
            this.eventIndex = eventIndex;
        }

        public String getIp() {
            return ip;
        }
    }

}