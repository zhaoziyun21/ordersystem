package com.kssj.product.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.product.dao.XReceiverInfoDao;
import com.kssj.product.model.XReceiverInfo;

@SuppressWarnings("unchecked")
public class XReceiverInfoDaoImpl extends GenericDaoImpl<XReceiverInfo, String> implements XReceiverInfoDao {

	public XReceiverInfoDaoImpl() {
		super(XReceiverInfo.class);
	}

}
