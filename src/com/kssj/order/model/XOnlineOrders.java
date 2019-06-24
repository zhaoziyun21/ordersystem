package com.kssj.order.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * XOnlineOrders entity. @author MyEclipse Persistence Tools
 */

public class XOnlineOrders implements java.io.Serializable {

	// Fields

	private String id;
	private String userId;
	private String orderType;
	private String orderMoney;
	private String restaurantId;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	private String delFlag;
	
	private String  foodCompanyName;

	// Constructors

	/** default constructor */
	public XOnlineOrders() {
	}
	
	

	public String getFoodCompanyName() {
		return foodCompanyName;
	}



	public void setFoodCompanyName(String foodCompanyName) {
		this.foodCompanyName = foodCompanyName;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
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

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	

}