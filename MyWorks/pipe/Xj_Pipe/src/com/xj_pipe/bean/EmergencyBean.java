package com.xj_pipe.bean;

import java.io.Serializable;

/**
 * 突发事件实体类
 * @author Administrator
 *
 */
public class EmergencyBean implements Serializable
{
	private static final long serialVersionUID = 1750602623988622911L;

	/**主键标识*/
	private String id;
	
	/**突发事件编号：EV+系统时间*/
	private String eventCode;
	
	/**突发事件标题*/
	private String title;
	
	/**突发事件描述*/
	private String description;
	
	/**上报人：巡检人员Id*/
	private String creater;
	private String createrName;
	
	/**上报时间*/
	private String createTime;
	
	/**发生时间*/
	private String happenDate;
	
	/**值班调度人员Id*/
	private String routine;
	private String routineName;
	
	/**值班调度填写时间*/
	private String routineTime;
	
	/**到场时间*/
	private String comeDate;
	
	/**处理时间*/
	private String handleDate;
	
	/**处理部门*/
	private String handleUnit;
	
	/**处理相关描述*/
	private String handleDesc;
	
	/**状态：  1发起待处理 2：处理待跟踪 3：跟踪完成*/
	private String state;
	
	/**发生区域*/
	private String area;
	
	/**经纬度*/
	private String longitude;
	private String latitude;
	
	/**发生类型  1：场站外  2：场站内*/
	private String type;
	
	/**附件路径*/
	private String filePath;
	private String fileName;
	
	/**时间区间查询*/
	private String startTime;
	private String endTime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getHappenDate() {
		return happenDate;
	}

	public void setHappenDate(String happenDate) {
		this.happenDate = happenDate;
	}

	public String getRoutine() {
		return routine;
	}

	public void setRoutine(String routine) {
		this.routine = routine;
	}

	public String getRoutineTime() {
		return routineTime;
	}

	public void setRoutineTime(String routineTime) {
		this.routineTime = routineTime;
	}

	public String getComeDate() {
		return comeDate;
	}

	public void setComeDate(String comeDate) {
		this.comeDate = comeDate;
	}

	public String getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(String handleDate) {
		this.handleDate = handleDate;
	}

	public String getHandleUnit() {
		return handleUnit;
	}

	public void setHandleUnit(String handleUnit) {
		this.handleUnit = handleUnit;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getRoutineName() {
		return routineName;
	}

	public void setRoutineName(String routineName) {
		this.routineName = routineName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getHandleDesc() {
		return handleDesc;
	}

	public void setHandleDesc(String handleDesc) {
		this.handleDesc = handleDesc;
	}

}
