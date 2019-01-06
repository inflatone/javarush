package com.javarush.task.task30.task3008;

/**
 * Перечисление, отвечает за тип сообщений пересылаемых между
 * клиентом и сервером.
 *
 * @author Alexander Savchenko
 * @version 1.0
 * @since 2019-01-05
 */
public enum MessageType {
    NAME_REQUEST,
    USER_NAME,
    NAME_ACCEPTED,
    TEXT,
    USER_ADDED,
    USER_REMOVED
}
