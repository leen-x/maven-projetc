package com.leenx.learn.mavenproject.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TIOStreamTransport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author gongben
 * @Description:
 * @date 2021/07/31 5:14 下午
 **/
public class ThriftDemo {
    public static void main(String[] args) throws TException, IOException {
        readData();
    }

    public static void writeData() throws IOException, TException {
        Pair pair = new Pair();
        pair.setValue1("value1---");
        pair.setValue2("value2---");
        FileOutputStream fos = new FileOutputStream(new File("pair"));
        pair.write(new TBinaryProtocol(new TIOStreamTransport(fos)));
        fos.close();
    }

    public static void readData() throws IOException, TException {
        Pair pair = new Pair();
        FileInputStream fis = new FileInputStream(new File("pair"));
        pair.read(new TBinaryProtocol(new TIOStreamTransport(fis)));
        System.out.println("key => " + pair.getValue1());
        System.out.println("value => " + pair.getValue2());
        fis.close();
    }
}
