package com.xj_pipe.bean;

import java.io.Serializable;
/**
 * 施工实体类
 * @author qinfan
 *
 * 2015年12月31日
 */
public class BuildBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2080014361696115644L;

	/**主键标识*/
	private String id;
	
	/**施工编号*/
	private String code;
	
	/**施工单位*/
	private String unit;
	
	/**施工区域*/
	private String area;
	
	/**完成日期*/
	private String finishDate;
	
	/**预计施工开始日期*/
	private String beginTime;
	
	/**预计施工结束日期*/
	private String endTime;
	
	/**是否动火 0：否 1：是*/
	private String isFire;
	
	/**施工总结 */
	private String content;
	
	/**上报人*/
	private String creater;
	private String createrName;
	
	/**上报时间时间*/
	private String createTime;
	
	/**总结填写人*/
	private String updater;
	private String updaterName;
	
	/**填写时间*/
	private String updateTime;
	
	/**状态 1：发起待处理 2：忽略 3：完成*/
	private String state;
	
	/**经纬度*/
	private String longitude;
	private String latitude;
	
	/**发生类型  1：场站外  2：场站内*/
	private String type;

	/**用于模糊查询*/
	private String startTime1;
	private String endTime1;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getIsFire() {
		return isFire;
	}
	public void setIsFire(String isFire) {
		this.isFire = isFire;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	public String getUpdaterName() {
		return updaterName;
	}
	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	public String getStartTime1() {
		return startTime1;
	}
	public void setStartTime1(String startTime1) {
		this.startTime1 = startTime1;
	}
	public String getEndTime1() {
		return endTime1;
	}
	public void setEndTime1(String endTime1) {
		this.endTime1 = endTime1;
	} 
}
