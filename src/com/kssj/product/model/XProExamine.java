package com.kssj.product.model;

import java.util.Date;

public class XProExamine {

	private String id;
	private String proId; //产品id
	private String exStatus; //审批状态
	private String exDesc; //审批结果描述
	private Date exInsTime; //审批插入时间
	private String exInsUser; //执行插入审批操作的用户
	private Date exUpdTime; //审批更新时间
	private String exUpdUser; //执行更新审批操作的用户
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	public String getExStatus() {
		return exStatus;
	}
	public void setExStatus(String exStatus) {
		this.exStatus = exStatus;
	}
	public String getExDesc() {
		return exDesc;
	}
	public void setExDesc(String exDesc) {
		this.exDesc = exDesc;
	}
	
	public Date getExInsTime() {
		return exInsTime;
	}
	public void setExInsTime(Date exInsTime) {
		this.exInsTime = exInsTime;
	}
	public Date getExUpdTime() {
		return exUpdTime;
	}
	public void setExUpdTime(Date exUpdTime) {
		this.exUpdTime = exUpdTime;
	}
	public String getExInsUser() {
		return exInsUser;
	}
	public void setExInsUser(String exInsUser) {
		this.exInsUser = exInsUser;
	}
	public String getExUpdUser() {
		return exUpdUser;
	}
	public void setExUpdUser(String exUpdUser) {
		this.exUpdUser = exUpdUser;
	}
	
	
}
