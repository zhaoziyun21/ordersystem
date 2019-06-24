package com.kssj.frame.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.kssj.frame.command.oo.QueryFilter;
import com.kssj.frame.web.paging.PagingBean;

/**
* @Description: DAO工具类（开发常用操作数据库工具类）
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午04:46:17
* @version V1.0
*/
@SuppressWarnings("rawtypes")
public interface GenericDao<T, PK extends Serializable> {
	/**
	 * 保存数据
	 * @param entity 实体类
	 * @return 实体类
	 */
	public T save(T entity);
	/**
	 * 合并实体对象
	 * @param entity 实体类
	 * @return 实体类
	 */
	public T merge(T entity);
	/**
	 * 通过主键获取数据
	 * @param id 主键
	 * @return 实体类
	 */
	public T get(PK id);
	/**
	 * 通过主键删除数据
	 * @param id 主键
	 */
	public void remove(PK id);
	/**
	 * 通过实体删除数据
	 * @param entity 实体类
	 */
	public void remove(T entity);
	/**
	 * 清除指定的缓冲对象
	 * @param entity 实体类
	 */
	public void evict(T entity);
	
	/**
	 * 获取所有数据
	 * @return 数据集合
	 */
	public List<T> getAll();
	/**
	 * 分页查询表的所有数据
	 * @param pb 分页对象
	 * @return 数据集合
	 */
	public List<T> getAll(PagingBean pb);
	/**
	 * 查询符页面条件的数据
	 * @param filter hql查询过滤器
	 * @return 数据集合
	 */
	public List<T> getAll(QueryFilter filter);
	/**
	 * 通过HQL查询数据
	 * @param hql hql语句
	 * @param hql hql参数
	 * @return 数据集合
	 */
	public List<T> findByHql(String hql, Object[] objs);
	/**
	 * 通过HQL查询分页数据
	 * @param hql hql语句
	 * @param objs hql参数
	 * @param pb 分页对象
	 * @return 数据集合
	 */
	public List<T> findByHql(String hql, Object[] objs, PagingBean pb);
	/**
	 * 通过HQL查询分页数据
	 * @param hql hql语句
	 * @param objs hql参数
	 * @param firstResult 当前页
	 * @param pageSize 每页显示条目数
	 * @return 数据集合
	 */
	public List<T> findByHql(String hql, Object[] objs, int firstResult,
			int pageSize);

	/**
	 * 根据sql查询
	 * @param sql sql语句
	 * @return 数据集合
	 */
	public List findBySql(final String sql);

	/**
	 * 根据sql查询分页
	 * @param obj sql参数
	 * @param sql sql语句
	 * @param start 当前页
	 * @param limit 每页显示条目数
	 * @return 数据集合
	 */
	public List findPageSql(final Object[] obj, final String sql,
			final Integer start, final Integer limit);

	public void flush();

	/**
	 * 执行删除或更新语句
	 * @param hql hql语句
	 * @param params hql参数
	 * @return 返回影响行数
	 */
	public Long update(final String hql, final Object... params);

	/**
	 * 执行sql语句
	 * @param sql sql语句
	 */
	public void executeSql(final String sql);
	
//***********************************新增功能**************************************	
	
	/**
	* @method: findCountByHQL
	* @Description: 根据hql查询(占位符查询)
	*
	* @param hql
	* @param paramList
	* @return
	* @return List<T>
	*
	*/
	public List<T> findListByHQL(final String hql,final List paramList);
	
	/**
	* @method: findCountByHQL
	* @Description: 根据hql查询(占位符查询)
	*
	* @param hql
	* @param paramList
	* @return
	* @return List<T>
	*
	*/
	public List<T> findListByHQLForList(final String hql,final List paramList);
	
	/**
	* @method: findPageByHQLForList
	* @Description: 分页自定义查询
	*
	* @param hql
	* @param paramList
	* @param start
	* @param limit
	* @return
	* @return List<T>
	*
	*/
	public List<T> findPageByHQLForList(final String hql,final List paramList,
			final Integer start, final Integer limit);
	
	/**
	* @method: saveOrUpdate
	* @Description: 保存或者更新实体
	*
	* @param <T>
	* @param entity
	* @return
	* @return T
	*
	*/
	public T saveOrUpdate(T entity);
	
	
	/**
	 * 执行sql语句 带参数的  不用硬组装sql
	 * @param sql 执行的SQL语句
	 * @param params 传入的参数数组
	 * @return 返回受影响行数
	 */
	public Long executeSql(final String sql, final Object... params);
	
//****************************新增************************
	
	/**
	* @method: getListByJdbcTemplate
	* @Description: 专用于视图
	*
	* @param <T>
	* @param sql
	* @param paramList
	* @param rowMapper
	* @return
	*
	*/
//	public <T> List<T> getListByJdbcTemplate(final String sql, final List<?> paramList, final RowMapper<T> rowMapper);
	
