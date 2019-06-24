package com.kssj.order.model;

import java.util.Date;

/**
 * XFoodBill entity. @author MyEclipse Persistence Tools
 */

public class XFoodBill implements java.io.Serializable {

	// Fields

	private String id;
	private String dictionaryId;
	private String foodId;
	private Integer foodNum;
	private String foodName;
	private String foodRemain;
	
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;

	// Constructors

	/** default constructor */
	public XFoodBill() {
	}

	public XFoodBill(String id, String dictionaryId, String foodId,
			Integer foodNum, String foodName, String foodRemain, Date insTime,
			String insUser, Date updTime, String updUser) {
		super();
		this.id = id;
		this.dictionaryId = dictionaryId;
		this.foodId = foodId;
		this.foodNum = foodNum;
		this.foodName = foodName;
		this.foodRemain = foodRemain;
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

	public String getDictionaryId() {
		return dictionaryId;
	}

	public void setDictionaryId(String dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	public String getFoodId() {
		return foodId;
	}

	public void setFoodId(String foodId) {
		this.foodId = foodId;
	}

	public Integer getFoodNum() {
		return foodNum;
	}

	public void setFoodNum(Integer foodNum) {
		this.foodNum = foodNum;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getFoodRemain() {
		return foodRemain;
	}

	public void setFoodRemain(String foodRemain) {
		this.foodRemain = foodRemain;
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