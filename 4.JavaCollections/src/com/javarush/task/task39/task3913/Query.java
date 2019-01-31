package com.javarush.task.task39.task3913;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

enum Query {
    IP("ip", Function.identity(), log -> log.ip),
    USER("user", Function.identity(), log -> log.name),
    DATE("date", LogParserHelper::getDate, log -> log.date),
    EVENT("event", LogParserHelper::chooseEvent, log -> log.event),
    STATUS("status", LogParserHelper::chooseStatus, log -> log.status);

    static final String VALUES_REGEX_PART;

    static {
        VALUES_REGEX_PART = Arrays.stream(values())
                .map(v -> v.name)
                .collect(Collectors.joining("|"));
    }

    private final String name;
    private final Function<String, ?> stringMapper;
    private final Function<LogStorage.LogEvent, ?> fieldMapper;

    Query(String name, Function<String, ?> stringMapper, Function<LogStorage.LogEvent, ?> fieldMapper) {
        this.name = name;
        this.stringMapper = stringMapper;
        this.fieldMapper = fieldMapper;
    }

    boolean validate(LogStorage.LogEvent log, String temp) {
        return fieldMapper.apply(log).equals(
                stringMapper.apply(temp)
        );
    }

    @SuppressWarnings("unchecked")
    <T> Function<LogStorage.LogEvent, T> fieldMapper() {
        return (Function<LogStorage.LogEvent, T>) fieldMapper;
    }

    @Override
    public String toString() {
        return name;
    }
}