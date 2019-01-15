package com.javarush.task.task27.task2712.statistic;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventType;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatisticManager {
    private final static StatisticManager instance = new StatisticManager();
    private final StatisticStorage statisticStorage = new StatisticStorage();
    private final Set<Cook> cooks = new HashSet<>();

    private StatisticManager() {
    }

    public static StatisticManager getInstance() {
        return instance;
    }

    public void register(EventDataRow data) {
        statisticStorage.put(data);
    }

    public void register(Cook cook) {
        cooks.add(cook);
    }

    public Map<LocalDate, Double> getAdvertisementStats() {
        return statisticStorage.getSelectedVideosEvents().stream().collect(
                Collectors.groupingBy(
                        event -> event.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.summingDouble(event -> ((VideoSelectedEventDataRow) event).getAmount() / 100.0)
                )
        );
    }

    public Map<LocalDate, Map<String, Integer>> getCooksStats() {
        return statisticStorage.getCookedOrderEvents().stream().collect(
                Collectors.groupingBy(
                        event -> event.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.groupingBy(
                                event -> ((CookedOrderEventDataRow) event).getCookName(),
                                Collectors.summingInt(EventDataRow::getTime)
                        )
                )
        );
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

        private List<EventDataRow> getSelectedVideosEvents() {
            return storage.get(EventType.SELECTED_VIDEOS);
        }

        private List<EventDataRow> getCookedOrderEvents() {
            return storage.get(EventType.COOKED_ORDER);
        }
    }
}