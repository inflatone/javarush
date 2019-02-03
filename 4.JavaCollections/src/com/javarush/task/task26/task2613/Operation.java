package com.javarush.task.task26.task2613;

public enum Operation {
    INFO,
    DEPOSIT,
    WITHDRAW,
    EXIT;

    public static Operation getAllowableOperationByOrdinal(Integer i) {
        Operation[] all = Operation.values();
        if (i < 1 || i > all.length) {
            throw new IllegalArgumentException();
        }
        return all[i - 1];
    }
}
