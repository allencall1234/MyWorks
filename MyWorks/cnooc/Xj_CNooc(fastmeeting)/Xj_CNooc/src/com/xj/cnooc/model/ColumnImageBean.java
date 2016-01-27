package com.xj.cnooc.model;

public class ColumnImageBean {
	private String textStr;
	private int imageUrl;
	private int index;

	public ColumnImageBean(String textStr, int imageUrl) {
		super();
		this.textStr = textStr;
		this.imageUrl = imageUrl;
	}

	public String getTextStr() {
		return textStr;
	}

	public void setTextStr(String textStr) {
		this.textStr = textStr;
	}

	public int getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(int imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
