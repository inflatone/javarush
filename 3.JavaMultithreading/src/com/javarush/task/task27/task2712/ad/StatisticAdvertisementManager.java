package com.javarush.task.task27.task2712.ad;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StatisticAdvertisementManager {
    private final static StatisticAdvertisementManager instance = new StatisticAdvertisementManager();
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();

    private StatisticAdvertisementManager() {
    }

    public static StatisticAdvertisementManager getInstance() {
        return instance;
    }

    public List<Advertisement> getFilteredSortedListOfAdvertisement(Predicate<Advertisement> predicate) {
        return storage.list().stream()
                .filter(predicate)
                .sorted(Comparator.comparing(Advertisement::getName, String::compareToIgnoreCase))
                .collect(Collectors.toList());
    }

}

