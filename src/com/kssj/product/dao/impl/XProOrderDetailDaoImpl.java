package com.kssj.product.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.product.dao.XProOrderDetailDao;
import com.kssj.product.model.XProOrderDetail;

@SuppressWarnings("unchecked")
public class XProOrderDetailDaoImpl extends GenericDaoImpl<XProOrderDetail, String> implements XProOrderDetailDao{

	public XProOrderDetailDaoImpl() {
		super(XProOrderDetail.class);
	}

}
