package com.javarush.task.task27.task2712.statistic;

import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatisticManager {
    private final static StatisticManager instance = new StatisticManager();
    private final StatisticStorage statisticStorage = new StatisticStorage();

    private StatisticManager() {
    }

    public static StatisticManager getInstance() {
        return instance;
    }

    public void register(EventDataRow data) {
        statisticStorage.put(data);
    }

    private class StatisticStorage {
        private final Map<EventType, List<EventDataRow>> storage;

        StatisticStorage() {
            storage = Arrays.stream(EventType.values())
                    .collect(Collectors.toMap(
                            Function.identity(),
                            i -> new ArrayList<>()
                    ));
        }

        private void put(EventDataRow data) {
            storage.get(data.getType()).add(data);
        }
    }
}