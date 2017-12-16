package com.spring.scheduler.core.enums;


/**
 * ClassName: ExecutorBlockStrategyEnum 
 * @Description: 
 * @author JornTang
 * @date 2017年8月17日
 */
public enum ExecutorBlockStrategyEnum {

    SERIAL_EXECUTION("单机串行"),
    /*CONCURRENT_EXECUTION("并行"),*/
    DISCARD_LATER("丢弃后续调度"),
    COVER_EARLY("覆盖之前调度");

    private final String title;
    private ExecutorBlockStrategyEnum (String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public static ExecutorBlockStrategyEnum match(String name, ExecutorBlockStrategyEnum defaultItem) {
        if (name != null) {
            for (ExecutorBlockStrategyEnum item:ExecutorBlockStrategyEnum.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }
}