package com.leenx.learn.mavenproject;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

/**
 * @author leen-x
 * @Description:
 * @date 2021/09/06 11:04 上午
 **/
public class RedisDemo {

    public static void main(String[] args) {

    }

    static Jedis initJedis() {
        JedisPoolConfig config = new JedisPoolConfig();
        String host = "localhost";
        int port = 6379;
        return new Jedis(host, 6379);
    }
}
