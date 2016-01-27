package com.xj_pipe.bean;

import java.io.Serializable;

public class PipeDotBean implements Serializable{
	private static final long serialVersionUID = 1L;

	/**主键标识*/
	private String id; 
	
	/**模板id*/
	private String templateId; 
	
	/**巡检点id(来自地图)*/
	private String pointId;
	
	/**巡检点名称(代号,来自地图)*/
	private String pointName;
	
	/**备注*/
	private String remarks;
	
	/**对应的点的集合的类型(1,模板对应2,紧急任务对应 3,普通巡检任务对应)*/
	private String type;
	
	/**是否签到(0:未签到,1:已签到)*/
	private String sign;
	
	/**经度*/
	private String longtitude;
	
	/**纬度*/
	private String latitude;
	
	/**1:单独选点,  2:由模板多点选点,  3:双击地图新增*/
	private String pointType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getPointType() {
		return pointType;
	}

	public void setPointType(String pointType) {
		this.pointType = pointType;
	}
	
}
