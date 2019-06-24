package com.kssj.auth.dao.impl;

import com.kssj.auth.dao.XUserDao;
import com.kssj.auth.model.XUser;
import com.kssj.frame.dao.impl.GenericDaoImpl;

@SuppressWarnings("unchecked")
public class XUserDaoImpl extends GenericDaoImpl<XUser, String> implements XUserDao{

	public XUserDaoImpl() {
		super(XUser.class);
	}

}
