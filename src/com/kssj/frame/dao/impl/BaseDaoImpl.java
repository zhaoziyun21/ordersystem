package com.kssj.frame.dao.impl;

import com.kssj.frame.dao.BaseDao;

/**
* @Description: 基础表类，对于主键为long类型　，则直接继承该类，
* 				若主键为其他类型，需要直接继承GenericDaoImpl
* 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午04:37:15
* @version V1.0
*/
@SuppressWarnings("unchecked")
public class BaseDaoImpl<T> extends GenericDaoImpl<T, Long> implements BaseDao<T>{

	@SuppressWarnings("rawtypes")
	public BaseDaoImpl(Class persistType) {
		super(persistType);
	}
}
