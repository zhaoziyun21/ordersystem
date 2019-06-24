package com.kssj.order.model;

import java.sql.Timestamp;

/**
 * XDictionary entity. @author MyEclipse Persistence Tools
 */

public class XDictionary implements java.io.Serializable {

	// Fields

	private String id;
	private String foodType;
	private String week;
	private Timestamp insTime;
	private String insUser;
	private Timestamp updTime;
	private String updUser;

	// Constructors

	/** default constructor */
	public XDictionary() {
	}

	/** full constructor */
	public XDictionary(String foodType, String week, Timestamp insTime,
			String insUser, Timestamp updTime, String updUser) {
		this.foodType = foodType;
		this.week = week;
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

	public String getFoodType() {
		return this.foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	public String getWeek() {
		return this.week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public Timestamp getInsTime() {
		return this.insTime;
	}

	public void setInsTime(Timestamp insTime) {
		this.insTime = insTime;
	}

	public String getInsUser() {
		return this.insUser;
	}

	public void setInsUser(String insUser) {
		this.insUser = insUser;
	}

	public Timestamp getUpdTime() {
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

}