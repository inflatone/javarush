package com.javarush.task.task36.task3608.model;

/**
 * Модель.
 *
 * @author Alexander Savchenko
 * @version 1.0
 * @since 2019-01-02
 */
public interface Model {
    ModelData getModelData();

    void loadUsers();

    void loadDeletedUsers();

    void loadUserById(long userId);

    void deleteUserById(long id);

    void changeUserData(String name, long id, int level);
}
