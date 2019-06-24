package com.kssj.frame.dao;

/**
* @Description:基础表类，对于主键为long类型　，则直接继承该类，
* 				若主键为其他类型，需要直接继承GenericDaoImpl
* 				(大部分Dao仅需要继承该接口即可)
* 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午04:55:43
* @version V1.0
*/
public interface BaseDao<T> extends GenericDao<T,Long> {
	
}
