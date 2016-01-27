package com.xj_pipe.bean;

public class PhotoBean 
{
	private String imgUrl;
	private String imgName;
	
	public PhotoBean(String imgUrl, String imgName) 
	{
		super();
		this.imgUrl = imgUrl;
		this.imgName = imgName;
	}
	
	public PhotoBean() 
	{
		
	}

	public String getImgUrl()
	{
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl)
	{
		this.imgUrl = imgUrl;
	}

	public String getImgName() 
	{
		return imgName;
	}

	public void setImgName(String imgName)
	{
		this.imgName = imgName;
	}
	

}
