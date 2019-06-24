package com.kssj.frame.service;

/**
* @Description: 基础表类，对于主键为long类型　，则直接继承该类，
* 				若主键为其他类型，需要直接继承GenericDaoImpl
* 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午04:55:02
* @version V1.0
*/
public interface BaseService<T> extends GenericService<T, Long>{
	
}
