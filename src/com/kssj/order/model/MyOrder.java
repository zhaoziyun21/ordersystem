package com.kssj.order.model;

import java.util.List;
import java.util.Map;

import com.kssj.auth.model.XUser;
import com.kssj.product.model.XReceiverInfo;

public class MyOrder {
	
	private String orderObj; 	//订餐对象
	private String orderId; 	//订单id
	private String orderTime;	//下单时间
	private String foodTypeNum;	//菜品份数
	private String moenyTotal; 	//总计
	private String balance;		//账户余额
	private String foodNameStr; //菜名
	private String flag; //给谁订餐
	private String forWhoId;
	
	private String  orderName;
	private List<Map> detailList; //详细
	private String orderType;
	
	private String isShow;
	
	private XFoodSendAddress xFoodSendAddress; //收餐地址
	
	private XUser xUser; //餐饮公司对象
	
	private String orderCategory; //订餐种类：正常订餐（ZC）预约订餐（YY）现场订餐（XC）
	
	public String getOrderCategory() {
		return orderCategory;
	}
	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	private String id;
	
	private String ordeNum;
	
	
	
	public String getForWhoId() {
		return forWhoId;
	}
	public void setForWhoId(String forWhoId) {
		this.forWhoId = forWhoId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getOrdeNum() {
		return ordeNum;
	}
	public void setOrdeNum(String ordeNum) {
		this.ordeNum = ordeNum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderObj() {
		return orderObj;
	}
	public void setOrderObj(String orderObj) {
		this.orderObj = orderObj;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getFoodTypeNum() {
		return foodTypeNum;
	}
	public void setFoodTypeNum(String foodTypeNum) {
		this.foodTypeNum = foodTypeNum;
	}
	public String getMoenyTotal() {
		return moenyTotal;
	}
	public void setMoenyTotal(String moenyTotal) {
		this.moenyTotal = moenyTotal;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getFoodNameStr() {
		return foodNameStr;
	}
	public void setFoodNameStr(String foodNameStr) {
		this.foodNameStr = foodNameStr;
	}
	public List<Map> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<Map> detailList) {
		this.detailList = detailList;
	}
	public XFoodSendAddress getxFoodSendAddress() {
		return xFoodSendAddress;
	}
	public void setxFoodSendAddress(XFoodSendAddress xFoodSendAddress) {
		this.xFoodSendAddress = xFoodSendAddress;
	}
	public XUser getxUser() {
		return xUser;
	}
	public void setxUser(XUser xUser) {
		this.xUser = xUser;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
