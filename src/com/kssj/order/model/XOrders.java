package com.kssj.order.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * XOrders entity. @author MyEclipse Persistence Tools
 */

public class XOrders implements java.io.Serializable {

	// Fields

	private String id;
	private String orderType;
	//订餐种类：正常订餐（ZC）预约订餐（YY）现场订餐（XC）
	private String orderCategory;
	private String userId;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	private String flag;
	private String forWhoId;
	private int delFlag;
	private String vistorName;
	private int sendOutFlag; //0：发货，1：未发货
	private Date sendOutTime; //发货时间
	private String recId; //收货人id
	private String expressNum; //快递单号
	private int recFlag; //收货状态  0：手动确认收货 1：未收货 2：系统默认收货
	// Constructors

	/** default constructor */
	public XOrders() {
	}

	public XOrders(String id, String orderType, String userId, Date insTime,
			String insUser, Date updTime, String updUser, String flag,
			String forWhoId, int delFlag, String vistorName) {
		super();
		this.id = id;
		this.orderType = orderType;
		this.userId = userId;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
		this.flag = flag;
		this.forWhoId = forWhoId;
		this.delFlag = delFlag;
		this.vistorName = vistorName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getForWhoId() {
		return forWhoId;
	}

	public void setForWhoId(String forWhoId) {
		this.forWhoId = forWhoId;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	public String getVistorName() {
		return vistorName;
	}

	public void setVistorName(String vistorName) {
		this.vistorName = vistorName;
	}

	public int getSendOutFlag() {
		return sendOutFlag;
	}

	public void setSendOutFlag(int sendOutFlag) {
		this.sendOutFlag = sendOutFlag;
	}

	public Date getSendOutTime() {
		return sendOutTime;
	}

	public void setSendOutTime(Date sendOutTime) {
		this.sendOutTime = sendOutTime;
	}

	public String getRecId() {
		return recId;
	}

	public void setRecId(String recId) {
		this.recId = recId;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getExpressNum() {
		return expressNum;
	}

	public void setExpressNum(String expressNum) {
		this.expressNum = expressNum;
	}

	public int getRecFlag() {
		return recFlag;
	}

	public void setRecFlag(int recFlag) {
		this.recFlag = recFlag;
	}

}