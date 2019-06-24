package com.kssj.order.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.order.dao.XOnlineOrdersDao;
import com.kssj.order.model.XOnlineOrders;

@SuppressWarnings("unchecked")
public class XOnlineOrdersDaoImpl extends GenericDaoImpl<XOnlineOrders,String> implements XOnlineOrdersDao{

	public XOnlineOrdersDaoImpl() {
		super(XOnlineOrders.class);
		// TODO Auto-generated constructor stub
	}

}
