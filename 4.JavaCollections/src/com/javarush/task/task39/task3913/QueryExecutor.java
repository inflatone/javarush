package com.javarush.task.task39.task3913;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class QueryExecutor {
    private final Map<Query, Function<QueryParser, Set<Object>>> commands;
    private LogParser logParser;
    private String query;

    {
        commands = Arrays.stream(Query.values())
                .collect(Collectors.toMap(
                        Function.identity(),
                        query ->
                                p -> getSet(query.fieldMapper(), p)
                ));
    }

    QueryExecutor(LogParser logParser, String query) {
        this.logParser = logParser;
        this.query = query;
    }

    Set<Object> executeQuery() {
        QueryParser parser = new QueryParser(query);
        return commands.getOrDefault(
                parser.getGetPart(),
                p -> Collections.emptySet()
        ).apply(parser);
    }

    private <T> Set<T> getSet(Function<LogStorage.LogEvent, T> mapper, QueryParser queryParser) {
        return getFilteredStream(queryParser)
                .map(mapper)
                .collect(Collectors.toSet());
    }

    private Stream<LogStorage.LogEvent> getFilteredStream(QueryParser parser) {
        return logParser.getFilteredLogStream(parser.getAfter(), parser.getBefore(), parser.getFilter());
    }
}