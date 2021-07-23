package com.leenx.learn.mavenproject;

import com.leenx.learn.mavenproject.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.*;

@Slf4j
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
        Demo demo = new Demo();
        demo.name = "lsx";
        demo.addr = "China";
        System.out.println(JacksonUtil.toJSONString(demo));
    }
}
