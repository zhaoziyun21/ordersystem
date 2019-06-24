package com.kssj.order.service;

import java.util.List;
import java.util.Map;

import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.service.GenericService;
import com.kssj.order.model.XFoodSendRegion;

public interface XFoodSendRegionService extends GenericService<XFoodSendRegion, String> {

	List<Map> getRegionList(SqlQueryFilter qf, String str);

	List<XFoodSendRegion> getAllAvailable();

}
