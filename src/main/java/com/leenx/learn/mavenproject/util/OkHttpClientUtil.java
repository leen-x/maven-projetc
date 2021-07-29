package com.leenx.learn.mavenproject.util;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author leen-x
 * @Description:
 * @date 2021/07/22 5:26 下午
 **/
public class OkHttpClientUtil {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");


    private static OkHttpClient client;

    static {
        client = new OkHttpClient.Builder()
                // 建立连接超时时间5S
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                // 读取数据超时时间10S
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * GET
     *
     * @param url
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T get(String url, Class<T> tClass) {
        return get(url, null, tClass);
    }

    /**
     * GET
     *
     * @param url
     * @param header
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T get(String url, Map<String, String> header, Class<T> tClass) {
        Request.Builder requestBuilder = new Request.Builder();
        // url
        requestBuilder.url(url);
        // header
        buildHeader(requestBuilder, header);
        //
        requestBuilder.get();
        Response response = execute(requestBuilder.build());
        return toBean(response, tClass);
    }

    /**
     * POST
     *
     * @param url
     * @param body
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T post(String url, Object body, Class<T> tClass) {
        return post(url, body, null, tClass);
    }

    /**
     * POST
     *
     * @param url
     * @param body
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T post(String url, Object body, Map<String, String> headers, Class<T> tClass) {
        Request.Builder requestBuilder = new Request.Builder();
        // url
        requestBuilder.url(url);
        // header
        buildHeader(requestBuilder, headers);
        // body OKHttp3 POST、PUT等不能空，所以取""空字符串，""空字符串符合JSON规定，为null
        requestBuilder.post(buildJsonBody(body));
        // 执行请求
        Response response = execute(requestBuilder.build());
        return toBean(response, tClass);
    }

    /**
     * POST
     *
     * @param url
     * @param body
     * @return
     */
    public static InputStream request(String method, String url, Object body, Map<String, String> headers) {
        Request.Builder requestBuilder = new Request.Builder();
        // url
        requestBuilder.url(url);
        // header
        buildHeader(requestBuilder, headers);
        // body
        if (method == null || "".equals(method) || Constant.HTTP_GET.equals(method)) {
            requestBuilder.get();
        } else {
            requestBuilder.method(method, buildJsonBody(body));
        }
        // 执行请求
        Response response = execute(requestBuilder.build());
        return toInputStream(response);
    }

    /**
     * POST
     *
     * @param url
     * @param body
     * @return
     */
    public static String requestForString(String method, String url, Object body, Map<String, String> headers) {
        Request.Builder requestBuilder = new Request.Builder();
        // url
        requestBuilder.url(url);
        // header
        buildHeader(requestBuilder, headers);
        // body
        if (method == null || "".equals(method) || Constant.HTTP_GET.equals(method)) {
            requestBuilder.get();
        } else {
            requestBuilder.method(method, buildJsonBody(body));
        }
        // 执行请求
        Response response = execute(requestBuilder.build());
        return toString(response);
    }

    /**
     * POST
     *
     * @param url
     * @param body
     * @return
     */
    public static byte[] requestForBytes(String method, String url, Object body, Map<String, String> headers) {
        Request.Builder requestBuilder = new Request.Builder();
        // url
        requestBuilder.url(url);
        // header
        buildHeader(requestBuilder, headers);
        // body
        if (method == null || "".equals(method) || Constant.HTTP_GET.equals(method)) {
            requestBuilder.get();
        } else {
            requestBuilder.method(method, buildJsonBody(body));
        }
        // 执行请求
        Response response = execute(requestBuilder.build());
        return toBytes(response);
    }

    /**
     * 构造请求头
     *
     * @param builder
     * @param headers
     */
    private static void buildHeader(Request.Builder builder, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 构造json请求体
     *
     * @param body
     */
    private static RequestBody buildJsonBody(Object body) {
        // body OKHttp3 POST、PUT等不能空，所以取""空字符串，""空字符串符合JSON规定，为null
        String bodyJson = "";
        if (body != null) {
            bodyJson = JacksonUtil.toJSONString(body);
        }
        return RequestBody.create(bodyJson, MEDIA_TYPE_JSON);
    }

    /**
     * 执行request
     *
     * @param request
     * @return
     */
    private static Response execute(Request request) {
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * response body String
     *
     * @param response
     * @return
     */
    private static String toString(Response response) {
        String body = null;
        try {
            if (response.body() != null) {
                body = response.body().string();
                return body;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * response body byte[]
     *
     * @param response
     * @return
     */
    private static byte[] toBytes(Response response) {
        byte[] body = null;
        try {
            if (response.body() != null) {
                body = response.body().bytes();
                return body;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * response body bean
     *
     * @param response
     * @param tClass
     * @param <T>
     * @return
     */
    private static <T> T toBean(Response response, Class<T> tClass) {
        String body = null;
        try {
            if (response.body() != null && tClass != null) {
                body = response.body().string();
                return JacksonUtil.parseObject(body, tClass);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * response body InputStream
     *
     * @param response
     * @return
     */
    private static InputStream toInputStream(Response response) {
        return response.body() != null ? response.body().byteStream() : null;
    }

    public static void main(String[] args) {
        String url = "http://www.kuaidi100.com/query?type=%E5%BF%AB%E9%80%92%E5%85%AC%E5%8F%B8%E4%BB%A3%E5%8F%B7&postid=%E5%BF%AB%E9%80%92%E5%8D%95%E5%8F%B7";
        String resp = OkHttpClientUtil.requestForString(Constant.HTTP_GET, url, null, null);
        System.out.println(resp);
    }
}
