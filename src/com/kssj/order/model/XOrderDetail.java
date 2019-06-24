package com.kssj.order.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * XOrderDetail entity. @author MyEclipse Persistence Tools
 */

public class XOrderDetail implements java.io.Serializable {

	// Fields

	private String id;
	private String foodId;
	private Integer foodNum;
	private Integer payPrice;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	private String orderId;

	// Constructors

	/** default constructor */
	public XOrderDetail() {
	}

	/** full constructor */
	public XOrderDetail(String foodId, Integer foodNum, Timestamp insTime,
			String insUser, Timestamp updTime, String updUser) {
		this.foodId = foodId;
		this.foodNum = foodNum;
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

	public String getFoodId() {
		return this.foodId;
	}

	public void setFoodId(String foodId) {
		this.foodId = foodId;
	}

	public Integer getFoodNum() {
		return this.foodNum;
	}

	public void setFoodNum(Integer foodNum) {
		this.foodNum = foodNum;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUpdUser() {
		return this.updUser;
	}

	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}

	public Integer getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Integer payPrice) {
		this.payPrice = payPrice;
	}

}