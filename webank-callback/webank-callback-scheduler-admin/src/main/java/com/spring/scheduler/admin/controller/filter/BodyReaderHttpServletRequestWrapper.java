package com.spring.scheduler.admin.controller.filter;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;

public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {  
  
    private final byte[] body;  
      
    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request)   
throws Exception {  
        super(request); 
        InputStream in= request.getInputStream();
        byte[] byt = new byte[request.getContentLength()];
        in.read(byt);
        body = byt;
        System.err.println();
    } 
    /** 
     * 通过BufferedReader和字符编码集转换成byte数组 
     * @param br 
     * @param encoding 
     * @return 
     * @throws IOException  
     */  
    private byte[] readBytes(BufferedReader br,String encoding) throws IOException{  
        String str = null,retStr="";  
        while ((str = br.readLine()) != null) {  
            retStr += str;  
        }  
        if (StringUtils.isNotBlank(retStr)) {  
             return retStr.getBytes(Charset.forName(encoding));  
        }  
        return null;  
    }  
    /** 
     * 读取流 
     *  
     * @param inStream 
     * @return 字节数组 
     * @throws Exception 
     */  
    public static byte[] readStream(BufferedReader inStream) throws Exception {  
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
        char[] buffer = new char[1024];  
        int len = 0;  
        while ((len = inStream.read(buffer)) != -1) {
        	byte[] bt= getBytes(buffer);
            outSteam.write(bt, 0, len);  
        }  
        outSteam.close();  
        inStream.close();  
        return outSteam.toByteArray();  
    } 
    private static byte[] getBytes (char[] chars) {
    	   Charset cs = Charset.forName ("UTF-8");
    	   CharBuffer cb = CharBuffer.allocate (chars.length);
    	   cb.put (chars);
    	                 cb.flip ();
    	   ByteBuffer bb = cs.encode (cb);
    	  
    	   return bb.array();

    	 }
    @Override  
    public BufferedReader getReader() throws IOException {  
        return new BufferedReader(new InputStreamReader(getInputStream()));  
    }  
  
    @Override  
    public ServletInputStream getInputStream() throws IOException {  
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);  
        return new ServletInputStream() {  
  
            @Override  
            public int read() throws IOException {  
                return bais.read();  
            }  
        };  
    }  
  
} 