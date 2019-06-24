package com.kssj.order.service;

import java.util.List;
import java.util.Map;

import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.GenericService;
import com.kssj.order.model.XFoodSendAddress;

public interface XFoodSendAddressService extends GenericService<XFoodSendAddress, String> {

	List<Map> getAddressList(SqlQueryFilter qf, String str);
	List<Map> getAddressList2(SqlQueryFilter qf, String foodBusiness);

}
