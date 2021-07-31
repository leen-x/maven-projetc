package com.leenx.learn.mavenproject.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author leen-x
 * @Description: 散列函数工具类
 * @date 2021/07/30 2:37 下午
 **/
public class HashingUtil {
    public static final String MD5 = "MD5";

    public static final String SHA_1 = "SHA-1";

    public static final String SHA_256 = "SHA-256";

    public static final String SHA_512 = "SHA-512";

    /**
     * MD5编码 字符串
     *
     * @param source 需MD5编码的字符串
     * @return byte[] 128位 or null
     */
    public static byte[] md5(String source) {
        if (source == null) {
            return null;
        }
        byte[] sourceBytes = source.getBytes(StandardCharsets.UTF_8);
        return md5(sourceBytes);
    }

    /**
     * MD5编码 字节
     *
     * @param source 需MD5编码的字节
     * @return byte[] 128位 or null
     */
    public static byte[] md5(byte[] source) {
        if (source == null) {
            return null;
        }
        MessageDigest digest = instance(MD5);
        return digest(digest, source);
    }

    /**
     * MD5编码 字符串
     *
     * @param source 需MD5编码的字符串
     * @return String 32位
     */
    public static String md5Hex(String source) {
        if (source == null) {
            return null;
        }
        byte[] bytes = md5(source);
        return bytesHex(bytes);
    }

    /**
     * MD5编码 字节
     *
     * @param source 需MD5编码的字节
     * @return String 32位
     */
    public static String md5Hex(byte[] source) {
        byte[] bytes = md5(source);
        return bytesHex(bytes);
    }

    /**
     * MD5编码 流
     *
     * @param source 需MD5编码的字节
     * @return byte[] 128bit or null
     */
    public static byte[] md5(final InputStream source) throws IOException {
        if (source == null) {
            return null;
        }
        MessageDigest digest = instance(MD5);
        final byte[] buffer = new byte[1024];
        int read = source.read(buffer, 0, 1024);
        while (read > -1) {
            digest.update(buffer, 0, read);
            read = source.read(buffer, 0, 1024);
        }
        byte[] bytes = digest.digest();
        return digest.digest();
    }

    /**
     * MD5编码 流
     *
     * @param source 需MD5编码的字节
     * @return String 32位 or null
     */
    public static String md5Hex(final InputStream source) throws IOException {
        byte[] md5bytes = md5(source);
        return bytesHex(md5bytes);
    }

    /**
     * SHA-1编码 字符串
     *
     * @param source 需MD5编码的字符串
     * @return byte[] 128位 or null
     */
    public static byte[] sha1(String source) {
        if (source == null) {
            return null;
        }
        byte[] sourceBytes = source.getBytes(StandardCharsets.UTF_8);
        return sha1(sourceBytes);
    }

    /**
     * SHA-1编码 字节
     *
     * @param source 需MD5编码的字节
     * @return byte[] 128位 or null
     */
    public static byte[] sha1(byte[] source) {
        MessageDigest digest = instance(SHA_1);
        return digest(digest, source);
    }

    /**
     * SHA-1编码 字符串
     *
     * @param source 需MD5编码的字符串
     * @return String 32位
     */
    public static String sha1Hex(String source) {
        if (source == null) {
            return null;
        }
        return bytesHex(sha1(source));
    }

    /**
     * SHA-1编码 字节
     *
     * @param source 需MD5编码的字节
     * @return String 32位
     */
    public static String sha1Hex(byte[] source) {
        if (source == null) {
            return null;
        }
        return bytesHex(sha1(source));
    }

    /**
     * SHA-1编码 流
     *
     * @param source 需MD5编码的字节
     * @return byte[] 128bit or null
     */
    public static byte[] sha1(final InputStream source) {
        if (source == null) {
            return null;
        }
        MessageDigest digest = instance(SHA_1);
        final byte[] buffer = new byte[1024];
        int read = 0;
        try {
            read = source.read(buffer, 0, 1024);
            while (read > -1) {
                digest.update(buffer, 0, read);
                read = source.read(buffer, 0, 1024);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return digest.digest();
    }

    /**
     * SHA-1编码 流
     *
     * @param source 需MD5编码的字节
     * @return String 32位 or null
     */
    public static String sha1Hex(final InputStream source) throws IOException {
        return bytesHex(sha1(source));
    }

    public static MessageDigest instance(String name) {
        try {
            return MessageDigest.getInstance(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] digest(MessageDigest digest, byte[] source) {
        byte[] bytes = null;
        if (source == null) {
            return null;
        }
        try {
            digest.update(source);
            bytes = digest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    private static String bytesHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte byt : bytes) {
            int b = (0x000000FF & byt) | 0xFFFFFF00;
            result.append(Integer.toHexString(b).substring(6));
        }
        return result.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        File file = new File("/Users/leen/Desktop/pic.jpg");
        String s = HashingUtil.sha1Hex("hahhahahahha");
        System.out.println(s);
        System.out.println(s.length());
    }
}
