package com.kssj.auth.dao.impl;

import com.kssj.auth.dao.XDeptSumDao;
import com.kssj.auth.model.XDeptSum;
import com.kssj.frame.dao.impl.GenericDaoImpl;

@SuppressWarnings("unchecked")
public class XDeptSumDaoImpl extends GenericDaoImpl<XDeptSum, String> implements XDeptSumDao{

	public XDeptSumDaoImpl() {
		super(XDeptSum.class);
	}

}
