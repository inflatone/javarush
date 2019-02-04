package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.CashMachine;
import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;

import java.util.ResourceBundle;

public class LoginCommand implements Command {
    private ResourceBundle validCreditCards
            = ResourceBundle.getBundle(CashMachine.class.getPackage().getName() + ".resources.verifiedCards");

    @Override
    public void execute() throws InterruptOperationException {
        String card;
        String pin;
        do {
            ConsoleHelper.writeMessage("Card number?");
            card = ConsoleHelper.readString();
            ConsoleHelper.writeMessage("Pin number?");
            pin = ConsoleHelper.readString();
        } while (isIncorrect(card, pin));
    }

    private boolean isIncorrect(String card, String pin) {
        if (!card.matches(ConsoleHelper.CARD_NUMBER_REGEX) || !pin.matches(ConsoleHelper.PIN_REGEX)) {
            ConsoleHelper.writeMessage("incorrect data format");
            return true;
        }
        if (!validCreditCards.containsKey(card) || !validCreditCards.getString(card).equals(pin)) {
            ConsoleHelper.writeMessage("inactive card or wrong pin");
            return true;
        }
        ConsoleHelper.writeMessage("successful authorization");
        return false;
    }
}
