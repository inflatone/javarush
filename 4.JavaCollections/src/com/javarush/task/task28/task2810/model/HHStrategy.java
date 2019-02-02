package com.javarush.task.task28.task2810.model;

        import com.javarush.task.task28.task2810.vo.Vacancy;
        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;
        import org.jsoup.nodes.Element;
        import org.jsoup.select.Elements;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;

public class HHStrategy implements Strategy {
    private static final String SITE_NAME = "\"http://hh.ua\"";
    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+%s&page=%s";
    private static final String USER_AGENT = "Mozilla/5.0 (jsoup)";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> result = new ArrayList<>();
        try {
            for (int i = 0; ; i++) {
                Document doc = getDocument(searchString, i);
                Elements elements = doc.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");
                if (elements.isEmpty()) {
                    break;
                }
                elements.forEach(e -> {
                    Vacancy v = new Vacancy();
                    v.setTitle(getDataQaText(e, "vacancy-serp__vacancy-title"));
                    v.setCompanyName(getDataQaText(e, "vacancy-serp__vacancy-employer"));
                    v.setCity(getDataQaText(e, "vacancy-serp__vacancy-address"));
                    v.setSalary(getDataQaText(e, "vacancy-serp__vacancy-compensation"));
                    v.setUrl(e.select("a").first().attr("href"));
                    v.setSiteName(SITE_NAME);
                    result.add(v);
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getDataQaText(Element e, String attributeValue) {
        return e.getElementsByAttributeValue("data-qa", attributeValue).text();
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, searchString, page))
                .userAgent(USER_AGENT)
                .referrer("")
                .get();
    }

}
