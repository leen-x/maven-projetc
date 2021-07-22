package com.leenx.learn.mavenproject;

import java.util.concurrent.*;

public class App {
    public static CountDownLatch count1 = new CountDownLatch(10000);
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(
            6,
            8,
            300,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public static class MyRunnable implements Runnable {
        @Override
        public void run() {
            count1.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello World!");
        MyRunnable runnable = new MyRunnable();
        long start = System.nanoTime();
//        for (int i = 0; i < 10000; i ++) {
//            new Thread(runnable).start();
//        }
//        count1.await();
//        System.out.println((System.nanoTime() - start) / 1000000 + "ms");
        start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            pool.submit(runnable);
        }
        count1.await();
        System.out.println((System.nanoTime() - start) / 1000000 + "ms");
        System.out.println(count1.getCount());
        pool.shutdown();
        Executors.newCachedThreadPool();
        Executors.newFixedThreadPool(4);
        Executors.newSingleThreadExecutor();
        Executors.newScheduledThreadPool(10);
        Executors.newWorkStealingPool();
    }
}
