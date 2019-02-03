package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.CurrencyManipulator;
import com.javarush.task.task26.task2613.CurrencyManipulatorFactory;

import java.util.Collection;

class InfoCommand implements Command {
    @Override
    public void execute() {
        Collection<CurrencyManipulator> manipulators = CurrencyManipulatorFactory.getAllCurrencyManipulators();
        if (manipulators.stream().noneMatch(CurrencyManipulator::hasMoney)) {
            ConsoleHelper.writeMessage("No money available.");
        } else {
            manipulators.forEach(
                    m -> ConsoleHelper.writeMessage(getOutputLineForManipulator(m))
            );
        }
    }

    private String getOutputLineForManipulator(CurrencyManipulator manipulator) {
        return String.format(
                "%s - %d",
                manipulator.getCurrencyCode(),
                manipulator.getTotalAmount()
        );
    }
}
