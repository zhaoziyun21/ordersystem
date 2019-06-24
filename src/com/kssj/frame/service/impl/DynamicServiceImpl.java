package com.kssj.frame.service.impl;

import java.io.Serializable;
import java.util.List;

import com.kssj.frame.command.oo.QueryFilter;
import com.kssj.frame.dao.DynamicDao;
import com.kssj.frame.service.DynamicService;
import com.kssj.frame.web.paging.PagingBean;


/**
* @Description: 通过它动态操作底层表(hibernate)
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午05:16:25
* @version V1.0
*/
public class DynamicServiceImpl implements DynamicService{
	
	private DynamicDao dynamicDao;
	
	public DynamicServiceImpl() {
		
	}
	
	public DynamicServiceImpl(DynamicDao dao) {
		this.dynamicDao=dao;
	}
	
	
	public Object save(Object entity) {
		return dynamicDao.save(entity);
	}

	
	public Object merge(Object entity) {
		return dynamicDao.merge(entity);
	}

	
	public Object get(Serializable id) {
		return dynamicDao.get(id);
	}

	
	public void remove(Serializable id) {
		dynamicDao.remove(id);
	}

	
	public void remove(Object entity) {
		dynamicDao.remove(entity);
	}

	
	public void evict(Object entity) {
		dynamicDao.evict(entity);
	}

	
	public List<Object> getAll() {
		return dynamicDao.getAll();
	}

	
	public List<Object> getAll(PagingBean pb) {
		return dynamicDao.getAll(pb);
	}

	
	public List<Object> getAll(QueryFilter filter) {
		return dynamicDao.getAll(filter);
	}

}
