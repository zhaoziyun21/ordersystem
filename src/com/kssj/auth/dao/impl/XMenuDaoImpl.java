package com.kssj.auth.dao.impl;

import com.kssj.auth.dao.XMenuDao;
import com.kssj.auth.model.XMenu;
import com.kssj.frame.dao.impl.GenericDaoImpl;

@SuppressWarnings("unchecked")
public class XMenuDaoImpl extends GenericDaoImpl<XMenu, String> implements XMenuDao{

	public XMenuDaoImpl() {
		super(XMenu.class);
		// TODO Auto-generated constructor stub
	}

}
