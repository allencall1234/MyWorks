package com.xj_pipe.bean;

import java.io.Serializable;

/**
 * 事故报修实体类
 * @author Administrator
 *
 */
public class FaultBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/**主键标识*/
	private int id;
	
	/**关联设备信息*/
	private String lineId;
	private String equipName;
	
	/**维修地点*/
	private String place;
	
	/**状态 */
	private String status;
	
	/**存在问题*/
	private String existProblem;
	
	/**缺陷类型*/
	private int defectType;
	private String typeName;
	
	/**登记人*/
	private String regist;
	private String registName;
	
	/**登记时间*/
	private String registTime;
	
	/**值班站长*/
	private String dutyStation;
	private String dutyStationName;
	
	/**缺陷上报时间*/
	private String defectTime;
	
	/**上报人*/
	private String report;
	private String reportName;
	
	/**缺陷处理人*/
	private String defectDeal;
	private String defectDealName;
	
	/**描述原因*/
	private String content;
	
	/**上报阶段文件名*/
	private String reportStageFileName;
	
	/**上报阶段文件编码*/
	private String reportStageFilePath;
	
	/**维修阶段文件名*/
	private String repairStageFileName;
	
	/**维修阶段文件路径*/
	private String repairStageFilePath;
	
	/**现场是否处理完成*/
	private String flag;
	
	/**场站内或场站外*/
	private String type;
	
	/**巡检主管审批人*/
	private String directorApprover;
	
	/**值班站长审批人*/
	private String stationApprover;
	
	/**值班调度审批人*/
	private String dispatchApprover;
	
	/**生产运行部经理审批人*/
	private String prodOperateMgrApprover;
	
	/**生产副总审批人*/
	private String prodGeneralApprover;
	
	/**巡检主管审批意见*/
	private String directorOpinion;
	
	/**值班站长审批意见*/
	private String stationOpinion;
	
	/**值班调度审批意见*/
	private String dispatchOpinion;
	
	/**生产运行部经理审批意见*/
	private String prodOperateMgrOpinion;
	
	/**生产副总审批意见*/
	private String prodGeneralOpinion;
	
	/**是否为重大缺陷/紧急*/
	private String importantEvent;
	
	/**维修计划开始日期*/
	private String repairPlanEffectDate;
	
	/**维修计划截止日期*/
	private String repairPlanExpireDate;
	
	/**维修计划开始日期*/
	private String repairActualEffectDate;
	
	/**维修计划截止日期*/
	private String repairActualExpireDate;
	
	/**维修处理描述*/
	private String repairSummary;
	
	/**值班调度核实意见*/
	private String verifyOpinion;
	
	/**经度*/
	private String longitude;
	
	/**纬度*/
	private String latitude;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
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

	public String getExistProblem() {
		return existProblem;
	}

	public void setExistProblem(String existProblem) {
		this.existProblem = existProblem;
	}

	public int getDefectType() {
		return defectType;
	}

	public void setDefectType(int defectType) {
		this.defectType = defectType;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public String getRegist() {
		return regist;
	}

	public void setRegist(String regist) {
		this.regist = regist;
	}

	public String getRegistName() {
		return registName;
	}

	public void setRegistName(String registName) {
		this.registName = registName;
	}

	public String getRegistTime() {
		return registTime;
	}

	public void setRegistTime(String registTime) {
		this.registTime = registTime;
	}

	public String getDutyStation() {
		return dutyStation;
	}

	public void setDutyStation(String dutyStation) {
		this.dutyStation = dutyStation;
	}

	public String getDutyStationName() {
		return dutyStationName;
	}

	public void setDutyStationName(String dutyStationName) {
		this.dutyStationName = dutyStationName;
	}

	public String getDefectTime() {
		return defectTime;
	}

	public void setDefectTime(String defectTime) {
		this.defectTime = defectTime;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
	public String getDefectDeal() {
		return defectDeal;
	}

	public void setDefectDeal(String defectDeal) {
		this.defectDeal = defectDeal;
	}

	public String getDefectDealName() {
		return defectDealName;
	}

	public void setDefectDealName(String defectDealName) {
		this.defectDealName = defectDealName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getReportStageFileName() {
		return reportStageFileName;
	}

	public void setReportStageFileName(String reportStageFileName) {
		this.reportStageFileName = reportStageFileName;
	}

	public String getReportStageFilePath() {
		return reportStageFilePath;
	}

	public void setReportStageFilePath(String reportStageFilePath) {
		this.reportStageFilePath = reportStageFilePath;
	}

	public String getRepairStageFileName() {
		return repairStageFileName;
	}

	public void setRepairStageFileName(String repairStageFileName) {
		this.repairStageFileName = repairStageFileName;
	}

	public String getRepairStageFilePath() {
		return repairStageFilePath;
	}

	public void setRepairStageFilePath(String repairStageFilePath) {
		this.repairStageFilePath = repairStageFilePath;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDirectorApprover() {
		return directorApprover;
	}

	public void setDirectorApprover(String directorApprover) {
		this.directorApprover = directorApprover;
	}

	public String getStationApprover() {
		return stationApprover;
	}

	public void setStationApprover(String stationApprover) {
		this.stationApprover = stationApprover;
	}

	public String getDispatchApprover() {
		return dispatchApprover;
	}

	public void setDispatchApprover(String dispatchApprover) {
		this.dispatchApprover = dispatchApprover;
	}

	public String getProdOperateMgrApprover() {
		return prodOperateMgrApprover;
	}

	public void setProdOperateMgrApprover(String prodOperateMgrApprover) {
		this.prodOperateMgrApprover = prodOperateMgrApprover;
	}

	public String getProdGeneralApprover() {
		return prodGeneralApprover;
	}

	public void setProdGeneralApprover(String prodGeneralApprover) {
		this.prodGeneralApprover = prodGeneralApprover;
	}

	public String getDirectorOpinion() {
		return directorOpinion;
	}

	public void setDirectorOpinion(String directorOpinion) {
		this.directorOpinion = directorOpinion;
	}
	
	public String getStationOpinion() {
		return stationOpinion;
	}

	public void setStationOpinion(String stationOpinion) {
		this.stationOpinion = stationOpinion;
	}
	
	public String getDispatchOpinion() {
		return dispatchOpinion;
	}

	public void setDispatchOpinion(String dispatchOpinion) {
		this.dispatchOpinion = dispatchOpinion;
	}
	
	public String getProdOperateMgrOpinion() {
		return prodOperateMgrOpinion;
	}

	public void setProdOperateMgrOpinion(String prodOperateMgrOpinion) {
		this.prodOperateMgrOpinion = prodOperateMgrOpinion;
	}
	
	public String getProdGeneralOpinion() {
		return prodGeneralOpinion;
	}

	public void setProdGeneralOpinion(String prodGeneralOpinion) {
		this.prodGeneralOpinion = prodGeneralOpinion;
	}

	public String getImportantEvent() {
		return importantEvent;
	}

	public void setImportantEvent(String importantEvent) {
		this.importantEvent = importantEvent;
	}
	
	public String getRepairPlanEffectDate() {
		return repairPlanEffectDate;
	}

	public void setRepairPlanEffectDate(String repairPlanEffectDate) {
		this.repairPlanEffectDate = repairPlanEffectDate;
	}

	public String getRepairPlanExpireDate() {
		return repairPlanExpireDate;
	}

	public void setRepairPlanExpireDate(String repairPlanExpireDate) {
		this.repairPlanExpireDate = repairPlanExpireDate;
	}

	public String getRepairActualEffectDate() {
		return repairActualEffectDate;
	}

	public void setRepairActualEffectDate(String repairActualEffectDate) {
		this.repairActualEffectDate = repairActualEffectDate;
	}

	public String getRepairActualExpireDate() {
		return repairActualExpireDate;
	}

	public void setRepairActualExpireDate(String repairActualExpireDate) {
		this.repairActualExpireDate = repairActualExpireDate;
	}

	public String getRepairSummary() {
		return repairSummary;
	}

	public void setRepairSummary(String repairSummary) {
		this.repairSummary = repairSummary;
	}

	public String getVerifyOpinion() {
		return verifyOpinion;
	}

	public void setVerifyOpinion(String verifyOpinion) {
		this.verifyOpinion = verifyOpinion;
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
	
}
