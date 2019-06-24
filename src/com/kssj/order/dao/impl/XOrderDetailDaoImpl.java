package com.kssj.order.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.order.dao.XOrderDetailDao;
import com.kssj.order.dao.XOrdersDao;
import com.kssj.order.model.XOrderDetail;
import com.kssj.order.model.XOrders;

@SuppressWarnings("unchecked")
public class XOrderDetailDaoImpl extends GenericDaoImpl<XOrderDetail, String> implements XOrderDetailDao{

	public XOrderDetailDaoImpl() {
		super(XOrderDetail.class);
		// TODO Auto-generated constructor stub
	}

}
