package com.kssj.frame.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.dao.PubDao;
import com.kssj.frame.service.PubService;
import com.kssj.frame.util.CoreListUtil;
import com.kssj.frame.web.paging.Page;
import common.Logger;

/**
* @Description: 公共SERVICE 未使用泛型(实现并优化hibernateDao的基本功能，及大部分的sql查询)
* 				（框架用）
* 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午05:17:23
* @version V1.0
*/
@SuppressWarnings("rawtypes")
public class PubServiceImpl implements PubService{
	@Resource
	private PubDao pubDao;
	
	private Logger log = Logger.getLogger(this.getClass());
	public Logger getLog() {
		return log;
	}
	public void setLog(Logger log) {
		this.log = log;
	}
	/*
	 * @see com.ccse.core.service.PubService#delete(java.lang.Object)
	 */
	public void delete(Object obj) {
		this.pubDao.delete(obj);
	}
	/*
	 * @see com.ccse.core.service.PubService#deleteById(java.lang.Class, java.io.Serializable)
	 */
	public void deleteById(Class c,Serializable id){
		this.getPubDao().delete(c, id);
	};
	/*
	 * @see com.ccse.core.service.PubService#save(java.lang.Object)
	 */
	public void save(Object obj) {
		this.pubDao.save(obj);
		
	}
	/*
	 * @see com.ccse.core.service.PubService#update(java.lang.Object)
	 */
	public void update(Object obj) {
		this.pubDao.update(obj);
		
	}
	/*
	 * @see com.ccse.core.service.PubService#query(java.lang.String)
	 */
	public List query(String sql){
		return this.pubDao.findBySqlToMap(sql);
	};
	
	/*
	 * @see com.ccse.core.service.PubService#query(java.lang.String, com.ccse.core.web.paging.Page)
	 */
	public List query(String sql,Page page){
		List l = this.getPubDao().findBySql(sql);
		if(CoreListUtil.isNotEmpty(l)){
			page.setListSize(l.size());
			int m = l.size() % page.getPageSize();
			if( m == 0){
				page.setMaxPage(l.size() / page.getPageSize());
			}else{
				page.setMaxPage(l.size() / page.getPageSize() + 1);
			}
		}else{
			page.setListSize(0);
		}
		return this.getPubDao().findBySqlToMap(sql, page.getCurrentPage(), page.getPageSize());
	};
	/*
	 * @see com.ccse.core.service.PubService#query(java.lang.String, java.lang.Object[], com.ccse.core.web.paging.Page)
	 */
	public List query(String sql,Object[] params,Page page){
		List l = this.getPubDao().findBySql(sql, params);
		if(CoreListUtil.isNotEmpty(l)){
			page.setListSize(l.size());
			int m = l.size() % page.getPageSize();
			if( m == 0){
				page.setMaxPage(l.size() / page.getPageSize());
			}else{
				page.setMaxPage(l.size() / page.getPageSize() + 1);
			}
		}else{
			page.setListSize(0);
		}
		return this.getPubDao().findBySqlToMap(sql, params, page.getCurrentPage(), page.getPageSize());
	};
	
	/*
	 * @see com.ccse.core.service.PubService#query(com.ccse.core.command.sql.SqlQueryFilter)
	 */
	public List query(SqlQueryFilter filter){
		String sql = filter.toSql();
		
		List l = this.getPubDao().findBySqlToMap(sql);
		if(CoreListUtil.isNotEmpty(l)){
			filter.setTotal(l.size());
		}
		return this.getPubDao().findBySqlToMap(sql, filter.getPage(),filter.getRows());
	};
	
	
	public Object findById(Class c,Serializable id){
		return this.getPubDao().findById(c, id);
	};
	
	public PubDao getPubDao() {
		return pubDao;
	}

	public void setPubDao(PubDao pubDao) {
		this.pubDao = pubDao;
	}
	
	

}
