package com.javarush.task.task36.task3608.view;

import com.javarush.task.task36.task3608.controller.Controller;
import com.javarush.task.task36.task3608.model.ModelData;

/**
 * Представление.
 *
 * @author Alexander Savchenko
 * @version 1.0
 * @since 2019-01-02
 */
public interface View {
    void refresh(ModelData modelData);

    void setController(Controller controller);
}
