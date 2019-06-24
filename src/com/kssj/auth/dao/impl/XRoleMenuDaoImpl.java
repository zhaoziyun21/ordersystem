package com.kssj.auth.dao.impl;

import com.kssj.auth.dao.XRoleMenuDao;
import com.kssj.auth.model.XRoleMenu;
import com.kssj.frame.dao.impl.GenericDaoImpl;

@SuppressWarnings("unchecked")
public class XRoleMenuDaoImpl extends GenericDaoImpl<XRoleMenu, String> implements XRoleMenuDao{

	public XRoleMenuDaoImpl() {
		super(XRoleMenu.class);
		// TODO Auto-generated constructor stub
	}

}
