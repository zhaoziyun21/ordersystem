package com.kssj.product.dao.impl;

import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.product.dao.XProCategoryDao;
import com.kssj.product.model.XProCategory;

@SuppressWarnings("unchecked")
public class XProCategoryDaoImpl extends GenericDaoImpl<XProCategory,String> implements XProCategoryDao {

	public XProCategoryDaoImpl() {
		super(XProCategory.class);
	}

}
