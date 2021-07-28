package com.leenx.learn.mavenproject.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * @author leen-x
 * @Description:
 * @date 2021/07/27 7:01 下午
 **/
public class AESUtil {
    private static final String SALT = "bq0ZtoDOjGg=";  //盐值
    private static byte[] secret = null;

    /**
     * 加密数据
     * @param data 待加密数据
     * @return
     */
    public static String encrypt(String data) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");  //密钥生成器
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG"); //创建强随机数对象
            secureRandom.setSeed(SALT.getBytes()); //传入盐值作为种子
            kgen.init(128, new SecureRandom(SALT.getBytes())); //创建128位的密钥，AES的密钥有128、192、256
            SecretKey key = kgen.generateKey(); //生成key
            secret = key.getEncoded();  //拿到正真的密钥，可以给需要的地方使用
            /**
             * 创建密码器，Cipher支持AES，DES，DESede(DES3)和RSA四种算法的加密操作，这里着重讲AES
             * Cipher.getInstance("算法/模式/填充模式")
             * AES模式有CBC(有向量模式)和ECB(无向量模式)，向量模式可以简单理解为偏移量，使用CBC模式需要定义一个IvParameterSpec对象
             * AES填充模式:有好几种，不做过多介绍
             *   PKCS5Padding:Cipher的默认实现的填充方式
             *   PKCS7Padding:可以与PKCS5Padding相互加解密，如前端crypto.js使用的AES默认实现的填充方式
             */
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); //可以简化写成Cipher.getInstance("AES")，默认使用AES/ECB/PKCS5Padding
            cipher.init(Cipher.ENCRYPT_MODE, key); //加密初始化
            //byte[] iv = "1234567890abcdef".getBytes("utf-8"); //向量长度需要和密钥一致，这里密钥是128位除8就是16字节
            //cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv)); //使用CBC模式，需要一个向量iv，可增加加密算法的强度

            //生成随机iv
            //byte[] iv =  new byte[16];
            //secureRandom.nextBytes(iv);

            return Base64.encodeBase64String(cipher.doFinal(data.getBytes(Constant.UTF_8))); //对数据加密后返回base64编码后的字符串
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 解密数据
     * @param data 待解密数据
     * @return
     */
    public static String decrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(secret, "AES"); //使用密钥创建AES的key
            cipher.init(Cipher.DECRYPT_MODE, key); //解密初始化

            //使用CBC向量模式解密
            //byte[] iv = "1234567890abcdef".getBytes("utf-8");
            //cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

            return new String(cipher.doFinal(Base64.decodeBase64(data)),"utf-8"); //对base64编码后的加密数据进行解密
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 解密方法2
     * 使用盐值再次生成密钥进行解密
     * @param data 待解密数据
     * @return
     */
    public static String decrypt2(String data) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(SALT.getBytes());
            kgen.init(128, new SecureRandom(SALT.getBytes()));
            SecretKey key = kgen.generateKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.decodeBase64(data)),"utf-8");
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("加密后：" + AESUtil.encrypt("cmbc.123"));
        System.out.println("解密后：" + AESUtil.decrypt("cuxzLian8EZfvhrilx1uJQ=="));
        System.out.println("秘钥：" + Base64.encodeBase64String(AESUtil.secret));
    }
}
