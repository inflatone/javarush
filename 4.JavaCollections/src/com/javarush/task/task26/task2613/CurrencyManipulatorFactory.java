package com.javarush.task.task26.task2613;

import java.util.HashMap;
import java.util.Map;

public class CurrencyManipulatorFactory {
    private final static Map<String, CurrencyManipulator> map = new HashMap<>();

    public static CurrencyManipulator getManipulatorByCurrencyCode(String currencyCode) {
        currencyCode = currencyCode.toUpperCase();
        CurrencyManipulator result = map.get(currencyCode);
        if (result == null) {
            result = new CurrencyManipulator(currencyCode);
            map.put(currencyCode, result);
        }
        return result;
    }

    private CurrencyManipulatorFactory() {
    }
}
