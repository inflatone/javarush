package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.CurrencyManipulator;
import com.javarush.task.task26.task2613.CurrencyManipulatorFactory;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;
import com.javarush.task.task26.task2613.exception.NotEnoughMoneyException;

import java.util.Map;

class WithdrawCommand implements Command {
    @Override
    public void execute() throws InterruptOperationException {
        String code = ConsoleHelper.askCurrencyCode();
        CurrencyManipulator manipulator = CurrencyManipulatorFactory.getManipulatorByCurrencyCode(code);
        Map<Integer, Integer> result;
        do {
            result = getMoneyMap(
                    manipulator,
                    ConsoleHelper.getValidAmount(manipulator::isAmountAvailable)
            );
        } while (result == null);
    }

    private Map<Integer, Integer> getMoneyMap(CurrencyManipulator manipulator, int amount) {
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
