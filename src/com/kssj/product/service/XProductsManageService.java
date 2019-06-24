package com.kssj.product.service;

import java.util.List;
import java.util.Map;

import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.GenericService;
import com.kssj.product.model.XProExamine;
import com.kssj.product.model.XProducts;

public interface XProductsManageService extends GenericService<XProducts, String> {
	
	public List<Map> getAllProducts(SqlQueryFilter filter, String startTime,
			String endTime, String flag);

	//public void saveProduct(XProducts producs);

	public void updateStatus(String id, String status);

	public List<Map> queryMyProOrder(String order_id);

	//审批状态中的产品
	public List<Map> getExamineAllProducts(SqlQueryFilter qf, String startTime,
			String endTime, String flag);

	public void saveProductAndProExamine(XProducts xProducts,
			XProExamine xProExamine);
	
}
