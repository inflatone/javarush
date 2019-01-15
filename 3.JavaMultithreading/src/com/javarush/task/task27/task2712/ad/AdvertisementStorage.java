package com.javarush.task.task27.task2712.ad;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementStorage {
    private final static AdvertisementStorage storage = new AdvertisementStorage();
    private final List<Advertisement> videos = new ArrayList<>();

    private AdvertisementStorage() {
        Object someContent = new Object();
        add(new Advertisement(someContent, "1st Video", 5000, 100, 3 * 60)); // 3 min
        add(new Advertisement(someContent, "2nd Video", 100, 10, 15 * 60)); // 15 min
        add(new Advertisement(someContent, "3rd Video", 400, 2, 10 * 60));   // 10 min
        add(new Advertisement(someContent, "4th Video", 400, 2, 5 * 60));   // 10 min

        add(new Advertisement(someContent, "5th Video", 5000, 100, 5 * 60));
        add(new Advertisement(someContent, "6th Video", 100, 10, 20 * 60));
        add(new Advertisement(someContent, "7th Video", 400, 2, 15 * 60));
        add(new Advertisement(someContent, "8th Video", 400, 2, 8 * 60));

        add(new Advertisement(someContent, "9th Video", 5000, 100, 2 * 60));
        add(new Advertisement(someContent, "10th Video", 100, 10, 5 * 60));
        add(new Advertisement(someContent, "11th Video", 400, 2, 4 * 60));
        add(new Advertisement(someContent, "12th Video", 400, 2, 2 * 60));

        add(new Advertisement(someContent, "13th Video", 500, 100, 2 * 60));
        add(new Advertisement(someContent, "14th Video", 220, 10, 5 * 60));
        add(new Advertisement(someContent, "15th Video", 20, 2, 4 * 60));
        add(new Advertisement(someContent, "16th Video", 55, 2, 2 * 60));
    }

    public static AdvertisementStorage getInstance() {
        return storage;
    }

    public void add(Advertisement advertisement) {
        videos.add(advertisement);
    }

    public List<Advertisement> list() {
        return videos;
    }
}
