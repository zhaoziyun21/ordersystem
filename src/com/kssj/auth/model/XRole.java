package com.kssj.auth.model;

import java.util.Date;

/**
 * XRole entity. @author MyEclipse Persistence Tools
 */

public class XRole implements java.io.Serializable {

	// Fields

	private String id;
	private String roleName;
	private String roleDesc;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	
	// Constructors

	/** default constructor */
	public XRole() {
	}

	/** full constructor */
 
	// Property accessors

	public String getId() {
		return this.id;
	}

 
	public XRole(String id, String roleName, String roleDesc, Date insTime,
			String insUser, Date updTime, String updUser) {
		super();
		this.id = id;
		this.roleName = roleName;
		this.roleDesc = roleDesc;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return this.roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
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