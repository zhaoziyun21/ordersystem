package com.kssj.order.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * XFood entity. @author MyEclipse Persistence Tools
 */

public class XFood implements java.io.Serializable {

	// Fields

	private String id;
	private String foodName;
	private String foodDesc;
	private Integer costPrice;
	private Integer sellPrice;
	private String foodImg;
	private String userId;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	private String foodType;
	private int delFlag;
	// Constructors

	public String getFoodType() {
		return foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

	/** default constructor */
	public XFood() {
	}

	/** full constructor */
	 

	// Property accessors

	public String getId() {
		return this.id;
	}

	public XFood(String id, String foodName, String foodDesc,
			Integer costPrice, Integer sellPrice, String foodImg,
			String userId, Date insTime, String insUser, Date updTime,
			String updUser, String foodType, int delFlag) {
		super();
		this.id = id;
		this.foodName = foodName;
		this.foodDesc = foodDesc;
		this.costPrice = costPrice;
		this.sellPrice = sellPrice;
		this.foodImg = foodImg;
		this.userId = userId;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
		this.foodType = foodType;
		this.delFlag = delFlag;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFoodName() {
		return this.foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getFoodDesc() {
		return this.foodDesc;
	}

	public void setFoodDesc(String foodDesc) {
		this.foodDesc = foodDesc;
	}

	public Integer getCostPrice() {
		return this.costPrice;
	}

	public void setCostPrice(Integer costPrice) {
		this.costPrice = costPrice;
	}

	public Integer getSellPrice() {
		return this.sellPrice;
	}

	public void setSellPrice(Integer sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getFoodImg() {
		return this.foodImg;
	}

	public void setFoodImg(String foodImg) {
		this.foodImg = foodImg;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public void setUpdTime(Timestamp updTime) {
		this.updTime = updTime;
	}

	public String getUpdUser() {
		return this.updUser;
	}

	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	 

}