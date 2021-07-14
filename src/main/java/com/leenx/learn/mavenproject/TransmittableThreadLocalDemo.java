package com.leenx.learn.mavenproject;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;

import java.text.MessageFormat;
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
        private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();
        private static final InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
        private static final TransmittableThreadLocal<String> transmittableThreadLocal = new TransmittableThreadLocal<>();


        public static void get() {
            String format = "ThreadLocal:{0}  InheritableThreadLocal:{1} TransmittableThreadLocal:{2}";
            System.out.println(MessageFormat.format(format, threadLocal.get(), inheritableThreadLocal.get(), transmittableThreadLocal.get()));
        }

        public static void put(String value) {
            threadLocal.set(value);
            inheritableThreadLocal.set(value);
            transmittableThreadLocal.set(value);
        }
    }

    public static class MyRunnable implements Runnable {
        @Override
        public void run() {
            DemoUtil.get();
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue(1024));
        Runnable commonRunnable = new MyRunnable();
        /**
         * ThreadLocal value_1
         * 创建线程是继承
         */
        DemoUtil.put("value_1");
        System.out.println("【main主线程】value_1");
        pool.submit(commonRunnable);

        /**
         * ThreadLocal value_2
         */
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DemoUtil.put("value_2");
        System.out.println("【main主线程】value_2");
        // Runnable
        pool.submit(commonRunnable);
        // TtlRunnable
        TtlRunnable ttlRunnable = TtlRunnable.get(commonRunnable);
        pool.submit(ttlRunnable);

        try {
            TimeUnit.SECONDS.sleep(100);
            System.out.println("退出。。。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
