package com.kssj.product.service;

import com.kssj.frame.service.GenericService;
import com.kssj.product.model.XProExamine;

public interface XProExamineService extends GenericService<XProExamine, String> {

	XProExamine getExamineDetail(String id);
	
}
