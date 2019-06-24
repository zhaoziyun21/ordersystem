package com.kssj.order.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.order.dao.XAppointRelationDao;
import com.kssj.order.model.XAppointRelation;

@SuppressWarnings("unchecked")
public class XAppointRelationDaoImpl  extends GenericDaoImpl<XAppointRelation,String> implements XAppointRelationDao {

	public XAppointRelationDaoImpl() {
		super(XAppointRelation.class);
	}

}
