package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.StatisticAdvertisementManager;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;

public class DirectorTablet {
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    public void printAdvertisementProfit() {

        class TotalAmountCounter {
            private double value = 0d;

            private void add(double value) {
                this.value += value;
            }
        }

        final TotalAmountCounter counter = new TotalAmountCounter();
        StatisticManager.getInstance().getAdvertisementStats().entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey, Comparator.reverseOrder()))
                .forEach(entry -> {
                    counter.add(entry.getValue());
                    ConsoleHelper.writeMessage(
                            String.format("%s - %.2f",
                                    FORMATTER.format(entry.getKey()),
                                    entry.getValue()
                            ));
                });
        ConsoleHelper.writeMessage(String.format("Total - %.2f", counter.value));
    }

    public void printCookWorkloading() {
        StatisticManager.getInstance().getCooksStats().entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey, Comparator.reverseOrder()))
                .forEach(map -> {
                    ConsoleHelper.writeMessage(FORMATTER.format(map.getKey()));
                    map.getValue().entrySet().stream()
                            .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                            .filter(inner -> inner.getValue() > 0)
                            .forEach(inner -> ConsoleHelper.writeMessage(
                                    String.format("%s - %.0f min",
                                            inner.getKey(),
                                            Math.ceil(inner.getValue() / 60.0)
                                    )
                            ));
                    ConsoleHelper.writeMessage("");
                });
    }

    public void printActiveVideoSet() {
        StatisticAdvertisementManager.getInstance()
                .getFilteredSortedListOfAdvertisement(ad -> ad.getHits() > 0)
                .forEach(ad -> ConsoleHelper.writeMessage(String.format("%s - %s", ad.getName(), ad.getHits())));
    }

    public void printArchivedVideoSet() {
        StatisticAdvertisementManager.getInstance()
                .getFilteredSortedListOfAdvertisement(ad -> ad.getHits() == 0)
                .forEach(ad -> ConsoleHelper.writeMessage(ad.getName()));
    }

}

