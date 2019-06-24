package com.kssj.product.model;

import java.util.Date;

@SuppressWarnings("serial")
public class XProCategory implements java.io.Serializable {

	private String id;
	private String proCateName; // 产品名称
	private String proCateDesc; // 描述
	private String delFlag; // 0：启用，1：禁用
	private String insUser; // 插入者
	private Date insTime; // 插入时间
	private String updUser; // 更新者
	private Date updTime; // 更新时间

	public XProCategory() {
		super();
	}

	public XProCategory(String id, String proCateName, String proCateDesc,
			String delFlag, String insUser, Date insTime, String updUser,
			Date updTime) {
		super();
		this.id = id;
		this.proCateName = proCateName;
		this.proCateDesc = proCateDesc;
		this.delFlag = delFlag;
		this.insUser = insUser;
		this.insTime = insTime;
		this.updUser = updUser;
		this.updTime = updTime;
	}





	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProCateName() {
		return proCateName;
	}

	public void setProCateName(String proCateName) {
		this.proCateName = proCateName;
	}

	public String getProCateDesc() {
		return proCateDesc;
	}

	public void setProCateDesc(String proCateDesc) {
		this.proCateDesc = proCateDesc;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getInsUser() {
		return insUser;
	}

	public void setInsUser(String insUser) {
		this.insUser = insUser;
	}

	public Date getInsTime() {
		return insTime;
	}

	public void setInsTime(Date insTime) {
		this.insTime = insTime;
	}

	public String getUpdUser() {
		return updUser;
	}

	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}

	public Date getUpdTime() {
		return updTime;
	}

	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

}
