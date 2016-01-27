package com.xj.cnooc.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AttachmentBean implements Serializable
{
	private String id;// 附件编号
	private String name;// 附件名称
	private String parentId;// 附件所属实体
	private String path;// 附件存放路径
	private String type; 
	
	public AttachmentBean()
	{
		
	}

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
