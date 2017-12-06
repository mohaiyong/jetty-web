package com.momo.po;

import java.util.Date;

/**
 * 用户信息实体
 * 
 * @author <a href="mailto:mohaiyong@mucfc.com">莫海涌</a>
 * @company 招联消费金融有限公司 版权所有 (c) 2015
 * @version 2015年7月17日
 */
public class UserInfo {

	private Long id;

	private String userid;

	private String username;

	private String password;

	private String salt;

	private Integer trynum;

	private String email;

	private String mobile;

	private String address;

	private String state;

	private Date createtime;

	private Date updatetime;

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getTrynum() {
		return trynum;
	}

	public void setTrynum(Integer trynum) {
		this.trynum = trynum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}