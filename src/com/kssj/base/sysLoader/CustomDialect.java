package com.kssj.base.sysLoader;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLDialect;

/**
* @Description: TODO
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 上午10:46:53
* @version V1.0
*/
public class CustomDialect extends MySQLDialect {
	 public CustomDialect() {   
		    super();   
		    registerHibernateType(Types.DECIMAL, Hibernate.BIG_DECIMAL.getName());   
		    registerHibernateType(-1, Hibernate.TEXT.getName());   
		} 

}
