package com.kssj.product.service;

import java.util.List;
import java.util.Map;

import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.GenericService;
import com.kssj.product.model.XProCategory;

public interface XProCategoryService extends GenericService<XProCategory, String> {

	List<Map> getProCategoryList(SqlQueryFilter qf, String str);

	List<XProCategory> getAllAvailable();

}
