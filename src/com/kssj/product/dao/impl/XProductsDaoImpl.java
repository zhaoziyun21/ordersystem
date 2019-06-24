package com.kssj.product.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.product.dao.XProductsDao;
import com.kssj.product.model.XProducts;

@SuppressWarnings("unchecked")
public class XProductsDaoImpl extends GenericDaoImpl<XProducts, String> implements XProductsDao {

	public XProductsDaoImpl() {
		super(XProducts.class);
	}

}
