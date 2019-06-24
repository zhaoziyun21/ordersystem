package com.kssj.frame.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.hql.ast.QueryTranslatorImpl;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.kssj.frame.command.oo.CriteriaCommand;
import com.kssj.frame.command.oo.QueryFilter;
import com.kssj.frame.command.oo.SortCommandImpl;
import com.kssj.frame.dao.DynamicDao;
import com.kssj.frame.web.paging.PagingBean;


/**
* @Description: 通过它动态操作底层表(hibernate)
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午03:09:57
* @version V1.0
*/
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DynamicDaoImpl implements DynamicDao{
	
	public DynamicDaoImpl(String entityClassName) {
		this.entityClassName=entityClassName;
	}
	
	public DynamicDaoImpl() {
		
	}

	// 持久实体类
	private String entityClassName;
	//session工厂
	private SessionFactory sessionFactory;
	//hibernate模板
	private HibernateTemplate hibernateTemplate;

	public String getEntityClassName() {
		return entityClassName;
	}
	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public HibernateTemplate getHibernateTemplate() {
		if(hibernateTemplate==null){
			hibernateTemplate=new HibernateTemplate(sessionFactory);
		}
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/*
	 * (non-Javadoc) 保存
	 * @see com.ccse.core.dao.DynamicDao#save(java.lang.Object)
	 */
	public Object save(Object entity) {
		 getHibernateTemplate().save(entityClassName,entity);
		 return entity;
	}
	/*
	 * (non-Javadoc) 修改
	 * @see com.ccse.core.dao.DynamicDao#merge(java.lang.Object)
	 */
	public Object merge(Object entity) {
		getHibernateTemplate().merge(entityClassName,entity);
		return entity;
	}
	/*
	 * (non-Javadoc)查询
	 * @see com.ccse.core.dao.DynamicDao#get(java.io.Serializable)
	 */
	public Object get(Serializable id) {
		return getHibernateTemplate().load(entityClassName, id);
	}
	/*
	 * (non-Javadoc)删除
	 * @see com.ccse.core.dao.DynamicDao#remove(java.io.Serializable)
	 */
	public void remove(Serializable id) {
		getHibernateTemplate().delete(entityClassName,get(id));
	}
	/*
	 * (non-Javadoc)删除
	 * @see com.ccse.core.dao.DynamicDao#remove(java.lang.Object)
	 */
	public void remove(Object entity) {
		getHibernateTemplate().delete(entityClassName,entity);
	}
	/*
	 * (non-Javadoc)内存清除指定对象
	 * @see com.ccse.core.dao.DynamicDao#evict(java.lang.Object)
	 */
	public void evict(Object entity) {
		getHibernateTemplate().evict(entity);
	}
	
	/**
	 * 返回queryString查询返回的记录数
	 * 
	 * @param queryString
	 * @param values
	 * @return Long
	 */
	public Long getTotalItems(String queryString,final Object[] values) {
		
		int orderByIndex=queryString.toUpperCase().indexOf(" ORDER BY ");
		
		if(orderByIndex!=-1){
			queryString=queryString.substring(0, orderByIndex);
		}
		
		QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(queryString, queryString, 
		java.util.Collections.EMPTY_MAP, (org.hibernate.engine.SessionFactoryImplementor)getSessionFactory());
		queryTranslator.compile(java.util.Collections.EMPTY_MAP, false);
		final String sql="select count(1) from (" + queryTranslator.getSQLString() + ") tmp_count_t"; 
		
		Object reVal= getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query= session.createSQLQuery(sql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.uniqueResult();
			}
		});
		
		return new Long(reVal.toString());
	}
	/*
	 * (non-Javadoc)
	 * @see com.ccse.core.dao.DynamicDao#getAll()
	 */
	public List<Object> getAll() {
		return (List<Object>)getHibernateTemplate().execute(new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
					String hql="from " + entityClassName;
					Query query=session.createQuery(hql);
					return query.list();
			}
		});
	}
	/*
	 * (non-Javadoc)
	 * @see com.ccse.core.dao.DynamicDao#getAll(com.ccse.core.web.paging.PagingBean)
	 */
	public List<Object> getAll(final PagingBean pb) {
		final String hql="from " + entityClassName;
		int totalItems=getTotalItems(hql,null).intValue();
		pb.setTotalItems(totalItems);
		return (List<Object>)getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query=session.createQuery(hql);
				query.setFirstResult(pb.getFirstResult()).setFetchSize(pb.getPageSize());
				query.setMaxResults(pb.getPageSize());
				return query.list();
			}
		});
	}
	/*
	 * (non-Javadoc)
	 * @see com.ccse.core.dao.DynamicDao#getAll(com.ccse.core.command.QueryFilter)
	 */
	public List<Object> getAll(final QueryFilter queryFilter) {
		
    	int totalCounts=getCountByFilter(queryFilter);
    	//设置总记录数
    	queryFilter.getPagingBean().setTotalItems(totalCounts);

    	List<Object> resultList=(List<Object>)getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				  Criteria criteria = session.createCriteria(entityClassName);
				  //重新清除alias的命名，防止计算记录行数后名称还存在该处
				  queryFilter.getAliasSet().clear();
				  setCriteriaByQueryFilter(criteria,queryFilter);
	              return criteria.list();
			}
		});

    	return resultList;
	}
	
	protected int getCountByFilter(final QueryFilter filter) {
        Object count = (Object) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(entityClassName);
                for(int i=0;i<filter.getCommands().size();i++){
                	CriteriaCommand command=filter.getCommands().get(i);
                	if (!(command instanceof SortCommandImpl)) {
                		criteria = command.execute(criteria);
					}
                }
                criteria.setProjection(Projections.rowCount());
                return criteria.uniqueResult();
            }
        });
        if(count==null) return new Integer(0);
        return new Integer(count.toString());
    }
	
	private Criteria setCriteriaByQueryFilter(Criteria criteria, QueryFilter filter){
		for(int i=0;i<filter.getCommands().size();i++){
			criteria=filter.getCommands().get(i).execute(criteria);
		}
		
		criteria.setFirstResult(filter.getPagingBean().getFirstResult());
		criteria.setMaxResults(filter.getPagingBean().getPageSize());

		return criteria;
	}

}
