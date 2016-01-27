package com.xj.cnooc.model;

import java.io.Serializable;

public class FaultSupportModel implements Serializable {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String title;// 支持简述标题
	private String createTime;// 发起时间
	private String endTime;// 截止时间
	private String createrName;// 发起人
	private String description;// 描述
	private String electricNetName;// 所属电网
	private String electricRunTypeName;// 电网运行方式
	private String factoryUserName;// 支持厂家人员
	private String supportSortName;// 故障分类
	private String type;// 故障类型
	private String proficientName;// 支持专家

	private String approvalContent;// 审批建议
	private String approvalUserName;// 审批人
	private String superName;// 填写报告的电力调度名字
	private String conclusion;// 电力调度填写的报告结论
	private String competen_advice;// 入库结论
	private String competen_grade;// 报告评级

	public String getApprovalContent() {
		return approvalContent;
	}

	public void setApprovalContent(String approvalContent) {
		this.approvalContent = approvalContent;
	}

	public String getApprovalUserName() {
		return approvalUserName;
	}

	public void setApprovalUserName(String approvalUserName) {
		this.approvalUserName = approvalUserName;
	}

	public String getProficientName() {
		return proficientName;
	}

	public void setProficientName(String proficientName) {
		this.proficientName = proficientName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getElectricNetName() {
		return electricNetName;
	}

	public void setElectricNetName(String electricNetName) {
		this.electricNetName = electricNetName;
	}

	public String getElectricRunTypeName() {
		return electricRunTypeName;
	}

	public void setElectricRunTypeName(String electricRunTypeName) {
		this.electricRunTypeName = electricRunTypeName;
	}

	public String getFactoryUserName() {
		return factoryUserName;
	}

	public void setFactoryUserName(String factoryUserName) {
		this.factoryUserName = factoryUserName;
	}

	public String getSupportSortName() {
		return supportSortName;
	}

	public void setSupportSortName(String supportSortName) {
		this.supportSortName = supportSortName;
	}

	public String getSuperName() {
		return superName;
	}

	public void setSuperName(String superName) {
		this.superName = superName;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getCompeten_advice() {
		return competen_advice;
	}

	public void setCompeten_advice(String competen_advice) {
		this.competen_advice = competen_advice;
	}

	public String getCompeten_grade() {
		return competen_grade;
	}

	public void setCompeten_grade(String competen_grade) {
		this.competen_grade = competen_grade;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
