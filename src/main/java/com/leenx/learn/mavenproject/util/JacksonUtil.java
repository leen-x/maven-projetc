package com.leenx.learn.mavenproject.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author leen-x
 * @Description:
 * @date 2021/07/22 4:27 下午
 **/
public class JacksonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        // 对于空的对象转json的时候不抛出错误
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 设置输入时忽略在json字符串中存在但在java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 设置输出时包含属性的风格 包含为null的属性
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /**
     * 对象转json
     *
     * @param obj
     * @return json字符串
     */
    public static String toJSONString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    /**
     * json转对象
     *
     * @param json  json字符串
     * @param clazz 对象类型
     * @return 对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (json == null || json.equals("")) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    /**
     * json转List
     *
     * @param json  json字符串
     * @param clazz 对象类型
     * @return List<
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        if (json == null || json.equals("")) {
            return null;
        }
        try {
            return mapper.readValue(json, new TypeReference<List<T>>() {
            });
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    /**
     * json字符串转Map
     *
     * @param json json字符串
     * @return Map
     */
    public static Map<String, Object> parseMap(String json) {
        if (json == null || json.equals("")) {
            return null;
        }
        try {
            return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    /**
     * json转对象
     *
     * @param json  json字符串
     * @param typeReference
     * @return 对象
     */
    public static <T> T readValue(String json, TypeReference<T> typeReference) {
        if (json == null || json.equals("")) {
            return null;
        }
        try {
            return mapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }
}
