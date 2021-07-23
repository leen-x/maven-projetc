package com.leenx.learn.mavenproject.util;

/**
 * @author leen-x
 * @Description:
 * @date 2021/07/21 7:38 下午
 **/
public class JSONException extends RuntimeException{
    public JSONException() {
    }

    public JSONException(String message) {
        super(message);
    }

    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }
}
