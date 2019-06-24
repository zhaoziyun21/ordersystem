package com.kssj.order.model;

import java.util.List;

public class MyFood {
	
	private String foodName; //套餐名称
	private String foodDesc; //套餐描述
	private int foodNum; // 套餐数
	private String realName; //订餐人
	private String deptName; //部门
	private String tel; //联系电话
	private String deptRoleName; //负责人
	private String roomNum;
	private String companyName; // 公司名称
	private String effectiveDate; // 生效日期
	
	
	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	private List<MyFoodUser> listUser;

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getFoodDesc() {
		return foodDesc;
	}

	public void setFoodDesc(String foodDesc) {
		this.foodDesc = foodDesc;
	}

	public int getFoodNum() {
		return foodNum;
	}

	public void setFoodNum(int foodNum) {
		this.foodNum = foodNum;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDeptRoleName() {
		return deptRoleName;
	}

	public void setDeptRoleName(String deptRoleName) {
		this.deptRoleName = deptRoleName;
	}

	public List<MyFoodUser> getListUser() {
		return listUser;
	}

	public void setListUser(List<MyFoodUser> listUser) {
		this.listUser = listUser;
	}

	
	
}
