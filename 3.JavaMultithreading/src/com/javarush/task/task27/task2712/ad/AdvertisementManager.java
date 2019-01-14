package com.javarush.task.task27.task2712.ad;

import com.javarush.task.task27.task2712.ConsoleHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
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
        new BestSetFinder()
                .getBestSet()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Advertisement::getAmountPerOneDisplaying, Comparator.reverseOrder()
                        ).thenComparing(Advertisement::getDuration, Comparator.reverseOrder())
                )
                .forEach(ad -> {
                    ad.revalidate();
                    ConsoleHelper.writeMessage(String.format(
                            "%s is displaying... %s, %s",
                            ad.getName(),
                            ad.getAmountPerOneDisplaying(),
                            1000 * ad.getAmountPerOneDisplaying() / ad.getDuration()
                    ));
                });
    }

    private class BestSetFinder {
        private long bestPrice;
        private int bestTime;
        private Set<Advertisement> bestSet;

        Set<Advertisement> getBestSet() {
            bestSet = Collections.emptySet();
            recursiveCheck(
                    storage.list().stream()
                            .filter(ad -> ad.getHits() > 0)
                            .collect(Collectors.toSet())
            );
            return bestSet;
        }

        private Set<Advertisement> getDuplicateSetWithoutElement(Set<Advertisement> ads, Advertisement element) {
            return ads.stream().filter(ad -> !ad.equals(element)).collect(Collectors.toSet());
        }

        private void recursiveCheck(Set<Advertisement> ads) {
            if (!ads.isEmpty()) {
                checkSet(ads);
                ads.forEach(ad -> recursiveCheck(getDuplicateSetWithoutElement(ads, ad)));
            }
        }

        private void checkSet(Set<Advertisement> ads) {
            int seconds = ads.stream().mapToInt(Advertisement::getDuration).sum();
            if (seconds <= timeSeconds) {
                long price = ads.stream().mapToLong(Advertisement::getAmountPerOneDisplaying).sum();
                if (isBetterThenCurrentBest(price, seconds, ads.size())) {
                    bestPrice = price;
                    bestTime = seconds;
                    bestSet = ads;
                }
            }
        }

        private boolean isBetterThenCurrentBest(long price, int seconds, int size) {
            return price > bestPrice
                    ||
                    price == bestPrice
                            && (
                            seconds > bestTime
                                    || seconds == bestTime && size < bestSet.size()
                    );
        }
    }
}
