package com.xj.dms.model;

import java.io.Serializable;

public class KnowledgeBaseBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int id;// id
	private String title;// 知识库标题
	private String context;// 内容
	private String type;// 所属装置
	private String keyword;// 关键词
	private String filename;// 文件名
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContext() {
		return context;
	}
	
	public void setContext(String context) {
		this.context = context;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
	
	

}
