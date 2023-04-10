package com.topjava.webapp;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainConcurrency {
    private static final int NUM_THREADS = 10000;
    private final int counter = 0;
    private final AtomicInteger atomicCounter = new AtomicInteger();
    private static final Lock lock = new ReentrantLock();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + ':' + Thread.currentThread().getState());

        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ':' + getState());
            }
        };
        thread0.start();

        new Thread(() -> System.out.println(Thread.currentThread().getName() + ':' + Thread.currentThread().getState())).start();

        MainConcurrency mainConcurrency = new MainConcurrency();
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < NUM_THREADS; i++) {
            Future<Integer> future = executorService.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.increment();
                }
                latch.countDown();
                return 5;
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executorService.shutdown();
        System.out.println(mainConcurrency.atomicCounter.get());
    }

    public void increment() {
        atomicCounter.incrementAndGet();
    }

}

class Deadlock {
    private static final String firstStr = "first string";
    private static final String secondStr = "second string";

    public static void main(String[] args) {
        createThread(firstStr, secondStr).start();
        createThread(secondStr, firstStr).start();
    }

    private static <T> Thread createThread(T res1, T res2) {
        return new Thread(() -> {
            synchronized (res1) {
                System.out.println("lock resource 'res1' - " + res1);
                synchronized (res2) {
                    System.out.println("lock resource 'res2' - " + res2);
                }
            }
        });
    }
}