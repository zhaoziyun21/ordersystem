package com.kssj.frame.command.sql;

import org.junit.Test;

/**
* @Description: Core frame test
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午04:59:38
* @version V1.0
*/
public class SqlSpellerTest {

	@Test
	public void testSqlOracleCondition() throws Exception{
		//属性名
		String property = "user.name";
		//操作符
		SqlSpellerOperatorType operator = SqlSpellerOperatorType.EQ;
		//值
		Object value = "2012-01-01 59:59:59";
		//值类型
		SqlSpellerDataType valueType = SqlSpellerDataType.D;
		
		SqlConditionSpeller speller = new SqlConditionSpellerForOracle(property,operator,value,valueType);
		System.out.println(speller.spell());
		
	}
	
	
	@Test
	public void testSqlServerCondition() throws Exception{
		//属性名
		String property = "user.name";
		//操作符
		SqlSpellerOperatorType operator = SqlSpellerOperatorType.EQ;
		//值
		Object value = "2012-01-01 59:59:59";
		//值类型
		SqlSpellerDataType valueType = SqlSpellerDataType.D;
		
		SqlConditionSpeller speller = new SqlConditionSpellerForSqlServer(property,operator,value,valueType);
		System.out.println(speller.spell());
		
	}
	
	@Test
	public void testSqlQueryFilter() throws Exception{
		SqlOrderSpeller orderSpell_1 = new SqlOrderSpeller("a","desc");
		SqlOrderSpeller orderSpell_2 = new SqlOrderSpeller("b","desc");
		
		SqlConditionSpeller conditionSpell_1 = new SqlConditionSpellerForOracle("name","LK","张三","S");
		SqlConditionSpeller conditionSpell_2 = new SqlConditionSpellerForOracle("cdate","LT","2012-01-01","D");
		
		SqlQueryFilter qf = new SqlQueryFilter(SqlSpellerDbType.ORACLE);
		qf.addCondition(conditionSpell_1);
		qf.addCondition(conditionSpell_2);
		qf.addOrder(orderSpell_1);
		qf.addOrder(orderSpell_2);
		
		System.out.println(qf.toCondition());
		System.out.println(qf.toOrderBy());
	}
}
