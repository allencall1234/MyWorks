package com.xj.dms.model;

import java.io.Serializable;

public class FaultTraceBean implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String id;// id
	private String place;// 发生位置
	private String status;// 故障状态
	private String breakdownIndex;// 故障索引
	private String happenTime;// 发生时间
	private String dutyMan;// 当班人员
	private String dispatchMan;// 调度人员
	private String tagname;// 标签
	private String discription;// 故障名称(描述)
	private String reason;// 发生原因
	private String solveTime;// 解决时间
	private String measures;// 处理措施
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPlace() {
		return place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getBreakdownIndex() {
		return breakdownIndex;
	}
	
	public void setBreakdownIndex(String breakdownIndex) {
		this.breakdownIndex = breakdownIndex;
	}
	
	public String getHappenTime() {
		return happenTime;
	}
	
	public void setHappenTime(String happenTime) {
		this.happenTime = happenTime;
	}
	
	public String getDutyMan() {
		return dutyMan;
	}
	
	public void setDutyMan(String dutyMan) {
		this.dutyMan = dutyMan;
	}
	
	public String getDispatchMan() {
		return dispatchMan;
	}
	
	public void setDispatchMan(String dispatchMan) {
		this.dispatchMan = dispatchMan;
	}
	
	public String getTagname() {
		return tagname;
	}
	
	public void setTagname(String tagname) {
		this.tagname = tagname;
	}
	
	public String getDiscription() {
		return discription;
	}
	
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getSolveTime() {
		return solveTime;
	}
	
	public void setSolveTime(String solveTime) {
		this.solveTime = solveTime;
	}
	
	public String getMeasures() {
		return measures;
	}
	
	public void setMeasures(String measures) {
		this.measures = measures;
	}
	
	

}
