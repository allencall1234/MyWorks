package com.xj_pipe.bean;

/**
 * 巡检要素
 * @author Administrator
 *
 */
public class InspectionFactor {
	/**id*/
	private String baseId;
	/**名称*/
	private String baseName;
	/**父id*/
	private String parentBaseId;
	/**排序方式,如id*/
	private String sort;
	/**子标题和父标题判断依据*/
	private String typeCode;
	/**同baseName*/
	private String typeDesc;
	
	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public String getParentBaseId() {
		return parentBaseId;
	}

	public void setParentBaseId(String parentBaseId) {
		this.parentBaseId = parentBaseId;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	
	
	
	
}
