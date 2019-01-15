package com.javarush.task.task27.task2712.ad;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private int timeSeconds;

    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public void processVideos() {
        if (storage.list().isEmpty()) {
            throw new NoVideoAvailableException();
        }
        OptimalVideoSelector selector = new OptimalVideoSelector();
        StatisticManager.getInstance()
                .register(new VideoSelectedEventDataRow(
                        selector.optimalVideoSet,
                        selector.amount,
                        selector.totalDuration
                ));
        selector.optimalVideoSet.forEach(ad -> {
            ad.revalidate();
            ConsoleHelper.writeMessage(String.format(
                    "%s is displaying... %s, %s",
                    ad.getName(),
                    ad.getAmountPerOneDisplaying(),
                    1000 * ad.getAmountPerOneDisplaying() / ad.getDuration()
            ));
        });
    }

    private class OptimalVideoSelector {
        private long amount;
        private int totalDuration;
        private List<Advertisement> optimalVideoSet = Collections.emptyList();

        private List<Advertisement> actualAds = storage.list()
                .stream().filter(ad -> ad.getHits() > 0).collect(Collectors.toList());

        private OptimalVideoSelector() {
            actualAds.forEach(ad -> recursiveCheck(Collections.singletonList(ad)));
            optimalVideoSet.sort(
                    Comparator.comparing(
                            Advertisement::getAmountPerOneDisplaying, Comparator.reverseOrder()
                    ).thenComparing(Advertisement::getDuration, Comparator.reverseOrder())
            );
        }

        private void recursiveCheck(List<Advertisement> ads) {
            if (checkList(ads)) {
                actualAds.stream()
                        .filter(ad -> !ads.contains(ad))
                        .map(ad -> getDuplicateListWithAddedElement(ads, ad))
                        .forEach(this::recursiveCheck);
            }
        }

        private boolean checkList(List<Advertisement> ads) {
            int seconds = ads.stream().mapToInt(Advertisement::getDuration).sum();
            boolean needToContinue = seconds <= timeSeconds;
            if (needToContinue) {
                long price = ads.stream().mapToLong(Advertisement::getAmountPerOneDisplaying).sum();
                if (isBetterThenCurrentBest(price, seconds, ads.size())) {
                    amount = price;
                    totalDuration = seconds;
                    optimalVideoSet = ads;
                }
            }
            return needToContinue;
        }

        private List<Advertisement> getDuplicateListWithAddedElement(List<Advertisement> List, Advertisement element) {
            List<Advertisement> result = new ArrayList<>(List);
            result.add(element);
            return result;
        }

        private boolean isBetterThenCurrentBest(long price, int seconds, int size) {
            return price > amount
                    ||
                    price == amount
                            && (
                            seconds > totalDuration
                                    || seconds == totalDuration && size < optimalVideoSet.size()
                    );
        }
    }
}
