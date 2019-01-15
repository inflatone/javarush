package com.javarush.task.task27.task2712.ad;

import com.javarush.task.task27.task2712.ConsoleHelper;

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
        new BestSelectionFinder()
                .getBestSelection()
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

    private class BestSelectionFinder {
        private long bestPrice;
        private int bestTime;
        private List<Advertisement> bestList;
        private List<Advertisement> actualAds = storage.list()
                .stream().filter(ad -> ad.getHits() > 0).collect(Collectors.toList());

        List<Advertisement> getBestSelection() {
            bestList = Collections.emptyList();
            actualAds.forEach(ad -> recursiveCheck(Collections.singletonList(ad)));
            return bestList;
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
                    bestPrice = price;
                    bestTime = seconds;
                    bestList = ads;
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
            return price > bestPrice
                    ||
                    price == bestPrice
                            && (
                            seconds > bestTime
                                    || seconds == bestTime && size < bestList.size()
                    );
        }
    }
}
