package com.xj_pipe.bean;

import java.io.Serializable;

public class LinkmanBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String linkman_name;
	private String linkman_position;
	private boolean linkman_flag;
	
	public boolean isLinkman_flag() {
		return linkman_flag;
	}
	public void setLinkman_flag(boolean linkman_flag) {
		this.linkman_flag = linkman_flag;
	}
	public String getLinkman_name() {
		return linkman_name;
	}
	public void setLinkman_name(String linkman_name) {
		this.linkman_name = linkman_name;
	}
	public String getLinkman_position() {
		return linkman_position;
	}
	public void setLinkman_position(String linkman_position) {
		this.linkman_position = linkman_position;
	}

}
