package com.javarush.task.task26.task2613;

import com.javarush.task.task26.task2613.exception.NotEnoughMoneyException;

import java.util.HashMap;
import java.util.Map;

public class CurrencyManipulator {
    private String currencyCode;
    private Map<Integer, Integer> denominations = new HashMap<>();

    public CurrencyManipulator(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void addAmount(int denomination, int count) {
        denominations.merge(denomination, count, Integer::sum);
    }

    public int getTotalAmount() {
        return denominations.entrySet().stream()
                .mapToInt(entry -> entry.getKey() * entry.getValue())
                .sum();
    }

    public boolean hasMoney() {
        return getTotalAmount() > 0;
    }

    public boolean isAmountAvailable(int expectedAmount) {
        return getTotalAmount() >= expectedAmount;
    }

    public Map<Integer, Integer> withdrawAmount(int expectedAmount) throws NotEnoughMoneyException {
        return new DenominationsFinder(expectedAmount).getMoneySet();
    }

    private class DenominationsFinder {
        private int expectedAmount;
        private Map<Integer, Integer> bestSet;
        private int bestCount;

        DenominationsFinder(int expectedAmount) {
            this.expectedAmount = expectedAmount;
            this.bestCount = 1 + denominations.entrySet().stream().mapToInt(Map.Entry::getValue).sum();
        }

        private void find(Map<Integer, Integer> availableBanknotes, Map<Integer, Integer> selectedBanknotes, int selectedCount, int selectedAmount) {
            if (selectedAmount == expectedAmount && selectedCount < bestCount) {
                bestSet = selectedBanknotes;
                bestCount = selectedCount;
            } else if (selectedCount <= bestCount && selectedAmount < expectedAmount) {
                availableBanknotes.keySet().forEach(
                        banknote -> find(
                                removeBanknote(availableBanknotes, banknote),
                                addBanknote(selectedBanknotes, banknote),
                                selectedCount + 1,
                                selectedAmount + banknote)
                );
            }
        }

        Map<Integer, Integer> getMoneySet() throws NotEnoughMoneyException {
            find(denominations, new HashMap<>(), 0, 0);
            if (bestSet == null) {
                throw new NotEnoughMoneyException();
            }
            removeSelectedBanknotes(bestSet);
            return bestSet;
        }

        private Map<Integer, Integer> removeBanknote(Map<Integer, Integer> banknotes, Integer removing) {
            Map<Integer, Integer> result = new HashMap<>(banknotes);
            result.computeIfPresent(removing, (k, v) -> v - 1);
            result.remove(removing, 0);
            return result;
        }

        private Map<Integer, Integer> addBanknote(Map<Integer, Integer> banknotes, Integer adding) {
            Map<Integer, Integer> result = new HashMap<>(banknotes);
            result.merge(adding, 1, (old, fresh) -> old + fresh);
            return result;
        }

        private void removeSelectedBanknotes(Map<Integer, Integer> moneySet) {
            moneySet.forEach((k, v) -> {
                denominations.computeIfPresent(k, (key, value) -> value - v);
                denominations.remove(k, 0);
            });
        }
    }
}
