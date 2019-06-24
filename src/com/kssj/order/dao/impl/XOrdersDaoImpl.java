package com.kssj.order.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.order.dao.XOrdersDao;
import com.kssj.order.model.XOrders;

@SuppressWarnings("unchecked")
public class XOrdersDaoImpl extends GenericDaoImpl<XOrders, String> implements XOrdersDao{

	public XOrdersDaoImpl() {
		super(XOrders.class);
		// TODO Auto-generated constructor stub
	}

}
