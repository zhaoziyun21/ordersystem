package com.kssj.frame.dao.impl;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.hql.ast.QueryTranslatorImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.kssj.frame.command.oo.CriteriaCommand;
import com.kssj.frame.command.oo.FieldCommandImpl;
import com.kssj.frame.command.oo.QueryFilter;
import com.kssj.frame.command.oo.SortCommandImpl;
import com.kssj.frame.dao.GenericDao;
import com.kssj.frame.web.paging.PagingBean;

/**
* @Description: DAO工具类（开发常用操作数据库工具类）
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午03:11:47
* @version V1.0
*/
@SuppressWarnings({"unchecked","rawtypes"})
abstract public class GenericDaoImpl<T, PK extends Serializable> extends
		HibernateDaoSupport implements GenericDao<T, PK> {
	protected Log logger = LogFactory.getLog(GenericDaoImpl.class);
	
	/**
	 * 统一添加删除标识
	 */
    protected final String DELETE_FLAG="1";
	protected JdbcTemplate jdbcTemplate;

	protected Class persistType;

	/**
	 * set the query(hql) in the app-dao.xml, then can use by
	 * getAllByQueryName(..);
	 */
	protected Map<String, String> querys = new HashMap<String, String>();

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setPersistType(Class persistType) {
		this.persistType = persistType;
	}

	public GenericDaoImpl(Class persistType) {
		this.persistType = persistType;
	}
	

	
	public void setQuerys(Map<String, String> querys) {
		this.querys = querys;
	}
	
	//***************************************************Hibernate Operation**************************************************
	
	/**
	* @method: findEntityById
	* @Description: 通过id和实体类型查找实体
	* @param @param <T>
	* @param @param id
	* @param @param entityClass
	* @param @return
	* @return T
	* @throws
	* @author ChenYW
	* @date 2013-3-4 下午10:17:37
	*/
	@SuppressWarnings("hiding")
	public <T> T findEntityById(Class<T> entityClass, Serializable id) {
		if (null == id || "".equals(id)) {
			return null;
		} else {
			return (T) getHibernateTemplate().get(entityClass, id);
		}
	}
	
	/**
	 * 根据主键返回对象
	 */
	public T get(PK id) {
		return (T) getHibernateTemplate().get(persistType, id);
	}
	
	/**
	 * 保存对象
	 */
	public T save(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}
	/**
	 * 立即执行 不会缓存你session 的事物
	 */
	public T merge(T entity) {
		getHibernateTemplate().merge(entity);
		return entity;
	}

	/**
	 * session.evict(obj)，会把指定的缓冲对象进行清除
	 * （区别于：session.clear()，把缓冲区内的全部对象清除，但不包括操作中的对象）
	 * 
	 * @author: ChenYW
	 * @date 2013-10-14 下午03:19:55
	 */
	public void evict(T entity) {
		getHibernateTemplate().evict(entity);
	}

	/**
	 * 查询（泛型内）所有数据
	 */
	public List<T> getAll() {
		return (List<T>) getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						
						String hql = "from " + persistType.getName();// + " where delFlag = '0'";
						Query query = session.createQuery(hql);
						
						return query.list();
					}
				});
	}
	
	/**
	* @method: getAll
	* @Description: 分页查询数据(根据分页实体PagingBean)
	*
	* @param pb
	* @return
	*
	* @author: ChenYW
	* @date 2013-10-14 下午03:23:26
	*/
	public List<T> getAll(final PagingBean pb) {
		final String hql = "from " + persistType.getName();
		int totalItems = getTotalItems(hql, null).intValue();
		pb.setTotalItems(totalItems);
		return (List<T>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						query.setFirstResult(pb.getFirstResult()).setFetchSize(
				
								pb.getPageSize());
						query.setMaxResults(pb.getPageSize());
						return query.list();
					}
				});
	}

	/**
	 * 根据“hql和参数数组” 查询结果集
	 * 
	 * @param hql   hql语句
	 * @param objs  hql参数
	 * 
	 * @return 结果集合
	 */
	public List<T> findByHql(final String hql, final Object[] objs) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (objs != null) {
					for (int i = 0; i < objs.length; i++) {
						query.setParameter(i, objs[i]);
					}
				}
				return (List<T>) query.list();
			}
		});
	}

	/**
	 * 根据“hql和参数数组” 带分页查询（不传分页实体类）
	 * 
	 * @param hql  			hql语句
	 * @param objs 			hql参数
	 * @param firstResult   当前页
	 * @param pageSize 		每页显示条目数量
	 * 
	 * @return 结果集合
	 */
	public List<T> findByHql(final String hql, final Object[] objs,
			final int firstResult, final int pageSize) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setFirstResult(firstResult).setMaxResults(pageSize);
				if (objs != null) {
					for (int i = 0; i < objs.length; i++) {
						query.setParameter(i, objs[i]);
					}
				}
				return (List<T>) query.list();
			}
		});
	}
	
	/**
	 * 带分页对象的hql查询
	 * @param hql hql语句
	 * @param objs hql参数
	 * @param pb 分页类
	 * @return 结果集合
	 */
	public List<T> findByHql(final String hql, final Object[] objs,
			PagingBean pb) {
		int totalItems = getTotalItems(hql, objs).intValue();
		pb.setTotalItems(totalItems);
		return findByHql(hql, objs, pb.getFirstResult(), pb.getPageSize());
	}

	/**
	 * 带分页对象的hql查询
	 * @param hql hql语句
	 * @param objs hql参数
	 * @param pb 分页类
	 * @return 结果集合
	 */
	public List find(final String hql, final Object[] objs, PagingBean pb) {
		int totalItems = getTotalItems(hql, objs).intValue();
		pb.setTotalItems(totalItems);
		return find(hql, objs, pb.getFirstResult(), pb.getPageSize());
	}
	
	/**
	 * hql查询
	 * @param hql hql语句
	 * @return 结果集合
	 */
	public List<T> findByHql(final String hql) {
		return findByHql(hql, null);
	}
	
	/**
	 * 根据id删除对象
	 * @param id 对象id
	 */
	public void remove(PK id) {
		getHibernateTemplate().delete(get(id));
	}
	
	/**
	 * 根据实体类删除对象
	 * @param id 实体类
	 */
	public void remove(T entity) {
		getHibernateTemplate().delete(entity);
	}

	/**
	* saveOrUpdate(保存或者更新实体)
	*/
	public T saveOrUpdate(T entity){
		getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}
	
	/**
	 * 通过hql查找某个唯一的实体对象
	 * 
	 * @param queryString
	 * @param values
	 * 
	 * @return
	 */
	public Object findUnique(final String hql, final Object[] values) {
		return (Object) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);

				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.uniqueResult();
			}
		});
	}

	/**
	* @method: flush
	* @Description: 刷新提交并清除缓存
	*/
	public void flush() {
		getHibernateTemplate().flush();
	}
	
	/**
	 * @Description: 执行删除或更新语句（ 不用硬组装hql）
	 * @param hql hql语句 
	 * @param params hql参数
	 * @return 返回影响行数
	 */
	public Long update(final String hql, final Object... params) {
		return (Long) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				int i = 0;
				for (Object param : params) {
					query.setParameter(i++, param);
				}
				Integer rows = query.executeUpdate();
				return new Long(rows);
			}
		});
	}
	
	/**
	* @method: findListByHQL
	* @Description: 根据hql查询(占位符查询)
	*
	* @param hql
	* @param paramList
	* @return
	* @return List<T>
	*
	* @author ChenYW
	* @date 2013-3-26 上午11:23:00
	*/
	public List<T> findListByHQL(final String hql,final List paramList){
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if(paramList!=null){
					for(int i=0;i<paramList.size();i++){
						query.setParameter(i, paramList.get(i));
					}
				}
				return (List<T>) query.list();
			}
		});
	}
			
	/**
	* @method: findListByHQLForList
	* @Description: 根据hql查询(list参数代号占位符查询，list代号名约定为”list“)
	* 				如：“in (:list)”
	*
	* @param hql
	* @param paramList
	* @return
	* @return List<T>
	*/
	public List<T> findListByHQLForList(final String hql,final List paramList){
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if(paramList!=null){
					for(int i=0;i<paramList.size();i++){
						if(paramList.get(i) instanceof   List) {
							List list = (List)paramList.get(i);
							query.setParameterList("list", list);
						} else {
							query.setParameter(i, paramList.get(i));
						}
					}
				}
				return (List<T>) query.list();
			}
		});
	}
	
	/**
	* @method: findPageByHQLForList
	* @Description: 根据hql分页查询(list参数代号占位符查询，list代号名约定为”list“)
	* 				如：“in (:list)”
	*
	* @param hql
	* @param paramList
	* @param start
	* @param limit
	* @return
	* @return List<T>
	*/
	public List<T> findPageByHQLForList(final String hql,final List paramList,
			final Integer start, final Integer limit){
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if(paramList!=null){
					for(int i=0;i<paramList.size();i++){
						if(paramList.get(i) instanceof   List) {
							List list = (List)paramList.get(i);
							query.setParameterList("list", list);
						} else {
							query.setParameter(i, paramList.get(i));
						}
					}
				}
				
				if (null != start && null != limit) {
					query.setFirstResult(start).setFetchSize(limit);
					query.setMaxResults(limit);
				}
				
				return (List<T>) query.list();
			}
		});
	}
	
	/**
	* @method: findCountByHQL
	* @Description: 查询结果集总数量（带参数）
	*
	* @param hql	如：final String hql="select count(*) "+queryString;
	* @param paramList
	* @return
	*/
	public Long findCountByHQL(final String hql,final List paramList){
		
		return (Long) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if(paramList!=null){
					for(int i=0;i<paramList.size();i++){
						query.setParameter(i, paramList.get(i));
					}
				}
				
				Long result = (Long)query.uniqueResult();
				return result;
			}
		});
	}
	
	/**
	* @method: getMySession
	* @Description: 获取Hibernate持久化管理器（session）
	*
	* @return
	*/
	public Session getMySession(){
		return super.getHibernateTemplate().getSessionFactory().getCurrentSession();
	}
	
	/**
	* @method: findListHqlParamsList
	* @Description: hql查询list（”：参数代号“占位符）（参数集合里放（Map<String, List<String>>））
	*				（使用者需注意参数类型，必须为List<Map<String, List<String>>>）
	*
	* @param hql
	* @param paramMap
	* @return
	*/
	public List<T> findListHqlParamsList(final String hql, final List<Map<String, List<String>>> paramMap) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
					
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						if(paramMap!=null)
						{
							for(int i=0;i<paramMap.size();i++){
								for(Iterator<String> it=paramMap.get(i).keySet().iterator();it.hasNext();){
									String para = it.next();//取得参数map中的key值
									query.setParameterList(para, paramMap.get(i).get(para));
								}
							}
						}
						return (List<T>) query.list();
					}
				});
	}
	
	/**
	 * 通过HQL查询一个集合，可分页  
	 * 关键：主要用于HQL 中有 IN 的情况
	 * @param 	[hql]     		[需要执行的hql]
	 * @param 	[paramMap]     	[in (这里需要传入的集合)]
	 * @param 	[listName]      [hql中拼接的集合名字]
	 * @param	[queryFilter]	[分页条件组装对象]
	 * 
	 */
	public List<T> findListHqlParamsList(final String hql, final List<Map<String, List<String>>> paramMap,final QueryFilter queryFilter) {
		if (StringUtils.isNotEmpty(queryFilter.getFilterName())) {
			return getAll2(queryFilter);
		}
		//获取总记录数
		int totalCounts = this.findListHqlParamsList(hql, paramMap).size();
		// 设置总记录数
		queryFilter.getPagingBean().setTotalItems(totalCounts);
		
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
					
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						if(paramMap!=null){
							for(int i=0;i<paramMap.size();i++){
								for(Iterator<String> it=paramMap.get(i).keySet().iterator();it.hasNext();){
									String para = it.next();
									query.setParameterList(para, paramMap.get(i).get(para));
									
									query.setFirstResult(queryFilter.getPagingBean().getFirstResult());
									query.setMaxResults(queryFilter.getPagingBean().getPageSize());
									
								}
							}
						}
						return (List<T>) query.list();
					}
				});
	}
	
	//***************************************************原生sql Operation**************************************************
	
	/**
	 * @Description: sql分页查询(sql语句不需要设置参数类型转换)
	 * 
	 * @param obj 	sql参数
	 * @param sql   sql语句
	 * @param start 起始数量
	 * @param limit 显示条目数
	 * 
	 * @return 结果集合
	 */
	public List<T> findPageSql(final Object[] obj, final String sql,
			final Integer start, final Integer limit) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session s) throws HibernateException,
					SQLException {
				
				Query query = s.createSQLQuery(sql);
				if (obj != null) {
					for (int i = 0; i < obj.length; i++) {
						Class theClass = obj[i].getClass();
						if (theClass.getName().equals("java.lang.String"))
							query.setString(i, (String) obj[i]);
						if (theClass.getName().equals("java.lang.Long"))
							query.setLong(i, (Long) obj[i]);
						if (theClass.getName().equals("java.sql.Date"))
							query.setDate(i, (Date) obj[i]);
						if (theClass.getName().equals("java.sql.Timestamp"))
							query.setTimestamp(i, (Timestamp) obj[i]);
						if (theClass.getName().equals("java.lang.Iterate"))
							query.setInteger(i, (Integer) obj[i]);
					}
				} // 根据此条件查询记录条数
				if(start!=null&&limit!=null){
					query.setFirstResult(start);
					query.setMaxResults(limit);
				}
				List<T> list = query.list();
				return list;
			}
		});
	}
	
	/**
	* @method: find
	* @Description: sql分页查询(sql语句设置参数类型转换)
	*
	* @param queryString   sql语句
	* @param values		   sql参数
	* @param firstResult   当前页
	* @param pageSize      每页显示条目数
	* @return
	*
	* @author: ChenYW
	* @date 2013-10-14 下午03:17:02
	*/
	public List<T> find(final String queryString, final Object[] values,
			final int firstResult, final int pageSize) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query queryObject = session.createQuery(queryString);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				if (pageSize > 0) {
					queryObject.setFirstResult(firstResult)
							.setMaxResults(pageSize)
							.setFetchSize(pageSize);//抓取策略
				}
				return (List<T>) queryObject.list();
			}
		});
	}
	
	/**
	 * 返回queryString查询返回的记录数（带参数）
	 * 
	 * @param queryString sql语句
	 * @param values      sql参数
	 * 
	 * @return Long       记录数
	 */
	public Long getTotalItems(String queryString, final Object[] values) {

		int orderByIndex = queryString.toUpperCase().indexOf(" ORDER BY ");

		if (orderByIndex != -1) {
			queryString = queryString.substring(0, orderByIndex);

		}

		QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(
				queryString,
				queryString,
				java.util.Collections.EMPTY_MAP,
				(org.hibernate.engine.SessionFactoryImplementor) getSessionFactory());
		queryTranslator.compile(java.util.Collections.EMPTY_MAP, false);
		final String sql = "select count(*) as c from ("
				+ queryTranslator.getSQLString() + ") tmp_count_t";

		Object reVal = getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.uniqueResult();
			}
		});

		// if(reVal==null) return new Long(0);

		return new Long(reVal.toString());

	}
	
	/**
	 * 根据sql查询（不带参数）
	 * 
	 * @param sql  sql语句
	 * 
	 * @return 结果集合
	 */
	public List<T> findBySql(final String sql) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				return (List<T>) query.list();
			}
		});
	}
	
	/**
	 * 执行sql语句
	 */
	public void executeSql(final String sql) {
		this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createSQLQuery(sql);
				query.executeUpdate();
				return null;
			}
		});
	}
	
	/**
	 * 执行sql语句 带参数的（ 不用硬组装sql）
	 */
	public Long executeSql(final String sql, final Object... params) {
		return (Long)this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createSQLQuery(sql);
				int i = 0;
				for (Object param : params) {
					query.setParameter(i++, param);
				}
				Integer rows = query.executeUpdate();
				return new Long(rows);
			}
		});
	}
	
	
	//**************************新增***************************
	/**
 	 * 执行存储过程 返回查询列表
 	 * @author  ChenYW
 	 * @param callSql 要调用的存储过程 例如:  {call ProcedureName(?,?,?)} 
 	 * @param pMap 为参数列表map, 其中  key 为 参数名称, value 为object 数组，其中 object[0] 为参数的实际值 ,  object[1] 为参数类型 ，参数类型 是 java.sql.Types 中常量类型
 	 *        其中传出参数 必须以 “out” 字符串开头，传出参数 value 中  object[0] 直接设置为null 即可，但   object[1] 必须设置
 	 *        
 	 * @return Map<String,Object>  其中map.get("rs") 为 返回的 ResultSet，其他返回值也可用 传入参数名称获取
 	 * 
 	 */
	public Map<String,Object> execQueryProcedure(final String callSql ,final Map<String,Object[]> pMap){
	 return	(Map<String,Object>)this.jdbcTemplate.execute(callSql, new CallableStatementCallback<Object>() {
			public  Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<String> outParam=new ArrayList<String>();
				Iterator<?> it=null;  
				Map<String,Object> returnMap=new HashMap<String,Object>();
				if(null!=pMap && !pMap.isEmpty()){
					
					/**
					 * 本来认为 oracle 中 用参数名称 设置 参数值就可以忽略参数位置，但事与愿违
					 * 所以添加参数 parameterNames，用于来说明 参数顺序
					 * 例如:
					 * m.put("parameterNames", new Object[]{"p1","p2","p3","out_p"});
					 * 那么在设置参数是就不会发生位置顺序错误 而导致 参数值设置错误,
					 * 如果只有一个参数 则可以不设置此参数
					 */
					if(null==pMap.get("parameterNames")){
			    	     it=pMap.keySet().iterator();
					}else {
						 List<Object> parameterList = Arrays.asList(pMap.get("parameterNames"));
						 it=parameterList.iterator();
					}
			    	while (it.hasNext()) {
					   String next=(String)it.next();
					   if(next.equalsIgnoreCase("parameterNames")){
						   continue;
					   }
					   else if(!next.startsWith("out")){//存储过程返回的参数名称以out 开头 
						   cs.setObject(next, pMap.get(next)[0],(Integer)pMap.get(next)[1] );
					   }else{
						   outParam.add(next);
						   cs.registerOutParameter(next, (Integer)pMap.get(next)[1]);
					   }
					}
			    }
			   
				ResultSet rs=cs.executeQuery();
				returnMap.put("rs", rs);
			    it=outParam.iterator();
			    while(it.hasNext()){
			       String key=(String)it.next();
			       Object o=cs.getObject(key);
			       returnMap.put(key, o);
			    }
				return returnMap;
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//**********************************core Operation(开发者不需调用)*************************************
	// ---------------------Query Filter
	// Start----------------------------------------------------------
	public int getCountByFilter(final QueryFilter filter) {
		Integer count = (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Criteria criteria = session.createCriteria(persistType);
						for (int i = 0; i < filter.getCommands().size(); i++) {
							CriteriaCommand command = filter.getCommands().get(
									i);
							if (!(command instanceof SortCommandImpl)) {
								criteria = command.execute(criteria);
							}
						}
						criteria.setProjection(Projections.rowCount());
						return criteria.uniqueResult();
					}
				});
		if (count == null)
			return new Integer(0);
		return count.intValue();
	}
	
	/**
	 * 根据QueryFilter 查询
	 * @param queryFilter hql查询过滤器
	 * @return 结果集合
	 */
	public List getAll(final QueryFilter queryFilter) {
		if (StringUtils.isNotEmpty(queryFilter.getFilterName())) {// 如果为空，则是根据自己写的hql语句的查询语句 TODO 
			return getAll2(queryFilter);
		}

		int totalCounts = getCountByFilter(queryFilter);
		// 设置总记录数
		queryFilter.getPagingBean().setTotalItems(totalCounts);

		List<T> resultList = (List<T>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Criteria criteria = session.createCriteria(persistType);
						// 重新清除alias的命名，防止计算记录行数后名称还存在该处
						queryFilter.getAliasSet().clear();
						setCriteriaByQueryFilter(criteria, queryFilter);
						return criteria.list();
					}
				});

		if (queryFilter.isExport()) {
			SimpleDateFormat tempDate = new SimpleDateFormat(
					"yyyyMMddhhmmssSSS");
			String datetime = tempDate.format(new java.util.Date());
			queryFilter.getRequest().setAttribute("fileName", datetime);
			queryFilter.getRequest().setAttribute("__exportList", resultList);
		}

		return resultList;
	}

	/**
	 * 按Hql查询并返回
	 * @param queryFilter hql查询过滤器
	 * @return 结果集合
	 */
	public List getAll2(QueryFilter queryFilter) {
		String hql = querys.get(queryFilter.getFilterName()).trim();

		String newHql = null;
		String condition = null;
		String groupBy = null;

		// 重新设置排序
		int orderIndex = hql.toUpperCase().indexOf(" ORDER BY ");
		int whereIndex = hql.toUpperCase().indexOf(" WHERE ");

		if (orderIndex < 0) {
			orderIndex = hql.length();
		}
		if (whereIndex < 0) {
			whereIndex = hql.length();
		}

		if (whereIndex < 0) {
			condition = " where 1=1 ";
		} else {
			condition = hql.substring(whereIndex + 7, orderIndex);

			logger.debug("condition:" + condition);

			Pattern groupByPattern = Pattern.compile(" GROUP BY [\\w|.]+");
			Matcher m = groupByPattern.matcher(condition.toUpperCase());
			// 存在Group By
			if (m.find()) {
				groupBy = condition.substring(m.start(), m.end());
				condition = condition.replace(groupBy, " ");
			}
			condition = " where (" + condition + ")";
		}

		String sortDesc = "";

		// 取得条件以及排序
		for (int i = 0; i < queryFilter.getCommands().size(); i++) {
			CriteriaCommand command = queryFilter.getCommands().get(i);
			if (command instanceof FieldCommandImpl) {
				condition += " and "
						+ ((FieldCommandImpl) command).getPartHql();
			} else if (command instanceof SortCommandImpl) {
				if (!"".equals(sortDesc)) {
					sortDesc += ",";
				}
				sortDesc += ((SortCommandImpl) command).getPartHql();
			}
		}
		
		newHql = hql.substring(0, whereIndex);

		if (queryFilter.getAliasSet().size() > 0) {
			// 取得hql中的表的别名，为关联外表作准备
			int fromIndex = newHql.indexOf(" FROM ");
			String entityAliasName = null;
			if (fromIndex > 0) {
				String afterFrom = newHql.substring(fromIndex + 6);

				String[] keys = afterFrom.split("[ ]");
				if (keys.length > 1) {
					if (!keys[1].toUpperCase().equals("ORDER")
							&& !keys[1].toUpperCase().equals("JOIN")) {
						entityAliasName = keys[1];
					}
				}
				// 加上别名
				if (entityAliasName == null) {
					entityAliasName = "vo";
					newHql = newHql.replace(keys[0], keys[0] + " "
							+ entityAliasName);
				}
			}

			// 若存在外键，则进行组合
			String joinHql = "";
			Iterator it = queryFilter.getAliasSet().iterator();
			while (it.hasNext()) {
				String joinVo = (String) it.next();
				joinHql += " join " + entityAliasName + "." + joinVo + " "
						+ joinVo;
			}

			// 加上外键的联接
			if (!"".equals(joinHql)) {
				newHql += joinHql;
			}
		}
		// 加上条件限制
		newHql += condition;

		// 加上分组
		if (groupBy != null) {
			newHql += groupBy + " ";
		}

		// 加上排序
		if (!"".equals(sortDesc)) {// 带在排序在内
			newHql += " order by " + sortDesc;
		} else {
			newHql += hql.substring(orderIndex);
		}

		Object[] params = queryFilter.getParamValueList().toArray();

		// 显示多少条记录
		int totalItems = getTotalItems(newHql, params).intValue();
		queryFilter.getPagingBean().setTotalItems(totalItems);
		if (logger.isDebugEnabled()) {
			logger.debug("new hql:" + newHql);
		}
		return find(newHql, params, queryFilter.getPagingBean()
				.getFirstResult(), queryFilter.getPagingBean().getPageSize());
	}
	
	private Criteria setCriteriaByQueryFilter(Criteria criteria, QueryFilter filter) {
		for (int i = 0; i < filter.getCommands().size(); i++) {
			criteria = filter.getCommands().get(i).execute(criteria);
		}

		if ("true".equals(filter.getRequest().getParameter("isExportAll"))) {
			criteria.setFirstResult(0);
			criteria.setMaxResults(filter.getPagingBean().getTotalItems());
		} else {
			criteria.setFirstResult(filter.getPagingBean().getFirstResult());
			criteria.setMaxResults(filter.getPagingBean().getPageSize());
		}

		return criteria;
	}
	/**③
	 *  sql基础查询（返回的是Map） ，带分页
	 * @param sql 		sql语句
	 * @param params 	条件的值,对应sql语句中的"?"
	 * @param currPage  当前页
	 * @param pageSize  每页显示条目数量
	 * 
	 * @return List<Map>查询结果集合（Criteria.ALIAS_TO_ENTITY_MAP）
	 * 					key：列名；value:对应的值
	 */
	
	public List<Map>  findBySqlToMap( final String sql,final Object[] params,final int currPage, final int pageSize) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session s) throws HibernateException,SQLException {
				Query query = s.createSQLQuery(sql);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				if(params!=null && params.length>0){
					for(int i = 0 , j = params.length ; i < j ; i++){
						query.setParameter(i,params[i]);
					}
				}
				if (currPage >= 1  && pageSize >= 1) {
					query.setFirstResult((currPage - 1) * pageSize);
					query.setMaxResults(pageSize);
				}
				List list = query.list();
				return list;
			}
		});
	}
	// ----------------------Query Filter
	// End-----------------------------------------------------------
}
