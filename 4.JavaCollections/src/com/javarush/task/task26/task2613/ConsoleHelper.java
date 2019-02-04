package com.javarush.task.task26.task2613;

import com.javarush.task.task26.task2613.exception.InterruptOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConsoleHelper {
    private final static ResourceBundle res
            = ResourceBundle.getBundle(CashMachine.class.getPackage().getName() + ".resources.common_en");

    private final static BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));
    public final static String POSITIVE_NUMBER_REGEX = "^[1-9]\\d*" + "\\s*$";
    public final static String CARD_NUMBER_REGEX = "^\\d{12}$";
    public final static String PIN_REGEX = "^\\d{4}$";
    private final static String TWO_POSITIVE_NUMBERS_REGEX = "^[1-9]\\d*" + "\\s+" + "[1-9]\\d*" + "\\s*$";

    public static void writeMessage(String message) {
        if (message != null) {
            System.out.println(message);
        }
    }

    public static String readString() throws InterruptOperationException {
        String result = "";
        try {
            result = bis.readLine();
            checkExit(result);
        } catch (IOException ignore) {
        }
        return result;
    }

    public static String askCurrencyCode() throws InterruptOperationException {
        return getCorrectInput(
                res.getString("choose.currency.code"),
                Function.identity(),
                c -> c.length() != 3,
                String::toUpperCase
        );
    }

    public static String[] getValidTwoDigits(String currencyCode) throws InterruptOperationException {
        return getCorrectInput(
                String.format(res.getString("choose.denomination.and.count.format"), currencyCode),
                Function.identity(),
                c -> !c.matches(TWO_POSITIVE_NUMBERS_REGEX),
                s -> s.split("\\s+")
        );
    }

    public static Operation askOperation() throws InterruptOperationException {
        return getCorrectInput(
                res.getString("choose.operation"),
                ConsoleHelper::getOperation,
                Objects::isNull,
                Function.identity()
        );
    }

    public static void printExitMessage() {
        ConsoleHelper.writeMessage(res.getString("the.end"));
    }

    private static <I, T> T getCorrectInput(
            String message, Function<String, I> preValidateMapper,
            Predicate<I> incorrectCondition, Function<I, T> returnMapper) throws InterruptOperationException {
        writeMessage(message);
        I result;
        do {
            result = preValidateMapper.apply(readString());
        } while (validate(result, incorrectCondition));
        return returnMapper.apply(result);
    }

    private static Operation getOperation(String operationCode) {
        Operation result;
        try {
            result = Operation.getAllowableOperationByOrdinal(Integer.parseInt(operationCode));
        } catch (IllegalArgumentException e) {
            result = null;
        }
        return result;
    }

    private static <T> boolean validate(T content, Predicate<T> checker) {
        boolean result = checker.test(content);
        if (result) {
            writeMessage(res.getString("onInvalid"));
        }
        return result;
    }

    private static void checkExit(String line) throws InterruptOperationException {
        if ("EXIT".equalsIgnoreCase(line)) {
            printExitMessage();
            throw new InterruptOperationException();
        }
    }
}
