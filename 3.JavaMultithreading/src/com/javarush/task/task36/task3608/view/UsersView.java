package com.javarush.task.task36.task3608.view;

import com.javarush.task.task36.task3608.controller.Controller;
import com.javarush.task.task36.task3608.model.ModelData;

/**
 * Представление пользователей.
 *
 * @author Alexander Savchenko
 * @version 1.0
 * @since 2019-01-02
 */
public class UsersView implements View {

    private Controller controller;


    @Override
    public void refresh(ModelData modelData) {
        System.out.println(
                modelData.isDisplayDeletedUserList()
                        ? "All deleted users:"
                        : "All users:"
        );
        modelData.getUsers().forEach(user -> System.out.printf("%s%s%n", '\t', user));
        System.out.println("===================================================");
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void fireEventShowAllUsers() {
        controller.onShowAllUsers();
    }

    public void fireEventShowDeletedUsers() {
        controller.onShowAllDeletedUsers();
    }

    public void fireEventOpenUserEditForm(long id) {
        controller.onOpenUserEditForm(id);
    }
}
