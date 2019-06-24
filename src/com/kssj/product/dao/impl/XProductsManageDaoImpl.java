package com.kssj.product.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.kssj.frame.command.oo.QueryFilter;
import com.kssj.frame.dao.impl.GenericDaoImpl;
import com.kssj.frame.web.paging.PagingBean;
import com.kssj.product.dao.XProductsManageDao;
import com.kssj.product.model.XProOrderDetail;
import com.kssj.product.model.XProducts;
@SuppressWarnings("unchecked")
public class XProductsManageDaoImpl extends GenericDaoImpl<XProducts, String> implements XProductsManageDao {

	public XProductsManageDaoImpl() {
		super(XProducts.class);
		// TODO Auto-generated constructor stub
	}

	
}
