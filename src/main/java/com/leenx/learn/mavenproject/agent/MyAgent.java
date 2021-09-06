package com.leenx.learn.mavenproject.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author leen-x
 * @Description:
 * @date 2021/08/27 7:56 下午
 **/
public class MyAgent {
    public static void premain(String args, Instrumentation instrumentation) throws Exception {
        System.out.println("Hello javaagent permain:"+args);
    }
}
