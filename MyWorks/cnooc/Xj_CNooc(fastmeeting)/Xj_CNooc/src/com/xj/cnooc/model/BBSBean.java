package com.xj.cnooc.model;

import java.io.Serializable;

public class BBSBean implements Serializable {
	private static final long serialVersionUID = 13635773553L;

	private int count;// 每个版块的帖子的数量
	private String ctime;// 帖子创建时间
	private String description;// 版块描述
	private int id;// 版块id
	private String moderatorname;// 版主
	private String name;// 版块名称
	private int oneModuleTotal;// 未看数量

	public BBSBean() {
		super();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModeratorname() {
		return moderatorname;
	}

	public void setModeratorname(String moderatorname) {
		this.moderatorname = moderatorname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOneModuleTotal() {
		return oneModuleTotal;
	}

	public void setOneModuleTotal(int oneModuleTotal) {
		this.oneModuleTotal = oneModuleTotal;
	}

}
