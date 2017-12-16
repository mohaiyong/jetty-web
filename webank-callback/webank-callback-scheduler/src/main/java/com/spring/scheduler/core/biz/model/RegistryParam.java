package com.spring.scheduler.core.biz.model;

import java.io.Serializable;

/**
 * ClassName: RegistryParam 
 * @Description: 
 * @author JornTang
 * @date 2017年8月17日
 */
public class RegistryParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private String registGroup;
    private String registryKey;
    private String registryValue;
    private String executorClient;
    private String clientName;

    public RegistryParam(){}
    public RegistryParam(String registGroup, String registryKey, String registryValue) {
        this.registGroup = registGroup;
        this.registryKey = registryKey;
        this.registryValue = registryValue;
    }
    public RegistryParam(String registGroup, String executorClient, String clientName, String registryValue) {
        this.registGroup = registGroup;
        this.executorClient = executorClient;
        this.clientName = clientName;
        this.registryValue = registryValue;
    }
    public String getExecutorClient() {
		return executorClient;
	}
	public void setExecutorClient(String executorClient) {
		this.executorClient = executorClient;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getRegistGroup() {
        return registGroup;
    }

    public void setRegistGroup(String registGroup) {
        this.registGroup = registGroup;
    }

    public String getRegistryKey() {
        return registryKey;
    }

    public void setRegistryKey(String registryKey) {
        this.registryKey = registryKey;
    }

    public String getRegistryValue() {
        return registryValue;
    }

    public void setRegistryValue(String registryValue) {
        this.registryValue = registryValue;
    }

    @Override
    public String toString() {
        return "RegistryParam{" +
                "registGroup='" + registGroup + '\'' +
                ", registryKey='" + registryKey + '\'' +
                ", registryValue='" + registryValue + '\'' +
                ", executorClient='" + executorClient + '\'' +
                ", clientName='" + clientName + '\'' +
                '}';
    }
}
