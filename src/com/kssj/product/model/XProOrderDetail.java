package com.kssj.product.model;

import java.sql.Timestamp;
import java.util.Date;

public class XProOrderDetail implements java.io.Serializable {

	private String id;
	private String proId; //产品id
	private Integer proNum; //订购产品数量
	private Integer payPrice; //订购产品价格
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	private String orderId;

	public XProOrderDetail() {
		super();
	}

	public XProOrderDetail(String proId, Integer proNum, Timestamp insTime,
			String insUser, Timestamp updTime, String updUser) {
		super();
		this.proId = proId;
		this.proNum = proNum;
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

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public Integer getProNum() {
		return proNum;
	}

	public void setProNum(Integer proNum) {
		this.proNum = proNum;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Integer payPrice) {
		this.payPrice = payPrice;
	}

}