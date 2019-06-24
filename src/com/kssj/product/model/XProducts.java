package com.kssj.product.model;

import java.util.Date;

public class XProducts implements java.io.Serializable {

	private String id;
	private String proCateId; //产品类别id
	private String proCode; //产品编号
	private String proName; //产品名称
	private int proNum; //产品数量
	private String proRemain; //产品数量
	private int proPrice; //产品价格
	private String proReferencePrice; //产品参考价格描述
	private String proDescribe; //产品描述
	private String proImageUrl; //产品图片url
	private String status; //产品上下架状态
	private String proOutUrl; //外链地址
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProCode() {
		return proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public int getProNum() {
		return proNum;
	}

	public void setProNum(int proNum) {
		this.proNum = proNum;
	}

	public int getProPrice() {
		return proPrice;
	}

	public void setProPrice(int proPrice) {
		this.proPrice = proPrice;
	}

	public String getProDescribe() {
		return proDescribe;
	}

	public void setProDescribe(String proDescribe) {
		this.proDescribe = proDescribe;
	}

	public String getProImageUrl() {
		return proImageUrl;
	}

	public void setProImageUrl(String proImageUrl) {
		this.proImageUrl = proImageUrl;
	}

	public String getProRemain() {
		return proRemain;
	}

	public void setProRemain(String proRemain) {
		this.proRemain = proRemain;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getProOutUrl() {
		return proOutUrl;
	}

	public void setProOutUrl(String proOutUrl) {
		this.proOutUrl = proOutUrl;
	}

	public String getProCateId() {
		return proCateId;
	}

	public void setProCateId(String proCateId) {
		this.proCateId = proCateId;
	}

	public String getProReferencePrice() {
		return proReferencePrice;
	}

	public void setProReferencePrice(String proReferencePrice) {
		this.proReferencePrice = proReferencePrice;
	}
	
}