	/**
	* @method: findCountByHQL
	* @Description: 根据HQL查询记录数目
	*
	* @param queryString
	* @param paramList
	* @return
	* @return Long
	*
	*/
	public Long findCountByHQL(final String hql,final List paramList);
	
	/**
	* @method: getMySession
	* @Description: 取得持久化管理器
	*
	* @return
	* @return Session
	*
	*/
	public Session getMySession();
	
	/**
	 * 通过HQL查询一个集合  关键：主要用于HQL 中有 IN 的情况
	 * @param [hql]     [需要执行的hql]
	 * @param [paramMap]     [in (这里需要传入的集合)]
	 * @params [listName]      [hql中拼接的集合名字]
	 */
	public List<T> findListHqlParamsList(final String hql,final List<Map<String, List<String>>> paramMap);
	
	/**
	* @method: find
	* @Description: *****************分页
	*
	* @param queryString
	* @param values
	* @param firstResult
	* @param pageSize
	* @return
	* @return List<T>
	*
	*/
	public List<T> find(final String queryString, final Object[] values,final int firstResult, final int pageSize);
	
	/**
	 * 通过HQL查询一个集合，可分页  
	 * 关键：主要用于HQL 中有 IN 的情况
	 * @param 	[hql]     		[需要执行的hql]
	 * @param 	[paramMap]     	[in (这里需要传入的集合)]
	 * @param 	[listName]      [hql中拼接的集合名字]
	 * @param	[queryFilter]	[分页条件组装对象]
	 */
	public List<T> findListHqlParamsList(final String hql, final List<Map<String, List<String>>> paramMap,final QueryFilter queryFilter);
	
	/**
 	 * 执行存储过程 返回查询列表
 	 *---------------调用举例1 无结果集返回-----------------------------------------------------------
 	 *   1.存储过程
 	 *    CREATE OR REPLACE PROCEDURE test1
     *        (
     *         id            VARCHAR2,
     *         out_count OUT INTEGER 
     *        )
     *    AS 
     *    BEGIN
     *          SELECT count(*) INTO out_count  FROM MCODE WHERE M_ID>=id;
     *    END ;
     *    
     *   2.java 调用代码
 	 *   Map<String , Object[]> m=new HashMap<String, Object[]>();
 	 *   m.put("parameterNames", new Object[]{"id","out_count"});  //申明参数顺序
	 *	 m.put("id"       ,new Object[]{"1" ,java.sql.Types.CHAR   });
	 *	 m.put("out_count",new Object[]{null,java.sql.Types.INTEGER});//输出参数一定要以"out" 开始
	 *	 Map<String, Object> returnMap = dbdglDao.execQueryProcedure("{call test1(?,?)}", m);
	 *	 ResultSet rs=(ResultSet)returnMap.get("rs");   //对于 sybase ,sqlserver 获取结果集,对于 oracle 有些特殊,见下个例子
	 *	 System.out.println(returnMap.get("out_count"));//获取输出参数
 	 * -----------------------调用举例2   有结果集返回------------------------------------------------------
 	 *    1. 定义返回集 所用游标
 	 *    create or replace package pkg_emc 
     *    as    
     *       type list is ref cursor;
     *    end pkg_emc;
     *    2. 存储过程 
     *     create or replace procedure mcode_list(out_list out pkg_emc.list) 
     *     is   
     *     begin
     *       open out_list for select * from mcode;
     *     end mcode_list;
     *    3.java 调用代码
 	 *    Map<String , Object[]> m=new HashMap<String, Object[]>();
 	 *    m.put("parameterNames", new Object[]{"out_list"});  //申明参数顺序
	 *	  m.put("out_list",new Object[]{null,oracle.jdbc.OracleTypes.CURSOR});//输出参数一定要以"out" 开始
	 *	  Map<String, Object> returnMap = dbdglDao.execQueryProcedure("{call mcode_list(?)}", m);
	 *	  ResultSet rs=(ResultSet)returnMap.get("out_list");
	 *	  while(rs.next()){ system.out.println( rs.getString("M_CODE"));}
	 *
 	 * @author  ChenYW
 	 * @param callSql 要调用的存储过程 例如:  {call ProcedureName(?,?,?)} 
 	 * @param pMap 为参数列表map, 其中  key 为 参数名称, value 为object 数组，其中 object[0] 为参数的实际值 ,  object[1] 为参数类型 ，参数类型 是 java.sql.Types 中常量类型
 	 *        其中传出参数 必须以 “out” 字符串开头，传出参数 value 中  object[0] 直接设置为null 即可，但   object[1] 必须设置
 	 *        
 	 * @return Map<String,Object>  其中map.get("rs") 为 返回的 ResultSet，其他返回值也可用 传入参数名称获取
 	 * 
 	 */
	public Map<String,Object> execQueryProcedure(final String callSql ,final Map<String,Object[]> pMap);
	
	public List<Map>  findBySqlToMap( final String sql,final Object[] params,final int currPage, final int pageSize);
}
