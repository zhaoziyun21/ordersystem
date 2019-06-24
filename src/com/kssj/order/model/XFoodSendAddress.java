package com.kssj.order.model;

import java.util.Date;

public class XFoodSendAddress implements java.io.Serializable  {

	private String id;
	private String regionId; //所属派送范围
	//派送范围名称
	private String regionName; 
	private String park; //园区
	private String highBuilding; //楼栋
	private String unit; //单元（座）
	private String floor; //楼层
	private String roomNum; //房间号
	private String delFlag; //0：启用，1：禁用
	private String insUser; //插入者
	private Date insTime; //插入时间
	private String updUser; //更新者
	private Date updTime; //更新时间

	public XFoodSendAddress() {
		super();
	}

	public XFoodSendAddress(String id, String regionId, String park,
			String highBuilding, String unit, String floor, String roomNum,
			String delFlag, String insUser, Date insTime, String updUser,
			Date updTime) {
		super();
		this.id = id;
		this.regionId = regionId;
		this.park = park;
		this.highBuilding = highBuilding;
		this.unit = unit;
		this.floor = floor;
		this.roomNum = roomNum;
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

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getPark() {
		return park;
	}

	public void setPark(String park) {
		this.park = park;
	}

	public String getHighBuilding() {
		return highBuilding;
	}

	public void setHighBuilding(String highBuilding) {
		this.highBuilding = highBuilding;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
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

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

}
