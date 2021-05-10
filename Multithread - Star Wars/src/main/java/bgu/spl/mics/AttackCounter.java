package bgu.spl.mics;

import java.util.concurrent.*;

public class AttackCounter extends CountDownLatch {
    private int count;
    private static Object CountHolder;

    /**
     * Constructs a {@code CountDownLatch} initialized with the given count.
     *
     * @param count the number of times {@link #countDown} must be invoked
     *              before threads can pass through {@link #await}
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public AttackCounter(int count) {
        super(count);
    }
    private static class CounterHolder {
        private static int count;
        private static final AttackCounter instance = new AttackCounter(count);
    }

    public static AttackCounter getInstance() {
        return CounterHolder.instance;
    }
}
