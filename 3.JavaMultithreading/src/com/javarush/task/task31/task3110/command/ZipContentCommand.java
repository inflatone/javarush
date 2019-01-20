package com.javarush.task.task31.task3110.command;

import com.javarush.task.task31.task3110.ConsoleHelper;
import com.javarush.task.task31.task3110.Operation;
import com.javarush.task.task31.task3110.ZipFileManager;

public class ZipContentCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Просмотр содержимого архива.");
        ZipFileManager manager = getZipFileManager();
        ConsoleHelper.writeMessage("Содержимое архива:");
        manager.getFilesList().forEach(p -> ConsoleHelper.writeMessage(p.toString()));
        ConsoleHelper.writeMessage("Содержимое архива прочитано.");
    }

    @Override
    public Operation getOperation() {
        return Operation.CONTENT;
    }
}
