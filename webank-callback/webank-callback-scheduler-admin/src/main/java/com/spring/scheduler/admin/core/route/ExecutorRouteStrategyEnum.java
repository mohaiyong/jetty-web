package com.spring.scheduler.admin.core.route;

import com.spring.scheduler.admin.core.route.strategy.ExecutorRouteBusyover;
import com.spring.scheduler.admin.core.route.strategy.ExecutorRouteConsistentHash;
import com.spring.scheduler.admin.core.route.strategy.ExecutorRouteFailover;
import com.spring.scheduler.admin.core.route.strategy.ExecutorRouteFirst;
import com.spring.scheduler.admin.core.route.strategy.ExecutorRouteLFU;
import com.spring.scheduler.admin.core.route.strategy.ExecutorRouteLRU;
import com.spring.scheduler.admin.core.route.strategy.ExecutorRouteLast;
import com.spring.scheduler.admin.core.route.strategy.ExecutorRouteRandom;
import com.spring.scheduler.admin.core.route.strategy.ExecutorRouteRound;

/**
 * ClassName: ExecutorRouteStrategyEnum 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public enum ExecutorRouteStrategyEnum {

	FIRST("第一个", new ExecutorRouteFirst()),
    LAST("最后一个", new ExecutorRouteLast()),
    ROUND("轮询", new ExecutorRouteRound()),
    RANDOM("随机", new ExecutorRouteRandom()),
    CONSISTENT_HASH("一致性HASH", new ExecutorRouteConsistentHash()),
    LEAST_FREQUENTLY_USED("最不经常使用", new ExecutorRouteLFU()),
    LEAST_RECENTLY_USED("最近最久未使用", new ExecutorRouteLRU()),
    FAILOVER("故障转移", new ExecutorRouteFailover()),
    SHARDING_BROADCAST("分片",new ExecutorRouteFailover()),
    BUSYOVER("忙碌转移", new ExecutorRouteBusyover());

    ExecutorRouteStrategyEnum(String title, ExecutorRouter router) {
        this.title = title;
        this.router = router;
    }

    private String title;
    private ExecutorRouter router;

    public String getTitle() {
        return title;
    }
    public ExecutorRouter getRouter() {
        return router;
    }

    public static ExecutorRouteStrategyEnum match(String name, ExecutorRouteStrategyEnum defaultItem){
        if (name != null) {
            for (ExecutorRouteStrategyEnum item: ExecutorRouteStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }

}
