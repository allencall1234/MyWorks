package com.xj.cnooc.model;

public class UserBean 
{
	private int id;// 唯一标识id
	private int accountid;// 账户id
	private String name;// 真实姓名
	private String beforedate;// 上次登录时间
	private String loginName;// 账号名称
	private String photo;// 照片地址
	private String rolename;// 角色名
	private String vendername;//厂家名称
	private String type;// 角色类型
	private int unit;// 厂家id
	
	public UserBean() {
		super();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBeforedate() {
		return beforedate;
	}
	public void setBeforedate(String beforedate) {
		this.beforedate = beforedate;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getVendername() {
		return vendername;
	}

	public void setVendername(String vendername) {
		this.vendername = vendername;
	}

	public int getAccountid() {
		return accountid;
	}

	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}
	
}
