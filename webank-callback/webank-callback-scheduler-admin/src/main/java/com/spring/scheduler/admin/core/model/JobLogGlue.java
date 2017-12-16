package com.spring.scheduler.admin.core.model;

/**
 * ClassName: JobLogGlue 
 * @Description: log for glue, used to track job code process
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public class JobLogGlue {
	
	private int id;
	private int jobId;				// ��������ID
	private String glueType;		// GLUE����	#com.core.glue.GlueTypeEnum
	private String glueSource;
	private String glueRemark;
	private String addTime;
	private String updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public String getGlueType() {
		return glueType;
	}

	public void setGlueType(String glueType) {
		this.glueType = glueType;
	}

	public String getGlueSource() {
		return glueSource;
	}

	public void setGlueSource(String glueSource) {
		this.glueSource = glueSource;
	}

	public String getGlueRemark() {
		return glueRemark;
	}

	public void setGlueRemark(String glueRemark) {
		this.glueRemark = glueRemark;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
