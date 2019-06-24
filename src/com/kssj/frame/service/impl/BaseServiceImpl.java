package com.kssj.frame.service.impl;

import com.kssj.frame.dao.GenericDao;
import com.kssj.frame.service.BaseService;

/**
* @Description: 基础表类，对于主键为long类型　，则直接继承该类，
* 				若主键为其他类型，需要直接继承GenericServiceImpl
* 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午05:10:46
* @version V1.0
*/
public class BaseServiceImpl<T> extends GenericServiceImpl<T, Long> implements BaseService<T>{

	
	@SuppressWarnings("rawtypes")
	public BaseServiceImpl(GenericDao dao) {
		super(dao);
	}
	
}
