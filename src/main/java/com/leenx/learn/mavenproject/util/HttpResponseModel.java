package com.leenx.learn.mavenproject.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author gongben
 * @Description: Http响应模型，包含状态、响应头、响应体等
 * @date 2021/07/29 2:25 下午
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponseModel<T> {
    /**
     * HTTP 响应状态码
     */
    private Integer status;

    /**
     * HTTP 响应头
     */
    private Map<String, String> headers;

    /**
     * HTTP 响应体
     */
    private T body;
}
