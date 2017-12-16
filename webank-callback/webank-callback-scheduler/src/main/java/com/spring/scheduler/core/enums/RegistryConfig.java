package com.spring.scheduler.core.enums;

/**
 * ClassName: RegistryConfig 
 * @Description: 注册配置
 * @author JornTang
 * @date 2017年8月17日
 */
public class RegistryConfig {

    public static final int BEAT_TIMEOUT = 30;
    public static final int DEAD_TIMEOUT = BEAT_TIMEOUT * 3;

    public enum RegistType{ EXECUTOR, ADMIN }

}
