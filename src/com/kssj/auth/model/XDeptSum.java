package com.kssj.auth.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * XDeptSum entity. @author MyEclipse Persistence Tools
 */

public class XDeptSum implements java.io.Serializable {

	// Fields

	private String id;
	private Integer deptSum;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	private String deptId;

	// Constructors

	/** default constructor */
	public XDeptSum() {
	}

	public XDeptSum(String id, Integer deptSum, Date insTime, String insUser,
			Date updTime, String updUser, String deptId) {
		super();
		this.id = id;
		this.deptSum = deptSum;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
		this.deptId = deptId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getDeptSum() {
		return deptSum;
	}

	public void setDeptSum(Integer deptSum) {
		this.deptSum = deptSum;
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

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	
}