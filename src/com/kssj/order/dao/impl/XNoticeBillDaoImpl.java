package com.kssj.order.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.order.dao.XNoticeBillDao;
import com.kssj.order.model.XNoticeBill;

@SuppressWarnings("unchecked")
public class XNoticeBillDaoImpl  extends GenericDaoImpl<XNoticeBill,String> implements XNoticeBillDao {

	public XNoticeBillDaoImpl() {
		super(XNoticeBill.class);
	}

}
