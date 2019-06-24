package com.kssj.frame.service;

import java.io.Serializable;
import java.util.List;

import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.web.paging.Page;

import common.Logger;

/**
* @Description: 公共SERVICE 未使用泛型(实现并优化hibernateDao的基本功能，及大部分的sql查询)
* 				（框架用）
* 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午05:03:19
* @version V1.0
*/
@SuppressWarnings("rawtypes")
public interface PubService {
	/**
	 * 保存对象
	 * @param obj POJO类
	 */
	public void save(Object obj);
	/**
	 * 更新对象
	 * @param obj POJO类
	 */
	public void update(Object obj);
	/**
	 * 删除对象
	 * @param obj POJO类
	 */
	public void delete(Object obj);
	/**
	 * 根据主键删除对象
	 * @param c 实体类的类型
	 * @param id 主键
	 */
	public void deleteById(Class c,Serializable id);
	/**
	 * 查询
	 * @param sql sql语句
	 * @return 
	 */
	public List query(String sql);
	/**
	 * 查询
	 * @param filter sql过滤器
	 * @return
	 */
	public List query(SqlQueryFilter filter);
	/**
	 * 根据id和class加载POJO对象
	 * @param c 类
	 * @param id 主键
	 * @return
	 */
	public Object findById(Class c,Serializable id);
	
	/**
	 * 根据sql分页,分页信息查询
	 * @param sql sql语句
	 * @param page 分页信息
	 * @return
	 */
	public List query(String sql,Page page);
	/**
	 * 根据sql(带有"?"的sql),分页信息查询
	 * @param sql sql语句
	 * @param params 参数 对应sql中的"?"
	 * @param page 分页信息
	 * @return
	 */
	public List query(String sql,Object[] params,Page page);
	
	/**
	 * 获取日志对象
	 * @return 日志对象
	 */
	public Logger getLog();
}
