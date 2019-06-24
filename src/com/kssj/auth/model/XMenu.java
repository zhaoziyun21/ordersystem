package com.kssj.auth.model;

import java.util.Date;

/**
 * XMenu entity. @author MyEclipse Persistence Tools
 */

public class XMenu implements java.io.Serializable {

	// Fields

	private String id;
	private String menuName;
	private String menuUrl;
	private String parentId;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;

	// Constructors

	/** default constructor */
	public XMenu() {
	}

	/** full constructor */
	public XMenu(String menuName, String menuUrl, String parentId,
			Date insTime, String insUser, Date updTime, String updUser) {
		this.menuName = menuName;
		this.menuUrl = menuUrl;
		this.parentId = parentId;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return this.menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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