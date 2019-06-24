package com.kssj.product.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.product.dao.XProExamineDao;
import com.kssj.product.model.XProExamine;

@SuppressWarnings("unchecked")
public class XProExamineDaoImpl extends GenericDaoImpl<XProExamine, String> implements XProExamineDao {

	public XProExamineDaoImpl() {
		super(XProExamine.class);
	}

}
