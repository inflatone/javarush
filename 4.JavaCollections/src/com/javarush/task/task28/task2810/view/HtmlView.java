package com.javarush.task.task28.task2810.view;

import com.javarush.task.task28.task2810.Controller;
import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HtmlView implements View {
    private Controller controller;

    private final String filePath =
            "./4.JavaCollections/src/"
                    + getClass().getPackage().getName().replace('.', '/')
                    + "/vacancies.html"
    ;

    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            updateFile(
                    getUpdatedFileContent(vacancies)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(vacancies.size());
    }

    @Override
    public void setController(Controller controller) {

        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Odessa");
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(
                new File(filePath),
                StandardCharsets.UTF_8.name()
        );
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        String result;
        try {
            Document original = getDocument();
            Element originalTemplateRef = original.getElementsByClass("template").first();
            Element template = originalTemplateRef.clone();
            template.removeAttr("style");
            template.removeClass("template");
            original.getElementsByClass("vacancy").stream()
                    .filter(e -> !"vacancy template".equals(e.className()))
                    .forEach(Node::remove);

            for (Vacancy v : vacancies) {
                Element current = template.clone();
                current.getElementsByClass("city").first().text(v.getCity());
                current.getElementsByClass("companyName").first().text(v.getCompanyName());
                current.getElementsByClass("salary").first().text(v.getSalary());
                current.select("a").first()
                        .text(v.getTitle())
                        .attr("href", v.getUrl());
                //current.wrap("<tr class=\"vacancy\"></tr>");
                originalTemplateRef.before(current);
            }
            result = original.outerHtml();
        } catch (IOException e) {
            e.printStackTrace();
            result = "Some exception occurred";
        }
        return result;
    }

    private void updateFile(String content) throws IOException {
        OutputStream out = new FileOutputStream(filePath);
        out.write(content.getBytes());
        out.close();
    }
}
