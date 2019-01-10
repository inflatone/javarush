package com.javarush.task.task28.task2802;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* 
Пишем свою ThreadFactory
*/
public class Solution {

    public static void main(String[] args) {
        class EmulateThreadFactoryTask implements Runnable {
            @Override
            public void run() {
                emulateThreadFactory();
            }
        }

        ThreadGroup group = new ThreadGroup("firstGroup");
        Thread thread = new Thread(group, new EmulateThreadFactoryTask());

        ThreadGroup group2 = new ThreadGroup("secondGroup");
        Thread thread2 = new Thread(group2, new EmulateThreadFactoryTask());

        thread.start();
        thread2.start();
    }

    private static void emulateThreadFactory() {
        AmigoThreadFactory factory = new AmigoThreadFactory();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        };
        factory.newThread(r).start();
        factory.newThread(r).start();
        factory.newThread(r).start();
    }

    public static class AmigoThreadFactory implements ThreadFactory {
        private static final AtomicInteger factoryCounter = new AtomicInteger(1);
        private final AtomicInteger threadCounter;
        private final int factoryNum;
        private final ThreadGroup group;

        public AmigoThreadFactory() {
            this.factoryNum = factoryCounter.getAndIncrement();
            this.threadCounter = new AtomicInteger(1);
            this.group = Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread result = new Thread(
                    group,
                    r,
                    String.format(
                            "%s-pool-%s-thread-%s",
                            group.getName(),
                            factoryNum,
                            threadCounter.getAndIncrement())
            );
            result.setDaemon(false);
            result.setPriority(Thread.NORM_PRIORITY);
            return result;
        }
    }

}
