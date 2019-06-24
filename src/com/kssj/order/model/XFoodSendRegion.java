package com.kssj.order.model;

import java.util.Date;

public class XFoodSendRegion implements java.io.Serializable  {

	private String id;
	private String regionName; //派送范围名称
	private String regionDesc; //描述
	private String delFlag; //0：启用，1：禁用
	private String insUser; //插入者
	private Date insTime; //插入时间
	private String updUser; //更新者
	private Date updTime; //更新时间

	public XFoodSendRegion() {
		super();
	}

	public XFoodSendRegion(String id, String regionName, String regionDesc,
			String delFlag, String insUser, Date insTime, String updUser,
			Date updTime) {
		super();
		this.id = id;
		this.regionName = regionName;
		this.regionDesc = regionDesc;
		this.delFlag = delFlag;
		this.insUser = insUser;
		this.insTime = insTime;
		this.updUser = updUser;
		this.updTime = updTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getRegionDesc() {
		return regionDesc;
	}

	public void setRegionDesc(String regionDesc) {
		this.regionDesc = regionDesc;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getInsUser() {
		return insUser;
	}

	public void setInsUser(String insUser) {
		this.insUser = insUser;
	}

	public Date getInsTime() {
		return insTime;
	}

	public void setInsTime(Date insTime) {
		this.insTime = insTime;
	}

	public String getUpdUser() {
		return updUser;
	}

	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}

	public Date getUpdTime() {
		return updTime;
	}

	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

}
