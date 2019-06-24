package com.kssj.frame.command.oo;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

/**
* @Description: sort - query - condition
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午05:01:40
* @version V1.0
*/
public class SortCommandImpl implements CriteriaCommand{

	public Criteria execute(Criteria criteria) {
		String[]propertys=sortName.split("[.]");
    	if(propertys!=null&&propertys.length>1){
    		for(int i=0;i<propertys.length-1;i++){
    			//防止别名重复
    			if(!filter.getAliasSet().contains(propertys[i])){
    				criteria.createAlias(propertys[i],propertys[i]);
    				filter.getAliasSet().add(propertys[i]);
    			}
    		}
    	}
		if(SORT_DESC.equalsIgnoreCase(ascDesc)){
			criteria.addOrder(Order.desc(sortName));
		}else if(SORT_ASC.equalsIgnoreCase(ascDesc)){
			criteria.addOrder(Order.asc(sortName));
		}
		return criteria;
	}
	
	public SortCommandImpl(String sortName,String ascDesc,QueryFilter filter){
		this.sortName=sortName;
		this.ascDesc=ascDesc;
		this.filter=filter;
	}
	
	private String sortName;
	
	private String ascDesc;
	
	private QueryFilter filter;

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getAscDesc() {
		return ascDesc;
	}

	public void setAscDesc(String ascDesc) {
		this.ascDesc = ascDesc;
	}
	
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973)
				.append(this.sortName) 
				.append(this.ascDesc).toHashCode() ;
	}
	
	public String getPartHql(){
		return sortName + " " + ascDesc;
	}
}
