package com.xj.cnooc.model;

public class ReplyPostBean
{
	private String reply_post_img;// 回帖人头像
	private int bbspost_id;// 帖子id
	private String replyName;// 回帖人姓名
	private String content;// 回复内容
	private String createformat;// 回帖时间
	private int reply_account;// 回帖数量
	
	public ReplyPostBean()
	{
		super();
	}
	
	public ReplyPostBean(String replyName, String content, String createformat)
	{
		super();
		this.replyName = replyName;
		this.content = content;
		this.createformat = createformat;
	}

	public String getReply_post_img() {
		return reply_post_img;
	}

	public void setReply_post_img(String reply_post_img) {
		this.reply_post_img = reply_post_img;
	}

	public int getBbspost_id() {
		return bbspost_id;
	}

	public void setBbspost_id(int bbspost_id) {
		this.bbspost_id = bbspost_id;
	}

	public String getReplyName() {
		return replyName;
	}

	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateformat() {
		return createformat;
	}

	public void setCreateformat(String createformat) {
		this.createformat = createformat;
	}

	public int getReply_account() {
		return reply_account;
	}

	public void setReply_account(int reply_account) {
		this.reply_account = reply_account;
	}
	
}
