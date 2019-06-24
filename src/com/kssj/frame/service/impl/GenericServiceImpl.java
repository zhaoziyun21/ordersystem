package com.kssj.frame.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.kssj.frame.command.oo.QueryFilter;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.dao.GenericDao;
import com.kssj.frame.dao.PubDao;
import com.kssj.frame.service.GenericService;
import com.kssj.frame.util.CoreListUtil;
import com.kssj.frame.web.paging.PagingBean;

/**
* @Description: SERVICE工具类（开发常用操作数据库工具类）
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午05:16:07
* @version V1.0
*/
@SuppressWarnings({"rawtypes","unchecked"})
public class GenericServiceImpl<T,PK extends Serializable> implements GenericService<T, PK> {
	protected transient final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private PubDao pubDao;
	
	protected GenericDao<T, Serializable> dao=null;

	public void setDao(GenericDao dao) {
		this.dao = dao;
	}
	
	public GenericServiceImpl(GenericDao dao) {
		this.dao=dao;
	}

	public T get(PK id) {
		return (T)dao.get(id);
	}

	public T save(T entity) {
		return (T)dao.save(entity);
	}
	
	public T merge(T entity){
		return (T)dao.merge(entity);
	}
	
	public void evict(T entity){
		dao.evict(entity);
	}
	//@WebMethod(operationName="getAll")
	public List<T> getAll(){
		return dao.getAll();
	}
	//@WebMethod(operationName="getAllByPb")
	public List<T> getAll(PagingBean pb){
		return dao.getAll(pb);
	}
	//@WebMethod(operationName="getAllByFilter")
	public List<T> getAll(QueryFilter filter){
		return dao.getAll(filter);
	}
	
	//@WebMethod(operationName="remove")
	public void remove(PK id){
		dao.remove(id);
	}
	//@WebMethod(operationName="removeByEntity")
	public void remove(T entity){
		dao.remove(entity);
	}
	
	
	public void flush() {
		dao.flush();
	}

	public List query(SqlQueryFilter filter) {
		String sql = filter.toSql();
		List l = this.getPubDao().findBySqlToMap(sql);
		if(CoreListUtil.isNotEmpty(l)){
			filter.setTotal(l.size());
		}
		return this.getPubDao().findBySqlToMap(sql, filter.getPage(),filter.getRows());
	}
	
	public List queryV2(SqlQueryFilter filter) {
		String sql = filter.toSql();
		List l = this.getPubDao().findBySqlToMap(sql);
		if(CoreListUtil.isNotEmpty(l)){
			filter.setTotal(l.size());
		}
		return this.getPubDao().findBySqlToMap(sql, 1,10000);
	}

	public PubDao getPubDao() {
		return pubDao;
	}

	public void setPubDao(PubDao pubDao) {
		this.pubDao = pubDao;
	}

	public Logger getLogger() {
		return logger;
	}
	
}
