package com.javarush.task.task26.task2613.command;

import com.javarush.task.task26.task2613.ConsoleHelper;
import com.javarush.task.task26.task2613.exception.InterruptOperationException;

public class LoginCommand implements Command {
    private static final String CARD_NUMBER = "123456789012";
    private static final String CARD_PIN = "1234";

    @Override
    public void execute() throws InterruptOperationException {
        String card;
        String pin;
        while (true) {
            ConsoleHelper.writeMessage("Card number?");
            card = ConsoleHelper.readString();
            ConsoleHelper.writeMessage("Pin number?");
            pin = ConsoleHelper.readString();
            if (!card.matches(ConsoleHelper.CARD_NUMBER_REGEX) || !pin.matches(ConsoleHelper.PIN_REGEX)) {
                continue;
            }
            if (card.equals(CARD_NUMBER) && pin.equals(CARD_PIN)) {
                break;
            }
        }
    }
}
