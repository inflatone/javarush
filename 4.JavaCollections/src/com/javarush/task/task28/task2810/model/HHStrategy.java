package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class HHStrategy implements Strategy {
    private static final String URL_FORMAT = "http://hh.ru/search/vacancy?text=java+%s&page=%s";
    private static final String USER_AGENT = "Mozilla/5.0 (jsoup)";
    private static final int TIMEOUT = 5 * 1000;

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        try {
            Document doc = Jsoup.connect(String.format(URL_FORMAT, searchString, 1))
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
