package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.Operation;

public interface Command {
    void execute() throws Exception;

    Operation getOperation();
}
