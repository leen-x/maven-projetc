package com.leenx.learn.mavenproject;

/**
 * @author gongben
 * @Description: 对象克隆demo
 * @date 2021/08/02 8:03 下午
 **/
public class CloneDemo {
    public static class CloneableClass implements Cloneable {
        public int id;

        public String nickName;

        @Override
        protected CloneableClass clone() throws CloneNotSupportedException {
            return (CloneableClass) super.clone();
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        CloneableClass a = new CloneableClass();
        a.id = 1;
        a.nickName = "aaa";
        CloneableClass b = a.clone();
        System.out.println(b.nickName);

    }
}
