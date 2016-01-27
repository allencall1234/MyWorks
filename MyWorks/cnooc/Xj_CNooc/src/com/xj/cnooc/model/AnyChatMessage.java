package com.xj.cnooc.model;

public class AnyChatMessage{
	
	private int userId;
	private int type;
	private String message;
	private String userName;
	private String photo;
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{type = " + type + ",UserId = " + userId + ",userName = " + userName + ",messsage = " + message + "}";
	}
}
