package com.kssj.order.dao.impl;

import java.util.List;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.order.dao.XFoodSendAddressDao;
import com.kssj.order.model.XFoodSendAddress;

@SuppressWarnings("unchecked")
public class XFoodSendAddressDaoImpl extends GenericDaoImpl<XFoodSendAddress,String> implements XFoodSendAddressDao {

	public XFoodSendAddressDaoImpl() {
		super(XFoodSendAddress.class);
	}

}
