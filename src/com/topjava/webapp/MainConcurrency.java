package com.topjava.webapp;

import java.util.ArrayList;
import java.util.List;

public class MainConcurrency {
    private static final int NUM_THREADS = 10000;
    private int counter = 0;

    public static void main(String[] args) {

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
        List<Thread> threads = new ArrayList<>(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.increment();
                }
            });
            thread.start();
            threads.add(thread);
        }

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println(mainConcurrency.counter);
    }

    public synchronized void increment() {
        counter++;
    }

}

class Deadlock {
    private static final String firstStr = "first string";
    private static final String secondStr = "second string";

    public static void main(String[] args) {
        Thread firstThread = new Thread(() -> {
            synchronized (firstStr) {
                System.out.println("firstThread lock resource 'firstStr'");
                synchronized (secondStr) {
                    System.out.println("firstThread lock resource 'secondStr'");
                }
            }
        });
        Thread secondThread = new Thread(() -> {
            synchronized (secondStr) {
                System.out.println("secondThread lock resource 'secondStr'");
                synchronized (firstStr) {
                    System.out.println("secondThread lock resource 'firstStr'");
                }
            }
        });

        firstThread.start();
        secondThread.start();
    }
}