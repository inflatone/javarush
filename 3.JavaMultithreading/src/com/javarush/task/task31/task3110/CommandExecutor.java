package com.javarush.task.task31.task3110;

import com.javarush.task.task31.task3110.command.*;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandExecutor {
    private static final Map<Operation, Command> allKnownCommandsMap = Stream.of(
            new ExitCommand(),
            new ZipAddCommand(),
            new ZipContentCommand(),
            new ZipCreateCommand(),
            new ZipExtractCommand(),
            new ZipRemoveCommand()
    ).collect(
            Collectors.toMap(Command::getOperation, Function.identity())
    );

    private CommandExecutor() {
    }

    public static void execute(Operation operation) throws Exception {
        allKnownCommandsMap.get(operation).execute();
    }
}
