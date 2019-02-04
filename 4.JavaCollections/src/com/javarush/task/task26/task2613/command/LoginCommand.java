package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.CashMachine;
import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;

import java.util.ResourceBundle;

public class LoginCommand implements Command {
    private ResourceBundle validCreditCards
            = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "verifiedCards");

    private ResourceBundle res
            = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "login_en");


    @Override
    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("before"));
        ConsoleHelper.writeMessage(res.getString("specify.data"));
        String card;
        String pin;
        do {
            card = ConsoleHelper.readString();
            pin = ConsoleHelper.readString();
        } while (isIncorrect(card, pin));
    }

    private boolean isIncorrect(String card, String pin) {
        if (!card.matches(ConsoleHelper.CARD_NUMBER_REGEX) || !pin.matches(ConsoleHelper.PIN_REGEX)) {
            ConsoleHelper.writeMessage(res.getString("try.again.with.details"));
            return true;
        }
        if (!validCreditCards.containsKey(card) || !validCreditCards.getString(card).equals(pin)) {
            ConsoleHelper.writeMessage(String.format(res.getString("not.verified.format"), card));
            return true;
        }
        ConsoleHelper.writeMessage(String.format(res.getString("success.format"), card));
        return false;
    }
}
