package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.Connection;
import com.javarush.task.task30.task3008.ConsoleHelper;
import com.javarush.task.task30.task3008.Message;
import com.javarush.task.task30.task3008.MessageType;

import java.io.IOException;
import java.net.Socket;

public class Client {
    protected Connection connection;

    private volatile boolean clientConnected = false;

    public static void main(String[] args) {
        new Client().run();
    }

    public void run() {
        SocketThread created = getSocketThread();
        created.setDaemon(true);
        created.start();
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Ошибка ожидания соединения.");
            }
        }

        if (!clientConnected) {
            ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");
            return;
        }

        ConsoleHelper.writeMessage("Соединение установлено. Для выхода наберите команду 'exit'.");
        for (
                String line = ConsoleHelper.readString();
                clientConnected && !"exit".equals(line);
                line = ConsoleHelper.readString()
        ) {
            if (shouldSendTextFromConsole()) {
                sendTextMessage(line);
            }
        }
    }

    protected String getServerAddress() {
        ConsoleHelper.writeMessage("Адрес сервера?");
        return ConsoleHelper.readString();
    }

    protected int getServerPort() {
        ConsoleHelper.writeMessage("Порт сервера?");
        return ConsoleHelper.readInt();
    }

    protected String getUserName() {
        ConsoleHelper.writeMessage("Имя?");
        return ConsoleHelper.readString();
    }

    protected boolean shouldSendTextFromConsole() {
        return true;
    }

    protected SocketThread getSocketThread() {
        return new SocketThread();
    }

    protected void sendTextMessage(String text) {
        try {
            connection.send(
                    new Message(MessageType.TEXT, text)
            );
        } catch (IOException ioe) {
            ConsoleHelper.writeMessage("Ошибка при отправке сообщения.");
            clientConnected = false;
        }
    }


    public class SocketThread extends Thread {
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage(
                    String.format("%s присоединился к чату.", userName)
            );
        }

        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage(
                    String.format("%s покинул чат.", userName)
            );
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }

        protected void clientHandshake() throws IOException, ClassNotFoundException {
            for (boolean proceeded = true; proceeded; ) {
                Message received = connection.receive();
                if (received.getType() == MessageType.NAME_REQUEST) {
                    connection.send(
                            new Message(MessageType.USER_NAME, getUserName())
                    );
                } else if (received.getType() == MessageType.NAME_ACCEPTED) {
                    notifyConnectionStatusChanged(true);
                    proceeded = false;
                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }
        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            while (true) {
                Message received = connection.receive();
                if (received.getType() == MessageType.TEXT) {
                    processIncomingMessage(received.getData());
                } else if (received.getType() == MessageType.USER_ADDED) {
                    informAboutAddingNewUser(received.getData());
                } else if (received.getType() == MessageType.USER_REMOVED) {
                    informAboutDeletingNewUser(received.getData());
                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }
        }

        @Override
        public void run() {
            String address = getServerAddress();
            int port = getServerPort();
            try {
                connection = new Connection(
                        new Socket(address, port)
                );
                clientHandshake();
                clientMainLoop();
            } catch (IOException | ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            }
        }
    }


}
