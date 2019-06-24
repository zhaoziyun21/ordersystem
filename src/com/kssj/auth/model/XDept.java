package com.kssj.auth.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * XDept entity. @author MyEclipse Persistence Tools
 */

public class XDept implements java.io.Serializable {

	// Fields

	private String id;
	private String deptId;
	private String companyId;
	private String deptName;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	private String supdepid;
	private String roomNum;
	// Constructors

	/** default constructor */
	public XDept() {
	}

	 

	public XDept(String id, String deptId, String companyId, String deptName,
			Date insTime, String insUser, Date updTime, String updUser,
			String supdepid, String roomNum) {
		super();
		this.id = id;
		this.deptId = deptId;
		this.companyId = companyId;
		this.deptName = deptName;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
		this.supdepid = supdepid;
		this.roomNum = roomNum;
	}



	public String getRoomNum() {
		return roomNum;
	}



	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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

	public String getSupdepid() {
		return supdepid;
	}

	public void setSupdepid(String supdepid) {
		this.supdepid = supdepid;
	}

	

}