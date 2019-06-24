package com.kssj.order.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.order.dao.XFoodBillDao;
import com.kssj.order.model.XFoodBill;

@SuppressWarnings("unchecked")
public class XFoodBillDaoImpl  extends GenericDaoImpl<XFoodBill,String> implements XFoodBillDao {

	public XFoodBillDaoImpl() {
		super(XFoodBill.class);
		// TODO Auto-generated constructor stub
	}

}
