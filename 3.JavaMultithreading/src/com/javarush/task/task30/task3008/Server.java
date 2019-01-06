package com.javarush.task.task30.task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Основной класс сервера.
 *
 * @author Alexander Savchenko
 * @version 1.0
 * @since 2019-01-05
 */
public class Server {

    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void sendBroadcastMessage(Message message) {
        try {
            for (Connection connection : connectionMap.values()) {
                connection.send(message);
            }
        } catch (IOException ioe) {
            ConsoleHelper.writeMessage("Ошибка. Сообщение не отправлено.");
        }
    }


    public static void main(String[] args) {
        ConsoleHelper.writeMessage("Порт сервера?");
        int port = ConsoleHelper.readInt();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ConsoleHelper.writeMessage("Сервер запущен.");
            while (true) {
                new Handler(serverSocket.accept())
                        .start();
            }
        } catch (IOException ioe) {
            ConsoleHelper.writeMessage("Ошибка соединения.");
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage(String.format("Соединение c %s установлено", socket.getRemoteSocketAddress()));
            String userName = null;
            try (Connection connection = new Connection(socket)) {
                userName = serverHandshake(connection);
                sendBroadcastMessage(
                        new Message(MessageType.USER_ADDED, userName)
                );
                sendListOfUsers(connection, userName);
                serverMainLoop(connection, userName);
            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Произошла ошибка при обмене данными с удаленным адресом.");
            } finally {
                if (userName != null) {
                    connectionMap.remove(userName);
                    sendBroadcastMessage(
                            new Message(MessageType.USER_REMOVED, userName)
                    );
                    ConsoleHelper.writeMessage("Соединение закрыто.");
                }
            }
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            Message received;
            do {
                connection.send(new Message(MessageType.NAME_REQUEST, "Представьтесь?"));
                received = connection.receive();
            } while (
                    received.getType() != MessageType.USER_NAME
                            || received.getData() == null
                            || "".equals(received.getData())
                            || connectionMap.putIfAbsent(received.getData(), connection) != null
            );
            connection.send(new Message(MessageType.NAME_ACCEPTED, "Добро пожаловать!"));
            return received.getData();
        }

        private void sendListOfUsers(Connection connection, String userName) throws IOException {
            for (String current : connectionMap.keySet()) {
                if (!current.equals(userName)) {
                    connection.send(new Message(MessageType.USER_ADDED, current));
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            do {
                Message input = connection.receive();
                if (input.getType() != MessageType.TEXT) {
                    ConsoleHelper.writeMessage("Ошибка.");
                } else {
                    sendBroadcastMessage(
                            new Message(MessageType.TEXT, String.format("%s: %s", userName, input.getData()))
                    );
                }
            } while (true);
        }
    }
}
