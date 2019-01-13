package com.javarush.task.task30.task3003;

import java.util.concurrent.TransferQueue;

public class Producer implements Runnable {
    private TransferQueue<ShareItem> queue;

    public Producer(TransferQueue<ShareItem> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (
                    int i = 1;
                    i < 10 && !Thread.currentThread().isInterrupted();
                    i++
            ) {
                System.out.format("Элемент 'ShareItem-%s' добавлен%n", i);
                queue.offer(new ShareItem(String.format("ShareItem-%s", i)));
                Thread.sleep(100);
                if (queue.hasWaitingConsumer()) {
                    System.out.format("Consumer в ожидании!%n");
                }
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
