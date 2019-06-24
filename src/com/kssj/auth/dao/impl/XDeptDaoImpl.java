package com.kssj.auth.dao.impl;

import com.kssj.auth.dao.XDeptDao;
import com.kssj.auth.model.XDept;
import com.kssj.frame.dao.impl.GenericDaoImpl;

@SuppressWarnings("unchecked")
public class XDeptDaoImpl extends GenericDaoImpl<XDept, String> implements XDeptDao{

	public XDeptDaoImpl() {
		super(XDept.class);
	}

}
