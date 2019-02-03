package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.CurrencyManipulatorFactory;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;

class DepositCommand implements Command {
    @Override
    public void execute() throws InterruptOperationException {
        String code = ConsoleHelper.askCurrencyCode();
        String[] digits = ConsoleHelper.getValidTwoDigits(code);
        CurrencyManipulatorFactory
                .getManipulatorByCurrencyCode(code)
                .addAmount(getDenomination(digits), getAmount(digits));
    }

    private int getDenomination(String[] digits) {
        return getInt(digits, 0);
    }

    private int getAmount(String[] digits) {
        return getInt(digits, 1);
    }

    private int getInt(String[] digits, int pos) {
        return Integer.parseInt(digits[pos]);
    }
}
