package com.xj_pipe.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageBean implements Parcelable{
	private int id;//消息id
	private int creater;//发送人id
	private String createrName;//发送人姓名
	private String createtime;//发送日期
	private int receiver;//接收人id
	private String content;//消息内容
	private int state;//状态（1：已读，0未读）
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCreater() {
		return creater;
	}
	public void setCreater(int creater) {
		this.creater = creater;
	}
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public int getReceiver() {
		return receiver;
	}
	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	@SuppressWarnings("unchecked")
	public static final Parcelable.Creator<MessageBean> CREATOR = new Creator(){  

		@Override  
		public MessageBean createFromParcel(Parcel source) {  
			// TODO Auto-generated method stub  
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错  
			MessageBean p = new MessageBean();  
			p.setId(source.readInt());  
			p.setCreater(source.readInt());  
			p.setCreaterName(source.readString());  
			p.setCreatetime(source.readString());
			p.setReceiver(source.readInt());
			p.setContent(source.readString());
			p.setState(source.readInt());
			return p;  
		}  

		@Override  
		public MessageBean[] newArray(int size) {  
			// TODO Auto-generated method stub  
			return new MessageBean[size];  
		}  
	};  
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeInt(id);
		dest.writeInt(creater);
		dest.writeString(createrName);
		dest.writeString(createtime);
		dest.writeInt(receiver);
		dest.writeString(content);
		dest.writeInt(state);
	}
}
