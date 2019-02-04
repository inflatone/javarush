package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.CashMachine;
import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.CurrencyManipulator;
import com.javarush.task.task26.task2613.CurrencyManipulatorFactory;

import java.util.Collection;
import java.util.ResourceBundle;

class InfoCommand implements Command {
    private ResourceBundle res
            = ResourceBundle.getBundle(CashMachine.class.getPackage().getName() + ".resources.info_en");


    @Override
    public void execute() {
        ConsoleHelper.writeMessage(res.getString("before"));
        Collection<CurrencyManipulator> manipulators = CurrencyManipulatorFactory.getAllCurrencyManipulators();
        if (manipulators.stream().noneMatch(CurrencyManipulator::hasMoney)) {
            ConsoleHelper.writeMessage(res.getString("no.money"));
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
