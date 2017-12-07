package com.momo.diamond;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.momo.util.diamond.TshPropertyPlaceholderConfigurer;

/**
 * 初始化配置信息
 */
public class DiamondClient extends TshPropertyPlaceholderConfigurer {

	private static DiamondClient tshDiamondClient = new DiamondClient();

	public static DiamondClient getInstance() {
		return tshDiamondClient;
	}

	public void init() {
		//1.待加载数据列表
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		//2.加载的组
		Map<String, String> map = Maps.newConcurrentMap();
		map.put("group", "webank_callback");
		map.put("dataId", "mysql");
		list.add(map);

		list.add(ImmutableMap.of("group", "webank_callback", "dataId", "common"));

		//3.加载配置
		this.loadMultConfigFromDiamond(list);
	}

}