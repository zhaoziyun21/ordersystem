package com.kssj.order.model;

import java.util.Date;

/**
 * XAppointRelation entity. @author MyEclipse Persistence Tools
 */

public class XAppointRelation implements java.io.Serializable {

	// Fields

	private String id;
	private String leadId;
	private String appointId;
	private String leadName;
	private String appointName;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;

	// Constructors

	/** default constructor */
	public XAppointRelation() {
	}

	public XAppointRelation(String id, String leadId, String appointId,
			String leadName, String appointName, Date insTime, String insUser,
			Date updTime, String updUser) {
		super();
		this.id = id;
		this.leadId = leadId;
		this.appointId = appointId;
		this.leadName = leadName;
		this.appointName = appointName;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLeadId() {
		return leadId;
	}

	public void setLeadId(String leadId) {
		this.leadId = leadId;
	}

	public String getAppointId() {
		return appointId;
	}

	public void setAppointId(String appointId) {
		this.appointId = appointId;
	}

	public String getLeadName() {
		return leadName;
	}

	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}

	public String getAppointName() {
		return appointName;
	}

	public void setAppointName(String appointName) {
		this.appointName = appointName;
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

	
	
}