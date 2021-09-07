package com.leenx.learn.mavenproject;

import com.alibaba.fastjson.JSON;

/**
 * @author leen-x
 * @Description:
 * @date 2021/07/22 2:12 下午
 **/
public class SerializableDemo {
    public static class Demo {
        public Demo demoObj;

        public String name;
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.demoObj = demo;
        demo.name = "abc";
        System.out.println(JSON.toJSON(demo));
    }
}
