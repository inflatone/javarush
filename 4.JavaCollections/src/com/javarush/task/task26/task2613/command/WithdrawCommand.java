package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.CashMachine;
import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.CurrencyManipulator;
import com.javarush.task.task26.task2613.CurrencyManipulatorFactory;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;
import com.javarush.task.task26.task2613.exception.NotEnoughMoneyException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;

class WithdrawCommand implements Command {
    private ResourceBundle res
            = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "withdraw_en");

    @Override
    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("before"));
        String code = ConsoleHelper.askCurrencyCode();
        CurrencyManipulator manipulator = CurrencyManipulatorFactory.getManipulatorByCurrencyCode(code);
        Map<Integer, Integer> result;
        String amount;
        do {
            ConsoleHelper.writeMessage(res.getString("specify.amount"));
            amount = ConsoleHelper.readString();
            result = getMoneyMap(
                    manipulator,
                    amount,
                    code
            );
        } while (result == null);
        result.entrySet().stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Map.Entry::getKey)))
                .forEach(b -> ConsoleHelper.writeMessage(String.format("\t%s - %s", b.getKey(), b.getValue())));
    }

    private Map<Integer, Integer> getMoneyMap(CurrencyManipulator manipulator, String line, String code) {
        if (!line.matches(ConsoleHelper.POSITIVE_NUMBER_REGEX)) {
            ConsoleHelper.writeMessage(res.getString("specify.not.empty.amount"));
            return null;
        }
        int amount = Integer.parseInt(line);
        if (!manipulator.isAmountAvailable(amount)) {
            ConsoleHelper.writeMessage(res.getString("not.enough.money"));
            return null;
        }
        Map<Integer, Integer> result;
        try {
            result = manipulator.withdrawAmount(amount);
        } catch (NotEnoughMoneyException e) {
            ConsoleHelper.writeMessage(res.getString("exact.amount.not.available"));
            result = null;
        }
        ConsoleHelper.writeMessage(String.format(res.getString("success.format"), amount, code));
        return result;
    }
}
