package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.CurrencyManipulator;
import com.javarush.task.task26.task2613.CurrencyManipulatorFactory;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;
import com.javarush.task.task26.task2613.exception.NotEnoughMoneyException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

class WithdrawCommand implements Command {
    @Override
    public void execute() throws InterruptOperationException {
        String code = ConsoleHelper.askCurrencyCode();
        CurrencyManipulator manipulator = CurrencyManipulatorFactory.getManipulatorByCurrencyCode(code);
        Map<Integer, Integer> result;
        do {
            ConsoleHelper.writeMessage("Withdraw amount?");
            result = getMoneyMap(
                    manipulator,
                    ConsoleHelper.readString()
            );
        } while (result == null);
        result.entrySet().stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Map.Entry::getKey)))
                .forEach(b -> ConsoleHelper.writeMessage(String.format("\t%s - %s", b.getKey(), b.getValue())));
    }

    private Map<Integer, Integer> getMoneyMap(CurrencyManipulator manipulator, String line) {
        if (!line.matches(ConsoleHelper.POSITIVE_NUMBER_REGEX)) {
            ConsoleHelper.writeMessage("not a number, try again");
            return null;
        }
        int amount = Integer.parseInt(line);
        if (!manipulator.isAmountAvailable(amount)) {
            ConsoleHelper.writeMessage("no money enough, try again");
            return null;
        }
        Map<Integer, Integer> result;
        try {
            result = manipulator.withdrawAmount(amount);
        } catch (NotEnoughMoneyException e) {
            ConsoleHelper.writeMessage("there's not enough banknotes, try another sum");
            result = null;
        }
        return result;
    }
}
