package com.kssj.order.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * XDetailRecord entity. @author MyEclipse Persistence Tools
 */

public class XDetailRecord implements java.io.Serializable {

	// Fields

	private String id;
	private String type;
	private String userId;
	private String deptId;
	private String balance;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	private String remark;
	private String changeMoney;

	// Constructors

	public String getChangeMoney() {
		return changeMoney;
	}

	public void setChangeMoney(String changeMoney) {
		this.changeMoney = changeMoney;
	}


	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

	/** default constructor */
	public XDetailRecord() {
	}

	/** full constructor */
	public XDetailRecord(String type, String userId, String deptId,
			String balance, Date insTime, String insUser,
			Date updTime, String updUser, String remark) {
		this.type = type;
		this.userId = userId;
		this.deptId = deptId;
		this.balance = balance;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
		this.remark = remark;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getBalance() {
		return this.balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public Date getInsTime() {
		return this.insTime;
	}

	public void setInsTime(Date insTime) {
		this.insTime = insTime;
	}

	public String getInsUser() {
		return this.insUser;
	}

	public void setInsUser(String insUser) {
		this.insUser = insUser;
	}

	public Date getUpdTime() {
		return this.updTime;
	}

	public void setUpdTime(Timestamp updTime) {
		this.updTime = updTime;
	}

	public String getUpdUser() {
		return this.updUser;
	}

	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}