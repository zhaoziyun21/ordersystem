package com.kssj.order.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.order.dao.XFoodSendRegionDao;
import com.kssj.order.model.XFoodSendRegion;

@SuppressWarnings("unchecked")
public class XFoodSendRegionDaoImpl extends GenericDaoImpl<XFoodSendRegion,String> implements XFoodSendRegionDao {

	public XFoodSendRegionDaoImpl() {
		super(XFoodSendRegion.class);
	}

}
