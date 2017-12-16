package com.spring.scheduler.core.biz.model;

import java.io.Serializable;
import java.util.Map;

/**
 * ClassName: ReturnT 
 * @Description: 
 * @author JornTang
 * @date 2017年8月17日
 */
public class ReturnT<T> implements Serializable {
	public static final long serialVersionUID = 42L;

	public static final int SUCCESS_CODE = 200;
	public static final int FAIL_CODE = 500;
	public static final ReturnT<String> SUCCESS = new ReturnT<String>(null);
	public static final ReturnT<String> FAIL = new ReturnT<String>(FAIL_CODE, null);
	
	private int code; 		//返回状态码 
	private String msg;		//返回信息
	private T content;		//返回内容
	private T extendResult; //返回扩展信息

	public ReturnT(){}
	public ReturnT(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public ReturnT(int code, String msg, T extendResult) {
		this.code = code;
		this.msg = msg;
		this.extendResult = extendResult;
	}
	public ReturnT(T content) {
		this.code = SUCCESS_CODE;
		this.content = content;
	}
	public T getExtendResult() {
		return extendResult;
	}
	public void setExtendResult(T extendResult) {
		this.extendResult = extendResult;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ReturnT [code=" + code + ", msg=" + msg + ", content=" + content + "]";
	}

}
