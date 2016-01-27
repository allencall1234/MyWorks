package com.xj.cnooc.model;

import java.io.Serializable;

public class PostBean implements Serializable
{
	private int answerCount;// 回复数量
	private String bbsPostName;// 发帖人姓名
	private String title;// 帖子标题
	private String content;// 帖子内容
	private String ctime;// 帖子创建时间
	private String photo;// 发帖人头像
	private int id;// 帖子编号
	private int iscurrent;// 用于判断是否可删除帖子，同一人可删除
	
	public PostBean() 
	{
		
	}

	public int getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

	public String getBbsPostName() {
		return bbsPostName;
	}

	public void setBbsPostName(String bbsPostName) {
		this.bbsPostName = bbsPostName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIscurrent() {
		return iscurrent;
	}

	public void setIscurrent(int iscurrent) {
		this.iscurrent = iscurrent;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
