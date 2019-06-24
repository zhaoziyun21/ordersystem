package com.kssj.auth.model;

import java.util.Date;

/**
 * XUserRole entity. @author MyEclipse Persistence Tools
 */

public class XUserRole implements java.io.Serializable {

	// Fields

	private String id;
	private String userId;
	private String roleId;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	// Constructors

	/** default constructor */
	public XUserRole() {
	}

	/** full constructor */
	

	// Property accessors

	public String getId() {
		return this.id;
	}

	 

	public XUserRole(String id, String userId, String roleId, Date insTime,
			String insUser, Date updTime, String updUser) {
		super();
		this.id = id;
		this.userId = userId;
		this.roleId = roleId;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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

	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

	public String getUpdUser() {
		return this.updUser;
	}

	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}


}