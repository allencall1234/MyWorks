package com.xj_pipe.bean;

import java.io.Serializable;

public class InspectionBean extends BaseBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;// 任务名称
	private int state;// 任务状态
	private String arrantTime;// 任务派发时间
	private String planStartTime;// 计划开始时间
	private String planEndTime;// 计划结束时间
	private String startTime;// 实际开始时间
	private String endTime;// 实际结束时间
	private String content;// 任务描述
	private String taskType;// 任务类型
	private String pointSet;// 巡检点id
	private String pointSetName;// 对应巡检点msid
	private String delayState;// 延误状态 0 未延误 1延误
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int getState() {
		return state;
	}
	
	@Override
	public void setState(int state) {
		this.state = state;
	}
	
	public String getArrantTime() {
		return arrantTime;
	}
	
	public void setArrantTime(String arrantTime) {
		this.arrantTime = arrantTime;
	}
	
	public String getPlanStartTime() {
		return planStartTime;
	}
	
	public void setPlanStartTime(String planStartTime) {
		this.planStartTime = planStartTime;
	}
	
	public String getPlanEndTime() {
		return planEndTime;
	}
	
	public void setPlanEndTime(String planEndTime) {
		this.planEndTime = planEndTime;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getTaskType() {
		return taskType;
	}
	
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
	public String getPointSet() {
		return pointSet;
	}
	
	public void setPointSet(String pointSet) {
		this.pointSet = pointSet;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPointSetName() {
		return pointSetName;
	}

	public void setPointSetName(String pointSetName) {
		this.pointSetName = pointSetName;
	}

	public String getDelayState() {
		return delayState;
	}

	public void setDelayState(String delayState) {
		this.delayState = delayState;
	}
	
}
