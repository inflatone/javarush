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
        add(new Advertisement(someContent, "четвертое видео", 2000, 4, 2 * 60));   // 2 min
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
