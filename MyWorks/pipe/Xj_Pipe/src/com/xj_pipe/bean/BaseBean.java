package com.xj_pipe.bean;

public class BaseBean 
{
	private String result;// 网络请求返回结果
	private int state;// 网络请求返回状态
	
	public BaseBean(String result, int state) 
	{
		super();
		this.result = result;
		this.state = state;
	}
	
	public BaseBean() 
	{
		super();
	}

	public String getResult() 
	{
		return result;
	}
	
	public void setResult(String result)
	{
		this.result = result;
	}
	
	public int getState() 
	{
		return state;
	}
	
	public void setState(int state) 
	{
		this.state = state;
	}

}
