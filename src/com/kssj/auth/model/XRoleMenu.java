package com.kssj.auth.model;

import java.util.Date;

/**
 * XRoleMenu entity. @author MyEclipse Persistence Tools
 */

public class XRoleMenu implements java.io.Serializable {

	// Fields

	private String id;
	private String roleId;
	private String menuId;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	// Constructors

	/** default constructor */
	public XRoleMenu() {
	}

	/** full constructor */
	 

	// Property accessors

	public String getId() {
		return this.id;
	}

	 

	public XRoleMenu(String id, String roleId, String menuId, Date insTime,
			String insUser, Date updTime, String updUser) {
		super();
		this.id = id;
		this.roleId = roleId;
		this.menuId = menuId;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getMenuId() {
		return this.menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
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