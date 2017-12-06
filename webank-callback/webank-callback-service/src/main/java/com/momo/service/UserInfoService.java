package com.momo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.momo.dao.UserInfoMapper;
import com.momo.po.UserInfo;

@Service("userInfoService")
public class UserInfoService {

	@Autowired
	private UserInfoMapper userInfoMapper;

	public List<UserInfo> getUserPageList(UserInfo userInfo, int curPage, int pageSize) {
		return userInfoMapper.getUserPageList(userInfo, curPage, pageSize);
	}

}