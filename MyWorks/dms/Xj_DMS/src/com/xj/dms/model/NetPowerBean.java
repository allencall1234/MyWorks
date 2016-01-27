package com.xj.dms.model;

import java.io.Serializable;

public class NetPowerBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String activePower;
	private String reactivePower;
	private String hotPower;

	public String getActivePower() {
		return activePower;
	}
	public void setActivePower(String activePower) {
		this.activePower = activePower;
	}
	public String getReactivePower() {
		return reactivePower;
	}
	public void setReactivePower(String reactivePower) {
		this.reactivePower = reactivePower;
	}
	public String getHotPower() {
		return hotPower;
	}
	public void setHotPower(String hotPower) {
		this.hotPower = hotPower;
	}
	
}
