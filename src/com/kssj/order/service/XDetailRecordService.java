package com.kssj.order.service;

import java.util.List;

import com.kssj.frame.service.GenericService;
import com.kssj.order.model.XDetailRecord;

public interface XDetailRecordService extends GenericService<XDetailRecord, String>{
	
	/**
	 * @method: getRecordById
	 * @Description: 根据id查询记录
	 * @param flag ：1、部门，2、用户
	 * @param id
	 * @return
	 * @author : lig
	 * @date 2017-3-15 下午2:44:06
	 */
	public List<XDetailRecord> getRecordById(String flag,String id);
}
