package com.xj.cnooc.model;

import java.io.Serializable;

public class PingYiModel implements Serializable {
	private String createrName;
	private String content;
	private String createTime;
	private int isAdopted;

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getIsAdopted() {
		return isAdopted;
	}

	public void setIsAdopted(int isAdopted) {
		this.isAdopted = isAdopted;
	}

}
