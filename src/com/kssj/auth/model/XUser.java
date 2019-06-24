package com.kssj.auth.model;

import java.util.Date;
import java.util.List;

/**
 * XUser entity. @author MyEclipse Persistence Tools
 */

public class XUser implements java.io.Serializable {

	// Fields

	private String id;
	private String userName;
	private String password;
	private String realName;
	private String deptId;
	private String tel;
	private String type;
	private Date insTime;
	private String insUser;
	private Date updTime;
	private String updUser;
	private String delFlag;
	private String email;
	private String foodCompanyName;
	private String jobtitle;
	private String userId;
	private String companyId;
	private String seclevel;
	private String managerid;
	private String managerstr;
	private String status; 
	private String otherCompanyId;
	//派送范围
	private String sendRegion;
	//员工的默认派送地址
	private String sendDefaultAddress;
	//餐饮公司公告生效时间
	private String noticeEffectiveTime;
	//餐饮公司公告描述
	private String noticeDesc;
	//针对餐饮公司  0：支持派送  1：不支持派送
	private String whetherSendStatus;
	
	private List<XMenu> xmenu;
	// Constructors

	/** default constructor */
	public XUser() {
	}

	/** full constructor */
 
	// Property accessors

	public String getId() {
		return this.id;
	}

	 
	public XUser(String id, String userName, String password, String realName,
			String deptId, String tel, String type, Date insTime,
			String insUser, Date updTime, String updUser, String delFlag,
			String email, String foodCompanyName, String jobtitle,
			String userId, String companyId, String seclevel, String managerid,
			String managerstr, String status, List<XMenu> xmenu) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.realName = realName;
		this.deptId = deptId;
		this.tel = tel;
		this.type = type;
		this.insTime = insTime;
		this.insUser = insUser;
		this.updTime = updTime;
		this.updUser = updUser;
		this.delFlag = delFlag;
		this.email = email;
		this.foodCompanyName = foodCompanyName;
		this.jobtitle = jobtitle;
		this.userId = userId;
		this.companyId = companyId;
		this.seclevel = seclevel;
		this.managerid = managerid;
		this.managerstr = managerstr;
		this.status = status;
		this.xmenu = xmenu;
	}

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getSeclevel() {
		return seclevel;
	}

	public void setSeclevel(String seclevel) {
		this.seclevel = seclevel;
	}

	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

	public String getManagerstr() {
		return managerstr;
	}

	public void setManagerstr(String managerstr) {
		this.managerstr = managerstr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getUpdUser() {
		return this.updUser;
	}

	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}

	public List<XMenu> getXmenu() {
		return xmenu;
	}

	public void setXmenu(List<XMenu> xmenu) {
		this.xmenu = xmenu;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getFoodCompanyName() {
		return foodCompanyName;
	}

	public void setFoodCompanyName(String foodCompanyName) {
		this.foodCompanyName = foodCompanyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtherCompanyId() {
		return otherCompanyId;
	}

	public void setOtherCompanyId(String otherCompanyId) {
		this.otherCompanyId = otherCompanyId;
	}

	public String getSendRegion() {
		return sendRegion;
	}

	public void setSendRegion(String sendRegion) {
		this.sendRegion = sendRegion;
	}

	public String getSendDefaultAddress() {
		return sendDefaultAddress;
	}

	public void setSendDefaultAddress(String sendDefaultAddress) {
		this.sendDefaultAddress = sendDefaultAddress;
	}

	public String getNoticeEffectiveTime() {
		return noticeEffectiveTime;
	}

	public void setNoticeEffectiveTime(String noticeEffectiveTime) {
		this.noticeEffectiveTime = noticeEffectiveTime;
	}

	public String getNoticeDesc() {
		return noticeDesc;
	}

	public void setNoticeDesc(String noticeDesc) {
		this.noticeDesc = noticeDesc;
	}

	public String getWhetherSendStatus() {
		return whetherSendStatus;
	}

	public void setWhetherSendStatus(String whetherSendStatus) {
		this.whetherSendStatus = whetherSendStatus;
	}

}