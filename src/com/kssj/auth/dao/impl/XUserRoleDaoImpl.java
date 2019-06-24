package com.kssj.auth.dao.impl;

import com.kssj.auth.dao.XUserRoleDao;
import com.kssj.auth.model.XUserRole;
import com.kssj.frame.dao.impl.GenericDaoImpl;

@SuppressWarnings("unchecked")
public class XUserRoleDaoImpl extends GenericDaoImpl<XUserRole, String> implements XUserRoleDao{

	public XUserRoleDaoImpl() {
		super(XUserRole.class);
		// TODO Auto-generated constructor stub
	}

}
