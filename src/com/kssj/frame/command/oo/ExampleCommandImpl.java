package com.kssj.frame.command.oo;

import org.hibernate.Criteria;
import org.hibernate.criterion.Example;


/**
 * pojo条件查询
 * @author admin 
 *
 */
public class ExampleCommandImpl implements CriteriaCommand{
	 /**
     * 允许用户注入一个vo,然后按该vo进行查找相匹配的记录。
     */
    private Object pojoExample=null;
    
    public void setPojoExample(Object pojoEx){
    	pojoExample=pojoEx;
    }

	public ExampleCommandImpl(Object pojoExample) {
		this.pojoExample = pojoExample;
	}

	public Criteria execute(Criteria criteria) {
		if(pojoExample!=null){
        	Example exampleRestriction = Example.create(pojoExample);
        	criteria.add( exampleRestriction);
        }
		return criteria;
	}
}
