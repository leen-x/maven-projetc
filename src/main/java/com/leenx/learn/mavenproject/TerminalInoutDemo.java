package com.leenx.learn.mavenproject;

import java.util.Scanner;

/**
 * @author linsongxiong
 * @Description: 测试终端输入的命令
 * @date 2021/07/14 3:19 下午
 **/
public class TerminalInoutDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("input your name:(finish with enter)");
        String name = scanner.nextLine();
        System.out.println("welcome " + name);
        System.out.println("bey...");
    }
}
