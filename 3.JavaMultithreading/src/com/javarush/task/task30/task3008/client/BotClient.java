package com.javarush.task.task30.task3008.client;


import com.javarush.task.task30.task3008.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BotClient extends Client {

    public static void main(String[] args) {
        new BotClient().run();
    }


    @Override
    protected String getUserName() {
        return String.format("date_bot_%s", (int) (100 * Math.random()));
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    public class BotSocketThread extends SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            int split = message.indexOf(": ");
            if (split == -1) {
                return;
            }
            String name = message.substring(0, split);
            String data = message.substring(split + 2);
            SimpleDateFormat format = new SimpleDateFormat();
            switch (data) {
                case "дата":
                    format.applyPattern("d.MM.YYYY");
                    break;
                case "день":
                    format.applyPattern("d");
                    break;
                case "месяц":
                    format.applyPattern("MMMM");
                    break;
                case "год":
                    format.applyPattern("YYYY");
                    break;
                case "время":
                    format.applyPattern("H:mm:ss");
                    break;
                case "час":
                    format.applyPattern("H");
                    break;
                case "минуты":
                    format.applyPattern("m");
                    break;
                case "секунды":
                    format.applyPattern("s");
                    break;
                default:
                    return;
            }
            sendTextMessage(
                    String.format(
                            "Информация для %s: %s",
                            name,
                            format.format(Calendar.getInstance().getTime())
                    )
            );
        }
    }

}
