package com.xj.dms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlanBean implements Parcelable{
	private String content;
	private String createDate;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@SuppressWarnings("unchecked")
	public static final Parcelable.Creator<NoticePlanBean> CREATOR = new Creator(){  

		@Override  
		public PlanBean createFromParcel(Parcel source) {  
			// TODO Auto-generated method stub  
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错  
			PlanBean p = new PlanBean();  
			p.setContent(source.readString());  
			p.setCreateDate(source.readString());  
			return p;  
		}  

		@Override  
		public NoticePlanBean[] newArray(int size) {  
			// TODO Auto-generated method stub  
			return new NoticePlanBean[size];  
		}  
	};  

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeString(content);
		dest.writeString(createDate);
	}
}
