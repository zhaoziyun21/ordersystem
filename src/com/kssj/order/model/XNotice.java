package com.kssj.order.model;

import java.util.Date;

public class XNotice implements java.io.Serializable {

	private String id;
	private String noticeName; //公告名称
	private String noticeDesc; //公告描述
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	private int delFlag; //0：代表“未删除” 1：代表“已删除”
	
	public XNotice() {
		super();
	}
	
	public XNotice(String id, String noticeName, String noticeDesc,
			Date insTime, String insUser, Date updTime, String updUser,
			int delFlag) {
		super();
		this.id = id;
		this.noticeName = noticeName;
		this.noticeDesc = noticeDesc;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
		this.delFlag = delFlag;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNoticeName() {
		return noticeName;
	}
	public void setNoticeName(String noticeName) {
		this.noticeName = noticeName;
	}
	public String getNoticeDesc() {
		return noticeDesc;
	}
	public void setNoticeDesc(String noticeDesc) {
		this.noticeDesc = noticeDesc;
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
	public int getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	
}