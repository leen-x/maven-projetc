package com.leenx.learn.mavenproject;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author linsongxiong
 * @Description:
 * @date 2021/07/14 11:34 上午
 **/
public class TransmittableThreadLocalDemo {
    public static class DemoUtil {
        private static final TransmittableThreadLocal<String> threadName = new TransmittableThreadLocal<>();

        public static String get() {
            return threadName.get();
        }

        public static void put(String value) {
            threadName.set(value);
        }
    }

    public static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " get from threadLocal : " + DemoUtil.get());
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 20,60, TimeUnit.SECONDS, new ArrayBlockingQueue(1024));
        Runnable commonRunnable = new MyRunnable();
        /**
         * value_1
         */
        DemoUtil.put("value_1");
        System.out.println("【main主线程】" + DemoUtil.get());
        pool.submit(commonRunnable);

        /**
         * value_2
         */
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DemoUtil.put("value_2");
        System.out.println("【main主线程】" + DemoUtil.get());
        TtlRunnable ttlRunnable = TtlRunnable.get(commonRunnable);
        pool.submit(ttlRunnable);
        pool.submit(commonRunnable);

        try {
            TimeUnit.SECONDS.sleep(100);
            System.out.println("退出。。。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
