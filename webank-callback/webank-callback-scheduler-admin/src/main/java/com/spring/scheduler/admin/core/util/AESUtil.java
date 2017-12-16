package com.spring.scheduler.admin.core.util;  
  
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
  
/**
 * ClassName: DESUtil 
 * @Description: 加密工具类 
 * 1.将byte[]转为各种进制的字符串 
 * 2.base 64 encode 
 * 3.base 64 decode 
 * 4.获取byte[]的md5值 
 * 5.获取字符串md5值 
 * 6.结合base64实现md5加密 
 * 7.AES加密 
 * 8.AES加密为base 64 code 
 * 9.AES解密 
 * 10.将base 64 code AES解密 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月23日
 */
public class AESUtil {  
      
    public static void main(String[] args) throws Exception {  
    	System.err.println(AESUtil.aesEncrypt(RandomUtil.getRandomNum(16), RandomUtil.getRandomNum(128)));
    }  
      
    /** 
     * 将byte[]转为各种进制的字符串 
     * @param bytes byte[] 
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制 
     * @return 转换后的字符串 
     */  
    public static String binary(byte[] bytes, int radix){  
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数  
    }  
      
    /** 
     * base 64 encode 
     * @param bytes 待编码的byte[] 
     * @return 编码后的base 64 code 
     */  
    public static String base64Encode(byte[] bytes){  
        return new BASE64Encoder().encode(bytes);  
    }  
      
    /** 
     * base 64 decode 
     * @param base64Code 待解码的base 64 code 
     * @return 解码后的byte[] 
     * @throws Exception 
     */  
    public static byte[] base64Decode(String base64Code) throws Exception{  
        return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);  
    }  
      
    /** 
     * 获取byte[]的md5值 
     * @param bytes byte[] 
     * @return md5 
     * @throws Exception 
     */  
    public static byte[] md5(byte[] bytes) throws Exception {  
        MessageDigest md = MessageDigest.getInstance("MD5");  
        md.update(bytes);  
        return md.digest();  
    }  
      
    /** 
     * 获取字符串md5值 
     * @param msg  
     * @return md5 
     * @throws Exception 
     */  
    public static byte[] md5(String msg) throws Exception {  
        return StringUtils.isEmpty(msg) ? null : md5(msg.getBytes());  
    }  
      
    /** 
     * 结合base64实现md5加密 
     * @param msg 待加密字符串 
     * @return 获取md5后转为base64 
     * @throws Exception 
     */  
    public static String md5Encrypt(String msg) throws Exception{  
        return StringUtils.isEmpty(msg) ? null : base64Encode(md5(msg));  
    }  
      
    /** 
     * AES加密 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的byte[] 
     * @throws Exception 
     */  
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(encryptKey.getBytes());
        kgen.init(128, random); 
  
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
          
        return cipher.doFinal(content.getBytes("utf-8"));  
    }  
      
    /** 
     * AES加密为base 64 code 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的base 64 code 
     * @throws Exception 
     */  
    public static String aesEncrypt(String content, String encryptKey) throws Exception {  
        return base64Encode(aesEncryptToBytes(content, encryptKey));  
    }  
      
    /** 
     * AES解密 
     * @param encryptBytes 待解密的byte[] 
     * @param decryptKey 解密密钥 
     * @return 解密后的String 
     * @throws Exception 
     */  
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(decryptKey.getBytes());
        kgen.init(128, random); 
          
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
        byte[] decryptBytes = cipher.doFinal(encryptBytes);  
          
        return new String(decryptBytes);  
    }  
      
    /** 
     * 将base 64 code AES解密 
     * @param encryptStr 待解密的base 64 code 
     * @param decryptKey 解密密钥 
     * @return 解密后的string 
     * @throws Exception 
     */  
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {  
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);  
    }  
     
    /**
     * 随机生成秘钥
     */
     public static String getKey() {
       try {
         KeyGenerator kg = KeyGenerator.getInstance("AES");
         kg.init(128);
         //要生成多少位，只需要修改这里即可128, 192或256
         SecretKey sk = kg.generateKey();
         byte[] b = sk.getEncoded();
         String s = byteToHexString(b);
         System.out.println(s);
         System.out.println("十六进制密钥长度为"+s.length());
         System.out.println("二进制密钥的长度为"+s.length()*4);
         return s;
       }catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
         System.out.println("没有此算法。");
       }
       return null;
     }
     /**
     * 使用指定的字符串生成秘钥
     */
     public static String getKeyByPass(String pass) {
       //生成秘钥
       try {
         KeyGenerator kg = KeyGenerator.getInstance("AES");
         //kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
         //SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以生成的秘钥就一样。
         SecretKey sk = kg.generateKey();
         SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
         random.setSeed(pass.getBytes());
         kg.init(128, random);
         byte[] b = sk.getEncoded();
         String s = byteToHexString(b);
         return s;
       }catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
         System.out.println("没有此算法。");
       }
       return null;
     }
     /**
     * byte数组转化为16进制字符串
     * @param bytes
     * @return
     */
     public static String byteToHexString(byte[] bytes) {
       StringBuffer sb = new StringBuffer();
       for (int i = 0; i < bytes.length; i++) {
         String strHex=Integer.toHexString(bytes[i]);
         if(strHex.length() > 3) {
           sb.append(strHex.substring(6));
         } else {
           if(strHex.length() < 2) {
             sb.append("0" + strHex);
           } else {
             sb.append(strHex);
           }
         }
       }
       return sb.toString();
   }
} 