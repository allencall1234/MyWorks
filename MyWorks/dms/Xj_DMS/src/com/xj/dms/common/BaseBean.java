package com.xj.dms.common;

public class BaseBean 
{
	private String taskStatus;// 任务查询状态
	private String taskTitle;// 任务查询标题
	private String taskType;// 任务查询类型
	private String taskTime;// 任务查询时间
	private String faultlocation;// 故障位置
	
	public String getTaskStatus() {
		return taskStatus;
	}
	
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	public String getTaskTitle() {
		return taskTitle;
	}
	
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	
	public String getTaskType() {
		return taskType;
	}
	
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
	public String getTaskTime() {
		return taskTime;
	}
	
	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}
	
	public String getFaultlocation() {
		return faultlocation;
	}
	
	public void setFaultlocation(String faultlocation) {
		this.faultlocation = faultlocation;
	}
	

}
