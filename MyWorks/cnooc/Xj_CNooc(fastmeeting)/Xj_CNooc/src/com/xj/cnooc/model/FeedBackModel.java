package com.xj.cnooc.model;

import java.io.Serializable;

/**
 * 现场反馈实体
 * 
 * @author qinfan
 *
 *         2015-9-22
 */
public class FeedBackModel implements Serializable {
	private String content;
	private String createTime;
	private String createName;

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

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}
}
