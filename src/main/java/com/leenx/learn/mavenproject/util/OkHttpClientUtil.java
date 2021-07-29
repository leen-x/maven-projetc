package com.leenx.learn.mavenproject.util;

import kotlin.Pair;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author leen-x
 * @Description: OkHttp3 工具类
 * @date 2021/07/22 5:26 下午
 **/
public class OkHttpClientUtil {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_TXT = MediaType.parse("text/plain; charset=utf-8");


    private static final OkHttpClient okHttpClient;

    static {
        okHttpClient = new OkHttpClient.Builder()
                // 建立连接超时时间5S
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                // 读取数据超时时间10S
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * GET request for String response body
     *
     * @param url 完整的URL
     * @return
     */
    public static HttpResponseModel<String> get(String url) {
        Request.Builder requestBuilder = new Request.Builder();
        // url
        requestBuilder.url(url);
        // execute
        requestBuilder.get();
        return toString(execute(requestBuilder.build()));
    }

    /**
     * GET request for JSON response body
     *
     * @param url    完整的URL
     * @param tClass 反序列化的类
     * @param <T>
     * @return
     */
    public static <T> HttpResponseModel<T> get(String url, Class<T> tClass) {
        return get(url, null, tClass);
    }

    /**
     * GET request for JSON response body
     *
     * @param url    完整的URL
     * @param header 请求头
     * @param tClass JSON反序列化的类
     * @param <T>
     * @return
     */
    public static <T> HttpResponseModel<T> get(String url, @Nullable Map<String, String> header, Class<T> tClass) {
        Request.Builder requestBuilder = new Request.Builder();
        // url
        requestBuilder.url(url);
        // header
        buildHeader(requestBuilder, header);
        // execute
        requestBuilder.get();
        Response response = execute(requestBuilder.build());
        return toBean(response, tClass);
    }

    /**
     * POST
     *
     * @param url    完整的URL
     * @param body   JSON序列化的bean
     * @param tClass JSON响应体反序列化为bean的类
     * @param <T>
     * @return
     */
    public static <T> HttpResponseModel<T> post(String url, @Nullable Object body, Class<T> tClass) {
        return post(url, body, null, tClass);
    }

    /**
     * POST
     *
     * @param url     完整的URL
     * @param body    JSON序列化的bean
     * @param headers 请求头
     * @param tClass  JSON响应体反序列化为bean的类
     * @param <T>
     * @return
     */
    public static <T> HttpResponseModel<T> post(String url, @Nullable Object body, @Nullable Map<String, String> headers, Class<T> tClass) {
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
     * 通用request 自行指定HTTP方法
     *
     * @param method  HTTP方法
     * @param url     完整的URL
     * @param body    JSON序列化的bean
     * @param headers 请求头
     * @return
     */
    public static HttpResponseModel<InputStream> request(String method, String url, @Nullable Object body, @Nullable Map<String, String> headers) {
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
     * request for byte[] response body
     *
     * @param method  HTTP方法
     * @param url     完整的URL
     * @param body    JSON序列化的bean
     * @param headers 请求头
     * @return
     */
    public static HttpResponseModel<String> requestForString(String method, String url, @Nullable Object body, @Nullable Map<String, String> headers) {
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
     * request for byte[] response body
     *
     * @param method  HTTP方法
     * @param url     完整的URL
     * @param body    JSON序列化的bean
     * @param headers 请求头
     * @return
     */
    public static HttpResponseModel<byte[]> requestForBytes(String method, String url, @Nullable Object body, @Nullable Map<String, String> headers) {
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
     * request for InputStream response body
     *
     * @param method  HTTP方法
     * @param url     完整的URL
     * @param body    JSON序列化的bean
     * @param headers 请求头
     * @return
     */
    public static HttpResponseModel<InputStream> requestForInputStream(String method, String url, @Nullable Object body, @Nullable Map<String, String> headers) {
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
            if (body instanceof String) {
                bodyJson = (String) body;
                return RequestBody.create(bodyJson, MEDIA_TYPE_TXT);
            } else {
                bodyJson = JacksonUtil.toJSONString(body);
                return RequestBody.create(bodyJson, MEDIA_TYPE_JSON);
            }
        }
        return RequestBody.create(bodyJson, MEDIA_TYPE_TXT);

    }

    /**
     * 执行request
     *
     * @param request
     * @return
     */
    private static Response execute(Request request) {
        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将QueryParam Map转化为key=xxx&key=xxx
     *
     * @param paramsMap
     * @return 如 key=xxx&key=xxx 这样的字符串
     */
    public static String convertQueryParamsMap2String(Map<String, ?> paramsMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> param : paramsMap.entrySet()) {
            if (param.getValue() instanceof String) {
                // 处理字符串 key=value
                sb.append(param.getKey()).append("=").append(param.getValue()).append("&");
            } else if (param.getValue() instanceof List || param.getValue() instanceof Set) {
                // 处理集合 key1=value1&key1=value2&key1=value3
                for (Object obj : (Collection<?>) param.getValue()) {
                    sb.append(param.getKey()).append("=").append(obj).append("&");
                }
            } else {
                // 其他类型处理
                sb.append(param.getKey()).append("=").append(param.getValue()).append("&");
            }
        }
        if (sb.length() > 0 && '&' == (sb.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private static Map<String, String> convertHeaders2Map(Headers headers) {
        Map<String, String> headerMap = new HashMap<>();
        if (headers != null) {
            for (Iterator<Pair<String, String>> it = headers.iterator(); it.hasNext(); ) {
                Pair<String, String> header = it.next();
                headerMap.put(header.component1(), header.component2());
            }
        }
        return headerMap;
    }

    /**
     * response body String
     *
     * @param response
     * @return
     */
    private static HttpResponseModel<String> toString(Response response) {
        String body = null;
        try {
            if (response.body() != null) {
                body = response.body().string();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return HttpResponseModel.<String>builder()
                .status(response.code())
                .headers(convertHeaders2Map(response.headers()))
                .body(body)
                .build();
    }

    /**
     * response body byte[]
     *
     * @param response
     * @return
     */
    private static HttpResponseModel<byte[]> toBytes(Response response) {
        byte[] body = null;
        try {
            if (response.body() != null) {
                body = response.body().bytes();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return HttpResponseModel.<byte[]>builder()
                .status(response.code())
                .headers(convertHeaders2Map(response.headers()))
                .body(body)
                .build();
    }

    /**
     * response body to bean
     *
     * @param response
     * @param tClass
     * @param <T>
     * @return
     */
    private static <T> HttpResponseModel<T> toBean(Response response, Class<T> tClass) {
        String body = null;
        T bean = null;
        try {
            if (response.body() != null && tClass != null) {
                body = response.body().string();
                bean = JacksonUtil.parseObject(body, tClass);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return HttpResponseModel.<T>builder()
                .status(response.code())
                .headers(convertHeaders2Map(response.headers()))
                .body(bean)
                .build();
    }

    /**
     * response body to InputStream
     *
     * @param response
     * @return
     */
    private static HttpResponseModel<InputStream> toInputStream(Response response) {
        InputStream body = null;
        if (response.body() != null) {
            body = response.body().byteStream();
        } else {
            return null;
        }
        return HttpResponseModel.<InputStream>builder()
                .status(response.code())
                .headers(convertHeaders2Map(response.headers()))
                .body(body)
                .build();
    }

    public static void main(String[] args) {
        String url = "http://www.kuaidi100.com/query?type=%E5%BF%AB%E9%80%92%E5%85%AC%E5%8F%B8%E4%BB%A3%E5%8F%B7&postid=%E5%BF%AB%E9%80%92%E5%8D%95%E5%8F%B7";
        HttpResponseModel<String> resp = OkHttpClientUtil.requestForString(Constant.HTTP_GET, url, null, null);
        System.out.println(resp.getBody());
    }
}
