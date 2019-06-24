package com.kssj.frame.command.oo;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
* @Description: (#FrameWork#)	standard-query
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午05:00:25
* @version V1.0
*/
@SuppressWarnings("unchecked")
public class FieldCommandImpl implements CriteriaCommand {
	
	private String property;
	private Object value;
	private String operation;
	
	private QueryFilter filter;
	
	public FieldCommandImpl(String property, Object value, String operation,QueryFilter filter) {
		this.property = property;
		this.value = value;
		this.operation = operation;
		this.filter=filter;
	}


	/**
	* Criteria查询
	*/
	public Criteria execute(Criteria criteria) {
	   	
    	String[] propertys=property.split("[.]");
    	
    	if(propertys!=null&&propertys.length>1){
    		if(!"vo".equals(propertys[0])){
    			for(int i=0;i<propertys.length-1;i++){
	    			if(!filter.getAliasSet().contains(propertys[i])){//TODO
	    				criteria.createAlias(propertys[i],propertys[i]);
	    				filter.getAliasSet().add(propertys[i]);
	    			}
	    		}
    		}
    	}
		
    	if("LT".equals(operation)){
			criteria.add(Restrictions.lt(property, value));
		}else if("GT".equals(operation)){
			criteria.add(Restrictions.gt(property, value));
		}else if("LE".equals(operation)){
			criteria.add(Restrictions.le(property, value));
		}else if("GE".equals(operation)){
			criteria.add(Restrictions.ge(property, value));
		}else if("LK".equals(operation)){
			criteria.add(Restrictions.like(property, "%" + value + "%").ignoreCase());
		}else if("LFK".equals(operation)){
			criteria.add(Restrictions.like(property, value + "%").ignoreCase());
		}else if("RHK".equals(operation)){
			criteria.add(Restrictions.like(property,"%" + value ).ignoreCase());
		}else if("NULL".equals(operation)){
			criteria.add(Restrictions.isNull(property));
		}else if("NOTNULL".equals(operation)){
			criteria.add(Restrictions.isNotNull(property));
		}else if("EMP".equals(operation)){
			criteria.add(Restrictions.isEmpty(property));
		}else if("NOTEMP".equals(operation)){
			criteria.add(Restrictions.isNotEmpty(property));
		}else if("NEQ".equals(operation)){
			criteria.add(Restrictions.ne(property, value));
		}else{
			criteria.add(Restrictions.eq(property, value));
		}
		
		return criteria;
	}
	
	/**
	* getPartHql
	*
	* @Description: HQL query
	* @param @return
	* @return String
	* @throws
	* @author ChenYW
	*/
	public String getPartHql() {
		
		String[]propertys=property.split("[.]");
		if(propertys!=null&&propertys.length>1){
    		if(!"vo".equals(propertys[0])){
				
				if(!filter.getAliasSet().contains(propertys[0])){
					filter.getAliasSet().add(propertys[0]);
				}
    		}
		}
		String partHql="";
		if("LT".equals(operation)){
			partHql=property + " < ? ";
			filter.getParamValueList().add(value);
		}else if("GT".equals(operation)){
			partHql=property + " > ? ";
			filter.getParamValueList().add(value);
		}else if("LE".equals(operation)){
			partHql=property + " <= ? ";
			filter.getParamValueList().add(value);
		}else if("GE".equals(operation)){
			partHql=property + " >= ? ";
			filter.getParamValueList().add(value);
		}else if("LK".equals(operation)){
			partHql=property + " like ? ";
			filter.getParamValueList().add("%"+value.toString()+"%");
		}else if("LFK".equals(operation)){
			partHql=property + " like ? ";
			filter.getParamValueList().add(value.toString()+"%");
		}else if("RHK".equals(operation)){
			partHql=property + " like ? ";
			filter.getParamValueList().add("%"+value.toString());
		}else if("NULL".equals(operation)){
			partHql=property + " is null ";		
		}else if("NOTNULL".equals(operation)){
			partHql=property + " is not null ";
		}else if("EMP".equals(operation)){

		}else if("NOTEMP".equals(operation)){
			
		}else if("NEQ".equals(operation)){
			partHql= property +" !=? "; 
			filter.getParamValueList().add(value);
		}else{
			partHql+= property + " =? ";
			filter.getParamValueList().add(value);
		}

		return partHql;
	}

	//*********************************************get : set********************************************
	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
}
