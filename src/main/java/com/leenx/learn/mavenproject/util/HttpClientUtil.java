package com.leenx.learn.mavenproject.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
     * request for JSON response body
     *
     * @param url     完整的URL
     * @param headers 请求头
     * @param body    请求体bean
     * @return
     */
    public static <T> HttpResponseModel<T> request(String method, String url, @Nullable Map<String, String> headers, @Nullable Object body, Class<T> tClass) {
        HttpUriRequest request = buildRequest(method, url, headers, body);
        // execute
        return toBean(execute(request), tClass);
    }

    /**
     * request for String response body
     *
     * @param url     完整的URL
     * @param headers 请求头
     * @param body    请求体bean
     * @return
     */
    public static HttpResponseModel<String> requestForString(String method, String url, @Nullable Map<String, String> headers, @Nullable Object body) {
        HttpUriRequest request = buildRequest(method, url, headers, body);
        // execute
        return toString(execute(request));
    }

    /**
     * request for byte[] response body
     *
     * @param url     完整的URL
     * @param headers 请求头
     * @param body    请求体bean
     * @return
     */
    public static HttpResponseModel<byte[]> requestForBytes(String method, String url, @Nullable Map<String, String> headers, @Nullable Object body) {
        HttpUriRequest request = buildRequest(method, url, headers, body);
        // execute
        return toBytes(execute(request));
    }

    /**
     * request for InputStream response body
     *
     * @param method  HTTP方法
     * @param url     完整的URL
     * @param headers 请求头
     * @param body    请求体bean
     * @return
     */
    public static HttpResponseModel<InputStream> requestForInputStream(String method, String url, @Nullable Map<String, String> headers, @Nullable Object body) {
        HttpUriRequest request = buildRequest(method, url, headers, body);
        // execute
        return toInputStream(execute(request));
    }

    /**
     * 构造请求
     *
     * @param method
     * @param url
     * @param headers
     * @param body
     * @return
     */
    private static HttpUriRequest buildRequest(String method, String url, @Nullable Map<String, String> headers, @Nullable Object body) {
        HttpUriRequest request = null;
        if (method == null || "".equals(method) || Constant.HTTP_GET.equals(method)) {
            request = RequestBuilder.get()
                    .setUri(url)
                    .build();
        } else {
            // handle body
            String reqBodyStr = body instanceof String ? (String) body : JacksonUtil.toJSONString(body);
            StringEntity stringEntity = new StringEntity(reqBodyStr, StandardCharsets.UTF_8);
            request = RequestBuilder
                    .create(method)
                    .setUri(url)
                    .setEntity(stringEntity)
                    .build();
        }
        // handle headers
        request.setHeaders(convertMap2Headers(headers));
        return request;
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

    /**
     * 执行请求
     *
     * @param request
     * @return
     */
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
        // 下载byte流demo
        String picUrl = "https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/baike/s=220/sign=8464c514b50e7bec27da04e31f2fb9fa/810a19d8bc3eb1358e9f66a0ab1ea8d3fd1f4455.jpg";
        HttpResponseModel<InputStream> responseModel = HttpClientUtil.requestForInputStream(Constant.HTTP_GET, picUrl, null, null);
        InputStream is = responseModel.getBody();
        OutputStream out = null;
        try {
            out = new FileOutputStream("/Users/leen/Desktop/pic");
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = is.read(buff)) != -1) {
                out.write(buff, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
