package com.kssj.order.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.order.dao.XFoodDao;
import com.kssj.order.model.XFood;

@SuppressWarnings("unchecked")
public class XFoodDaoImpl extends GenericDaoImpl<XFood, String> implements XFoodDao {

	public XFoodDaoImpl() {
		super(XFood.class);
		// TODO Auto-generated constructor stub
	}

}
