package com.kssj.frame.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.kssj.frame.command.oo.QueryFilter;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.web.paging.PagingBean;

/**
* @Description: SERVICE工具类（开发常用操作数据库工具类）
* 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午04:56:37
* @version V1.0
*/
@SuppressWarnings("rawtypes")
public interface GenericService<T,PK extends Serializable> {
	/**
	 * 保存数据
	 * @param entity 实体类
	 * @return 实体类
	 */
	public T save(T entity);
	/**
	 * 合并指定的实体对象
	 * merge the object
	 * @param entity 实体类
	 * @return 实体类
	 */
	public T merge(T entity);
	
	/**
	 * 清除指定的缓冲对象
	 * evict the object
	 * @param entity 实体类
	 */
	public void evict(T entity);
	/**
	 * 通过主键获取实体对象
	 * @param id 主键
	 * @return 实体类
	 */
	public T get(PK id);
	
	/**
	 * 查询所有数据
	 * @return 数据集合
	 */
	//@WebMethod(operationName="getAll")
	public List<T> getAll();
	
	/**
	 * 分页查询
	 * @param pb 分页类
	 * @return 数据集合
	 */
	//@WebMethod(operationName="getAllByPb")
	public List<T> getAll(PagingBean pb);
	
	/**
	 * 查询符合页面条件的数据
	 * @param filter hql查询过滤器
	 * @return 数据集合
	 */
	//@WebMethod(operationName="getAllByFilter")
	public List<T> getAll(QueryFilter filter);
	
	/**
	 * 通过主键删除数据
	 * @param id 主键
	 */
	//@WebMethod(operationName="remove")
	public void remove(PK id);
	/**
	 * 通过实体删除数据
	 * @param entity 实体类
	 */
	//@WebMethod(operationName="removeByEntity")
	public void remove(T entity);
	
	/**
	 * 刷新当前session
	 */
	public void flush();
	
	/**
	 * 查询
	 * @param filter sql过滤器
	 * @return
	 */
	public List query(SqlQueryFilter filter);

	
}
