package com.javarush.task.task25.task2506;

/**
 * Монитор состояния нити.
 *
 * @author Alexander Savchenko
 * @version 1.0
 * @since 2019-01-04
 */
public class LoggingStateThread extends Thread {
    private Thread target;

    public LoggingStateThread(Thread target) {
        this.target = target;
    }

    @Override
    public void run() {
        State state = null;
        while (state != State.TERMINATED) {
            if (target.getState() != state) {
                state = target.getState();
                System.out.println(state);
            }
        }
    }
}
