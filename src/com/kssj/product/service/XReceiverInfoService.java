package com.kssj.product.service;

import java.util.List;

import com.kssj.frame.service.GenericService;
import com.kssj.product.model.XReceiverInfo;

public interface XReceiverInfoService extends GenericService<XReceiverInfo, String> {

	//执行添加收货人信息操作
	XReceiverInfo saveOrUpdateReceiverInfo(XReceiverInfo xReceiverInfo);

	//查询当前登录用户的收货人个数
	Long findReceiverNum(String userId);

	//查询当前登录用户的所有收货人信息
	List<XReceiverInfo> findAllReceiversInfo(String userId);

	//通过收货人id查询此收货人地址信息
	XReceiverInfo findReceiverById(String id);

	//查询默认地址
	XReceiverInfo findReceiverByRecDefaultStatus(int i);

	//设置默认地址
	XReceiverInfo setUpRecDefaultStatus(String id);

	//删除收货人
	XReceiverInfo deleteReceiver(String id);

}
