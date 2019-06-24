package com.kssj.order.model;

import java.util.List;

public class MyAccount {
	
	private String foodName; //套餐名称
	private int foodNum; // 套餐数
	private int total; //金额
	private String deptName; //部门名称
	private int sumNum;//总份数
	private int deptTotal;//部门金额
	private int sumTotal; //总金额
	
	private String companyName; //公司名称
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public int getFoodNum() {
		return foodNum;
	}
	public void setFoodNum(int foodNum) {
		this.foodNum = foodNum;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public int getSumNum() {
		return sumNum;
	}
	public void setSumNum(int sumNum) {
		this.sumNum = sumNum;
	}
	public int getDeptTotal() {
		return deptTotal;
	}
	public void setDeptTotal(int deptTotal) {
		this.deptTotal = deptTotal;
	}
	public int getSumTotal() {
		return sumTotal;
	}
	public void setSumTotal(int sumTotal) {
		this.sumTotal = sumTotal;
	}
	
	
	
	
}
