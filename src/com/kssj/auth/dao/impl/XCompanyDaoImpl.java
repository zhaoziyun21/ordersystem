package com.kssj.auth.dao.impl;

import com.kssj.auth.dao.XCompanyDao;
import com.kssj.auth.model.XCompany;
import com.kssj.frame.dao.impl.GenericDaoImpl;

@SuppressWarnings("unchecked")
public class XCompanyDaoImpl extends GenericDaoImpl<XCompany, String> implements XCompanyDao{

	public XCompanyDaoImpl() {
		super(XCompany.class);
	}

}
