package com.kssj.product.model;

public class XReceiverInfo {

	private String id;
	private String userId; //登录用户id
	private String recName; //收货人姓名
	private String recPhone; //收货人手机号
	private String recArea; //所在地区
	private String recDetailAddress; //详细地址
	//收货地址
	private String recAddress; 
	private int recDefaultStatus; //0：默认地址  1：非默认地址
	private int recDelFlag; //0：删除  1：未删除

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

	public String getRecName() {
		return recName;
	}

	public void setRecName(String recName) {
		this.recName = recName;
	}

	public String getRecPhone() {
		return recPhone;
	}

	public void setRecPhone(String recPhone) {
		this.recPhone = recPhone;
	}

	public String getRecArea() {
		return recArea;
	}

	public void setRecArea(String recArea) {
		this.recArea = recArea;
	}

	public String getRecDetailAddress() {
		return recDetailAddress;
	}

	public void setRecDetailAddress(String recDetailAddress) {
		this.recDetailAddress = recDetailAddress;
	}

	public int getRecDefaultStatus() {
		return recDefaultStatus;
	}

	public void setRecDefaultStatus(int recDefaultStatus) {
		this.recDefaultStatus = recDefaultStatus;
	}

	public int getRecDelFlag() {
		return recDelFlag;
	}

	public void setRecDelFlag(int recDelFlag) {
		this.recDelFlag = recDelFlag;
	}

	public String getRecAddress() {
		return recAddress;
	}

	public void setRecAddress(String recAddress) {
		this.recAddress = recAddress;
	}

}
