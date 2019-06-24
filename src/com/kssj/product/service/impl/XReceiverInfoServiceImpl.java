package com.kssj.product.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.product.dao.XReceiverInfoDao;
import com.kssj.product.model.XReceiverInfo;
import com.kssj.product.service.XReceiverInfoService;

public class XReceiverInfoServiceImpl extends GenericServiceImpl<XReceiverInfo, String> implements XReceiverInfoService {

	private XReceiverInfoDao dao;
	
	public XReceiverInfoServiceImpl(XReceiverInfoDao dao) {
		super(dao);
		this.dao = dao;
	}

	/**
	 * 执行添加收货人信息操作
	 * @return 
	 */
	@Override
	public XReceiverInfo saveOrUpdateReceiverInfo(XReceiverInfo xReceiverInfo) {
		return dao.saveOrUpdate(xReceiverInfo);
	}

	/**
	 * 查询当前登录用户的收货人个数
	 */
	@Override
	public Long findReceiverNum(String userId) {
		String hql = "SELECT COUNT(*) FROM XReceiverInfo WHERE userId = '"+userId+"' and recDelFlag = 1";
		Long count = dao.findCountByHQL(hql, null);
		return count;
	}

	/**
	 * 查询当前登录用户的所有收货人信息
	 */
	@Override
	public List<XReceiverInfo> findAllReceiversInfo(String userId) {
		String hql = "FROM XReceiverInfo WHERE userId = '"+userId+"' and recDelFlag = 1";
		List<XReceiverInfo> xReceiverInfoList = dao.findListByHQL(hql, null);
		if(xReceiverInfoList != null && xReceiverInfoList.size() > 0){
			return xReceiverInfoList;
		}
		return null;
	}

	/**
	 * 通过收货人id查询此收货人地址信息
	 */
	@Override
	public XReceiverInfo findReceiverById(String id) {
		String hql = "FROM XReceiverInfo WHERE id = '"+id+"'";
		List<XReceiverInfo> xReceiverInfoList = dao.findListByHQL(hql, null);
		if(xReceiverInfoList != null && xReceiverInfoList.size() > 0){
			return xReceiverInfoList.get(0);
		}
		return null;
	}

	/**
	 * 查询默认地址
	 */
	@Override
	public XReceiverInfo findReceiverByRecDefaultStatus(int i) {
		String hql = "FROM XReceiverInfo WHERE recDefaultStatus = '"+i+"' and recDelFlag = 1";
		List<XReceiverInfo> xReceiverInfoList = dao.findListByHQL(hql, null);
		if(xReceiverInfoList != null && xReceiverInfoList.size() > 0){
			return xReceiverInfoList.get(0);
		}
		return null;
	}

	/**
	 * 设置默认地址
	 */
	@Override
	@Transactional
	public XReceiverInfo setUpRecDefaultStatus(String id) {
		//更改原先的默认地址
		XReceiverInfo xReceiverInfo = findReceiverByRecDefaultStatus(0);
		xReceiverInfo.setRecDefaultStatus(1);
		saveOrUpdateReceiverInfo(xReceiverInfo);
		
		//设置现在的默认地址
		XReceiverInfo xReceiverInfo2 = findReceiverById(id);
		xReceiverInfo2.setRecDefaultStatus(0);
		XReceiverInfo xReceiverInfo3 = saveOrUpdateReceiverInfo(xReceiverInfo);
		return xReceiverInfo3;
	}

	/**
	 * 删除收货人
	 */
	@Override
	@Transactional
	public XReceiverInfo deleteReceiver(String id) {
		XReceiverInfo xReceiverInfo = findReceiverById(id);
		xReceiverInfo.setRecDelFlag(0); //删除状态
		XReceiverInfo xReceiverInfo3 = saveOrUpdateReceiverInfo(xReceiverInfo);
		
		if(xReceiverInfo.getRecDefaultStatus() == 0){ //如果删除的是默认地址
			//重新设置默认地址
			XReceiverInfo xReceiverInfo2 = findReceiverByRecDefaultStatus(1);
			if(xReceiverInfo2 != null){
				xReceiverInfo2.setRecDefaultStatus(0);
				saveOrUpdateReceiverInfo(xReceiverInfo2);
			}
		}
		return xReceiverInfo3;
	}

}
