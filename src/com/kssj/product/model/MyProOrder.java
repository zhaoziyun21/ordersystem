package com.kssj.product.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyProOrder {

	private String orderObj; // 订购产品对象
	private String orderTime; // 下单时间
	private String expressNum;	//快递单号
	private String proTypeNum; // 产品件数
	private String moneyTotal; // 总计
	private String balance; // 账户余额
	private String proNameStr; // 产品名
	private String flag; // 给谁订购产品
	private String forWhoId;
	private String sendOutFlag; //发货状态
	private long sendOutTime; //发货时间
	private Integer recFlag; //收货状态  0：手动确认收货 1：未收货 2：系统默认收货
	

	private String orderName;
	private List<Map> detailList; // 详细
	private String orderType;
	private XReceiverInfo xReceiverInfo; //收货人

	private String isShow;
	private String id;

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

	public String getProTypeNum() {
		return proTypeNum;
	}

	public void setProTypeNum(String proTypeNum) {
		this.proTypeNum = proTypeNum;
	}

	public String getMoneyTotal() {
		return moneyTotal;
	}

	public void setMoneyTotal(String moneyTotal) {
		this.moneyTotal = moneyTotal;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getProNameStr() {
		return proNameStr;
	}

	public void setProNameStr(String proNameStr) {
		this.proNameStr = proNameStr;
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

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public List<Map> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<Map> detailList) {
		this.detailList = detailList;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendOutFlag() {
		return sendOutFlag;
	}

	public void setSendOutFlag(String sendOutFlag) {
		this.sendOutFlag = sendOutFlag;
	}

	public XReceiverInfo getxReceiverInfo() {
		return xReceiverInfo;
	}

	public void setxReceiverInfo(XReceiverInfo xReceiverInfo) {
		this.xReceiverInfo = xReceiverInfo;
	}

	public long getSendOutTime() {
		return sendOutTime;
	}

	public void setSendOutTime(long sendOutTime) {
		this.sendOutTime = sendOutTime;
	}

	public String getExpressNum() {
		return expressNum;
	}

	public void setExpressNum(String expressNum) {
		this.expressNum = expressNum;
	}

	public Integer getRecFlag() {
		return recFlag;
	}

	public void setRecFlag(Integer recFlag) {
		this.recFlag = recFlag;
	}


}
