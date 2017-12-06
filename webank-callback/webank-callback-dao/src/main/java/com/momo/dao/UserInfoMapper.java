package com.momo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.momo.po.UserInfo;

public interface UserInfoMapper {

	/**
	 * 根据主键查询用户
	 * @param id
	 * @return
	 */
	UserInfo selectByPrimaryKey(Long id);

	/**
	 * 用户登录
	 * @param userInfo
	 * @return
	 */
	public UserInfo login(@Param("record") UserInfo userInfo);

	/**
	 * 增加用户
	 * @param userInfo
	 * @return
	 */
	public int addUser(@Param("record") UserInfo userInfo);

	/**
	 * 删除用户
	 * @param userInfo
	 * @return
	 */
	public int deleteUser(@Param("record") UserInfo userInfo);

	/**
	 * 更新用户
	 * @param userInfo
	 * @return
	 */
	public int updateUser(@Param("record") UserInfo userInfo);

	/**
	 * 查询用户
	 * @param userInfo
	 * @return
	 */
	public UserInfo getUser(@Param("record") UserInfo userInfo);

	/**
	 * 分页查询用户
	 * @param userInfo
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<UserInfo> getUserPageList(@Param("record") UserInfo userInfo, @Param("start") int start, @Param("limit") int limit);
}