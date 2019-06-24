package com.kssj.product.service.impl;

import java.util.List;

import com.kssj.frame.service.impl.GenericServiceImpl;
import com.kssj.product.dao.XProExamineDao;
import com.kssj.product.model.XProExamine;
import com.kssj.product.service.XProExamineService;

public class XProExamineServiceImpl extends GenericServiceImpl<XProExamine, String>
		implements XProExamineService {

	private XProExamineDao dao;

	public XProExamineServiceImpl(XProExamineDao dao) {
		super(dao);
		this.dao = dao;
	}

	@Override
	public XProExamine getExamineDetail(String id) {
		String hql ="from XProExamine where proId=?";
		Object[] o ={id};
		List<XProExamine> xProExamineList = this.dao.findByHql(hql, o);
		if(xProExamineList != null && xProExamineList.size() > 0){
			return xProExamineList.get(0);
		}
		return null;
	}

}
