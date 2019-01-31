package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.*;

import java.nio.file.Path;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery, QLQuery {
    private final LogStorage storage;
    private final QueryExecutor executor;

    public LogParser(Path logDir) {
        storage = new LogStorage(logDir);
        executor = new QueryExecutor(this);
    }

    Stream<LogStorage.LogEvent> getFilteredLogStream(
            Date after, Date before, Predicate<LogStorage.LogEvent> additionalFilter
    ) {
        return storage.getFilteredLogEventStream(after, before, additionalFilter);
    }

    @Override
    public Set<Object> execute(String query) {
        return executor.executeQuery(query);
    }

    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        return getUniqueIPs(after, before).size();
    }

    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        return storage.getFilteredIPSet(after, before, null);
    }

    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        return storage.getFilteredIPSet(after, before, log -> log.name.equals(user));
    }

    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        return storage.getFilteredIPSet(after, before, log -> log.event == event);
    }

    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        return storage.getFilteredIPSet(after, before, log -> log.status == status);
    }

    @Override
    public Set<String> getAllUsers() {
        return storage.getFilteredUserSet(null, null, null);
    }

    @Override
    public int getNumberOfUsers(Date after, Date before) {
        return storage.getFilteredUserSet(
                after, before, null
        ).size();
    }

    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        return storage.getFilteredAndMappedLogEventSet(
                after, before, log -> log.name.equals(user), log -> log.event
        ).size();
    }

    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        return storage.getFilteredUserSet(after, before, log -> log.ip.equals(ip));
    }

    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        return storage.getEventFilteredUserSet(after, before, Event.LOGIN);
    }

    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        return storage.getEventFilteredUserSet(after, before, Event.DOWNLOAD_PLUGIN);
    }

    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        return storage.getEventFilteredUserSet(after, before, Event.WRITE_MESSAGE);
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        return storage.getEventFilteredUserSet(after, before, Event.SOLVE_TASK);
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        return storage.getEventFilteredUserSet(after, before, Event.SOLVE_TASK, task);
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        return storage.getEventFilteredUserSet(after, before, Event.DONE_TASK);
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {
        return storage.getEventFilteredUserSet(after, before, Event.DONE_TASK, task);
    }

    @Override
    public Set<Date> getDatesForUserAndEvent(String user, Event event, Date after, Date before) {
        return storage.getFilteredDateSet(after, before, log -> log.name.equals(user) && log.event == event);
    }

    @Override
    public Set<Date> getDatesWhenSomethingFailed(Date after, Date before) {
        return storage.getFilteredDateSet(after, before, log -> log.status == Status.FAILED);
    }

    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {
        return storage.getFilteredDateSet(after, before, log -> log.status == Status.ERROR);
    }

    @Override
    public Date getDateWhenUserLoggedFirstTime(String user, Date after, Date before) {
        return storage.getUserFirstTimeDate(after, before, user, Event.LOGIN, null);
    }

    @Override
    public Date getDateWhenUserSolvedTask(String user, int task, Date after, Date before) {
        return storage.getUserFirstTimeDate(after, before, user, Event.SOLVE_TASK, task);
    }

    @Override
    public Date getDateWhenUserDoneTask(String user, int task, Date after, Date before) {
        return storage.getUserFirstTimeDate(after, before, user, Event.DONE_TASK, task);
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
        return storage.getFilteredAndMappedLogEventSet(
                after, before, null, log -> log.event
        );
    }

    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {
        return storage.getFilteredAndMappedLogEventSet(
                after, before, log -> log.ip.equals(ip), log -> log.event
        );
    }

    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {
        return storage.getFilteredAndMappedLogEventSet(
                after, before, log -> log.name.equals(user), log -> log.event
        );
    }

    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {
        return storage.getFilteredAndMappedLogEventSet(
                after, before, log -> log.status == Status.FAILED, log -> log.event
        );
    }

    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {
        return storage.getFilteredAndMappedLogEventSet(
                after, before, log -> log.status == Status.ERROR, log -> log.event
        );
    }

    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {
        return storage.getEventFilteredLogsCount(after, before, Event.SOLVE_TASK, task);
    }

    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {
        return storage.getEventFilteredLogsCount(after, before, Event.DONE_TASK, task);
    }

    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        return storage.getTasksStatsByEvent(after, before, Event.SOLVE_TASK);
    }

    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {
        return storage.getTasksStatsByEvent(after, before, Event.DONE_TASK);
    }

}