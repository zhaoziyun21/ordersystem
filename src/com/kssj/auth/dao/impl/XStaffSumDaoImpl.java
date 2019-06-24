package com.kssj.auth.dao.impl;

import com.kssj.auth.dao.XStaffSumDao;
import com.kssj.auth.model.XStaffSum;
import com.kssj.frame.dao.impl.GenericDaoImpl;

@SuppressWarnings("unchecked")
public class XStaffSumDaoImpl extends GenericDaoImpl<XStaffSum,String> implements XStaffSumDao{

	public XStaffSumDaoImpl() {
		super(XStaffSum.class);
	}

}
