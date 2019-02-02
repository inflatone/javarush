package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoikrugStrategy implements Strategy {
    private static final String SITE_NAME = "https://moikrug.ru";
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?q=java+%s&page=%d";
    private static final String USER_AGENT = "Mozilla/5.0 (jsoup)";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> result = new ArrayList<>();
        try {
            for (int i = 0; ; i++) {
                Document doc = getDocument(searchString, i);
                Elements elements = doc.getElementsByClass("job");
                if (elements.isEmpty()) {
                    break;
                }
                elements.forEach(e -> {
                    Vacancy v = new Vacancy();
                    v.setTitle(getInnerClassText(e, "title"));
                    v.setCompanyName(getInnerClassText(e, "company_name"));
                    v.setCity(getInnerClassText(e, "location"));
                    v.setSalary(getInnerClassText(e, "count"));
                    v.setUrl(
                            SITE_NAME + e.getElementsByClass("job_icon").first().attr("href")
                    );

                    v.setSiteName(SITE_NAME);
                    result.add(v);
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getInnerClassText(Element e, String className) {
        Element first = e.getElementsByClass(className).first();
        return first != null ? first.text() : "";
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, searchString, page))
                .userAgent(USER_AGENT)
                .referrer("")
                .get();
    }

}
