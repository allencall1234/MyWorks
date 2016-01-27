package com.xj.dms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NoticePlanBean implements Parcelable{
	private String dateTime;
	private String following;
	private String serviceName;

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getFollowing() {
		return following;
	}

	public void setFollowing(String following) {
		this.following = following;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@SuppressWarnings("unchecked")
	public static final Parcelable.Creator<NoticePlanBean> CREATOR = new Creator(){  

		@Override  
		public NoticePlanBean createFromParcel(Parcel source) {  
			// TODO Auto-generated method stub  
			// 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错  
			NoticePlanBean p = new NoticePlanBean();  
			p.setDateTime(source.readString());  
			p.setFollowing(source.readString());  
			p.setServiceName(source.readString());  
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
		dest.writeString(dateTime);
		dest.writeString(following);
		dest.writeString(serviceName);
	}
}
