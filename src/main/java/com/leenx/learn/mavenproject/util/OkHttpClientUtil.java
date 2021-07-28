package com.leenx.learn.mavenproject.util;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * @author leen-x
 * @Description:
 * @date 2021/07/22 5:26 下午
 **/
public class OkHttpClientUtil {
    private static OkHttpClient client;

    static {
        client = new OkHttpClient.Builder()
                // 建立连接超时时间5S
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                // 读取数据超时时间10S
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
    }

}
