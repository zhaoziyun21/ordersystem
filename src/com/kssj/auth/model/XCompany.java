package com.kssj.auth.model;

import java.util.Date;

/**
 * XCompany entity. @author MyEclipse Persistence Tools
 */

public class XCompany implements java.io.Serializable {

	// Fields

	private String id;
	private String companyId;
	private String companyName;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	private String companyCode;
	private String companyDesc;
	private String parentId;
	// Constructors

	/** default constructor */
	public XCompany() {
	}

	/** full constructor */
	 
	// Property accessors

	public String getId() {
		return this.id;
	}

	 
	 

	public XCompany(String id, String companyId, String companyName,
			Date insTime, String insUser, Date updTime, String updUser,
			String companyCode, String companyDesc, String parentId) {
		super();
		this.id = id;
		this.companyId = companyId;
		this.companyName = companyName;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
		this.companyCode = companyCode;
		this.companyDesc = companyDesc;
		this.parentId = parentId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	 

}