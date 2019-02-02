package com.javarush.task.task28.task2810.view;

import com.javarush.task.task28.task2810.Controller;
import com.javarush.task.task28.task2810.vo.Vacancy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        return "";
    }

    private void updateFile(String content) throws IOException {
        OutputStream out = new FileOutputStream(filePath);
        out.write(content.getBytes());
        out.close();

    }
}
