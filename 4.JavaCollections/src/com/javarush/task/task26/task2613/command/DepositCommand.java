package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.CashMachine;
import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.CurrencyManipulatorFactory;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;

import java.util.ResourceBundle;

class DepositCommand implements Command {
    private ResourceBundle res
            = ResourceBundle.getBundle(CashMachine.class.getPackage().getName() + ".resources.deposit_en");

    @Override
    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("before"));
        String code = ConsoleHelper.askCurrencyCode();
        String[] digits = ConsoleHelper.getValidTwoDigits(code);
        int denomination = getDenomination(digits);
        int amount = getAmount(digits);
        CurrencyManipulatorFactory
                .getManipulatorByCurrencyCode(code)
                .addAmount(denomination, amount);
        ConsoleHelper.writeMessage(
                String.format(res.getString("success.format"), denomination * amount, code)
        );
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
