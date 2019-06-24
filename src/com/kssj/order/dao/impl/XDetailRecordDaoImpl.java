package com.kssj.order.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.order.dao.XDetailRecordDao;
import com.kssj.order.model.XDetailRecord;

@SuppressWarnings("unchecked")
public class XDetailRecordDaoImpl extends GenericDaoImpl<XDetailRecord, String> implements XDetailRecordDao{

	public XDetailRecordDaoImpl() {
		super(XDetailRecord.class);
		// TODO Auto-generated constructor stub
	}

}
