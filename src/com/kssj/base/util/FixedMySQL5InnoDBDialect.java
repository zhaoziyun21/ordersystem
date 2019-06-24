package com.kssj.base.util;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;

public class FixedMySQL5InnoDBDialect extends MySQL5InnoDBDialect{
	protected void registerVarcharTypes() {
		super.registerVarcharTypes();
		registerColumnType(Types.CHAR, 255, "char($l)");
		registerHibernateType(Types.CHAR, Hibernate.STRING.getName());
	}

	public FixedMySQL5InnoDBDialect() {
		super();
		registerHibernateType(Types.LONGVARCHAR, 16777215, "mediumtext");
	}  
	
}
