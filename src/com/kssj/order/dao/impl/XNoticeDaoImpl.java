package com.kssj.order.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.order.dao.XNoticeDao;
import com.kssj.order.model.XNotice;

@SuppressWarnings("unchecked")
public class XNoticeDaoImpl extends GenericDaoImpl<XNotice, String> implements XNoticeDao {

	public XNoticeDaoImpl() {
		super(XNotice.class);
	}

}
