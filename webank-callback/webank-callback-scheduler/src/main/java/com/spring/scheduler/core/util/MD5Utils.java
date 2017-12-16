package com.spring.scheduler.core.util;


import java.io .UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
      
    public static String md5Base64Url(String str){
        //先进行MD5加密,加密完成字符串继续进行base64加密。
        String res = "";
        byte[] bytesMd5 = DigestUtils.md5(str);
          
        Base64 base64 = new Base64();
        //base64 加密
        byte[] byteBase64 = base64.encode(bytesMd5);
        res = new String(byteBase64);
        return res;
    }
      
    // md5+base64+url
    public static String encodeToUrlString(String str){
        String rev = "";
        try{
            str = java.net.URLEncoder.encode(str, "UTF-8");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] bytesMd5 = DigestUtils.md5(str);
        Base64 base64 = new Base64();
        byte[] byteBase64 = base64.encode(bytesMd5);
        rev = new String(byteBase64);
        return rev;
    }
    
    public static void main(String[] args) {
		System.out.println(stringToMd5("zaq12wsx@123"));
	}
    
    /*** 
     * MD5加码 生成32位md5码 
     */  
    public static String stringToMd5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){  
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().toUpperCase();
    }
}
