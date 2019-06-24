package com.kssj.auth.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * XStaffSum entity. @author MyEclipse Persistence Tools
 */

public class XStaffSum implements java.io.Serializable {

	// Fields

	private String id;
	private String userId;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	private String balance;

	// Constructors

	/** default constructor */
	public XStaffSum() {
	}

	public XStaffSum(String id, String userId, Date insTime, String insUser,
			Date updTime, String updUser, String balance) {
		super();
		this.id = id;
		this.userId = userId;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
		this.balance = balance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getInsTime() {
		return insTime;
	}

	public void setInsTime(Date insTime) {
		this.insTime = insTime;
	}

	public String getInsUser() {
		return insUser;
	}

	public void setInsUser(String insUser) {
		this.insUser = insUser;
	}

	public Date getUpdTime() {
		return updTime;
	}

	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

	public String getUpdUser() {
		return updUser;
	}

	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	

}