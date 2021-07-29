package com.leenx.learn.mavenproject.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author leen-x
 * @Description: apache HttpClient 工具类
 * @date 2021/07/29 10:50 上午
 **/
public class HttpClientUtil {

    // 单例
    private static final CloseableHttpClient httpClient;

    static {
        // 初始化HttpClient
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(60000)
                .setSocketTimeout(15000)
                .build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    /**
     * GET request for String response body
     *
     * @param url 完整的URL
     * @return
     */
    public static HttpResponseModel<String> get(String url, @Nullable Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeaders(convertMap2Headers(headers));
        CloseableHttpResponse httpResponse = execute(httpGet);
        return toString(httpResponse);
    }

    /**
     * POST request for String response body
     *
     * @param url     完整URL
     * @param headers 请求头
     * @param body    请求体
     * @return
     */
    public static HttpResponseModel<String> post(String url, @Nullable Map<String, String> headers, Object body) {
        // handle url
        HttpPost httpPost = new HttpPost(url);
        // handle headers
        httpPost.setHeaders(convertMap2Headers(headers));
        // handle body
        String reqBodyStr = body instanceof String ? (String) body : JacksonUtil.toJSONString(body);
        StringEntity stringEntity = new StringEntity(reqBodyStr, StandardCharsets.UTF_8);
        // execute
        return toString(execute(httpPost));
    }

    /**
     * POST request for JSON response body
     *
     * @param url     完整URL
     * @param headers 请求头
     * @param body    请求体
     * @param tClass  JSON响应体反序列化为bean的类
     * @return
     */
    public static <T> HttpResponseModel<T> post(String url, @Nullable Map<String, String> headers, @Nullable Object body, Class<T> tClass) {
        // handle url
        HttpPost httpPost = new HttpPost(url);
        // handle headers
        httpPost.setHeaders(convertMap2Headers(headers));
        // handle body
        String reqBodyStr = body instanceof String ? (String) body : JacksonUtil.toJSONString(body);
        StringEntity stringEntity = new StringEntity(reqBodyStr, StandardCharsets.UTF_8);
        // execute
        CloseableHttpResponse httpResponse = execute(httpPost);
        return toBean(execute(httpPost), tClass);
    }

    /**
     * POST request for String response body
     *
     * @param url     完整的URL
     * @param headers 请求头
     * @param body    请求体bean
     * @return
     */
    public static HttpResponseModel<String> requestForString(String url, @Nullable Map<String, String> headers, @Nullable Object body) {
        // handle url
        HttpPost httpPost = new HttpPost(url);
        // handle headers
        httpPost.setHeaders(convertMap2Headers(headers));
        // handle body
        String reqBodyStr = body instanceof String ? (String) body : JacksonUtil.toJSONString(body);
        StringEntity stringEntity = new StringEntity(reqBodyStr, StandardCharsets.UTF_8);
        // execute
        CloseableHttpResponse httpResponse = execute(httpPost);
        return toString(execute(httpPost));
    }

    /**
     * request for byte[] response body
     *
     * @param url     完整的URL
     * @param headers 请求头
     * @param body    请求体bean
     * @return
     */
    public static HttpResponseModel<byte[]> requestForBytes(String url, @Nullable Map<String, String> headers, @Nullable Object body) {
        // handle url
        HttpPost httpPost = new HttpPost(url);
        // handle headers
        httpPost.setHeaders(convertMap2Headers(headers));
        // handle body
        String reqBodyStr = body instanceof String ? (String) body : JacksonUtil.toJSONString(body);
        StringEntity stringEntity = new StringEntity(reqBodyStr, StandardCharsets.UTF_8);
        // execute
        CloseableHttpResponse httpResponse = execute(httpPost);
        return toBytes(execute(httpPost));
    }

    /**
     * request for InputStream response body
     *
     * @param url     完整的URL
     * @param headers 请求头
     * @param body    请求体bean
     * @return
     */
    public static HttpResponseModel<InputStream> requestForInputStream(String url, @Nullable Map<String, String> headers, @Nullable Object body) {
        // handle url
        HttpPost httpPost = new HttpPost(url);
        // handle headers
        httpPost.setHeaders(convertMap2Headers(headers));
        // handle body
        String reqBodyStr = body instanceof String ? (String) body : JacksonUtil.toJSONString(body);
        StringEntity stringEntity = new StringEntity(reqBodyStr, StandardCharsets.UTF_8);
        // execute
        return toInputStream(execute(httpPost));
    }


    private static CloseableHttpResponse execute(HttpUriRequest request) {
        try {
            CloseableHttpResponse response = null;
            response = httpClient.execute(request);
            return response;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * response body to bean
     *
     * @param httpResponse 响应
     * @param tClass       JSON响应体反序列化为bean的类
     * @param <T>
     * @return
     */
    private static <T> HttpResponseModel<T> toBean(CloseableHttpResponse httpResponse, Class<T> tClass) {
        HttpResponseModel<T> responseModel = null;
        HttpEntity entity = httpResponse.getEntity();
        String responseBody = null;
        if (entity != null) {
            try {
                responseBody = EntityUtils.toString(entity, "UTF-8");
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
        return HttpResponseModel.<T>builder()
                .status(httpResponse.getStatusLine().getStatusCode())
                .headers(convertHeaders2Map(httpResponse.getAllHeaders()))
                .body(JacksonUtil.parseObject(responseBody, tClass))
                .build();
    }

    /**
     * response body to String
     *
     * @param httpResponse 响应
     * @return
     */
    private static HttpResponseModel<String> toString(CloseableHttpResponse httpResponse) {
        HttpResponseModel<String> responseModel = null;
        HttpEntity entity = httpResponse.getEntity();
        String responseBody = null;
        if (entity != null) {
            try {
                responseBody = EntityUtils.toString(entity, "UTF-8");
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
        return HttpResponseModel.<String>builder()
                .status(httpResponse.getStatusLine().getStatusCode())
                .headers(convertHeaders2Map(httpResponse.getAllHeaders()))
                .body(responseBody)
                .build();
    }

    /**
     * response body to byte[]
     *
     * @param httpResponse 响应
     * @return {@link HttpResponseModel<byte[]>}
     */
    private static HttpResponseModel<byte[]> toBytes(CloseableHttpResponse httpResponse) {
        HttpResponseModel<byte[]> responseModel = null;
        HttpEntity entity = httpResponse.getEntity();
        byte[] responseBody = null;
        if (entity != null) {
            try {
                responseBody = EntityUtils.toByteArray(entity);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
        return HttpResponseModel.<byte[]>builder()
                .status(httpResponse.getStatusLine().getStatusCode())
                .headers(convertHeaders2Map(httpResponse.getAllHeaders()))
                .body(responseBody)
                .build();
    }

    /**
     * response body to InputStream
     *
     * @param httpResponse 响应
     * @return {@link HttpResponseModel<InputStream>}
     */
    private static HttpResponseModel<InputStream> toInputStream(CloseableHttpResponse httpResponse) {
        HttpResponseModel<InputStream> responseModel = null;
        HttpEntity entity = httpResponse.getEntity();
        InputStream responseBody = null;
        if (entity != null) {
            try {
                responseBody = entity.getContent();
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
        return HttpResponseModel.<InputStream>builder()
                .status(httpResponse.getStatusLine().getStatusCode())
                .headers(convertHeaders2Map(httpResponse.getAllHeaders()))
                .body(responseBody)
                .build();
    }

    /**
     * 将QueryParam Map转化为key=xxx&key=xxx
     *
     * @param paramsMap QueryParam Map
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

    private static Header[] convertMap2Headers(Map<String, String> headerMap) {
        Header[] headers = null;
        if (headerMap != null && !headerMap.isEmpty()) {
            headers = new Header[headerMap.size()];
            int i = 0;
            for (Map.Entry<String, String> header : headerMap.entrySet()) {
                headers[i++] = new BasicHeader(header.getKey(), header.getValue());
            }
        }
        return headers;
    }

    private static Map<String, String> convertHeaders2Map(Header[] headers) {
        Map<String, String> headerMap = null;
        if (headers != null && headers.length > 0) {
            headerMap = new HashMap<>();
            for (Header header : headers) {
                headerMap.put(header.getName(), header.getValue());
            }
        }
        return headerMap;
    }

    private static Boolean isCollectionNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static void main(String[] args) {
        System.out.println("" instanceof String);
    }
}
