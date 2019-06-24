package com.kssj.frame.command.sql;

/**
* @Description: sql: the speller of a single confition
* 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 上午09:54:27
* @version V1.0
*/
public abstract class SqlConditionSpeller implements SqlSpeller{
	
	private String property;				/** property name */
	private Object value;					/** value 		  */
	private SqlSpellerOperatorType operator;/** operator 	  */
	private SqlSpellerDataType valueType;	/** value type 	  */
	private SqlSpellerDbType dbType;		/** DB type 	  */
	
	/** speller type */
	private SqlSpellerType spellType = SqlSpellerType.CONDITION;
	

	public SqlConditionSpeller(){}
	
	/**
	 * 	constructor
	 * 
	 * @param property 
	 * @param operator 
	 * @param value 
	 * @param valueType 
	 * @param dbType 
	 */
	public SqlConditionSpeller(String property, SqlSpellerOperatorType operator, Object value,SqlSpellerDataType valueType, SqlSpellerDbType dbType) {
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.valueType = valueType;
		this.dbType = dbType;
	}
	
	/**
	 * ①.spell a confitional statements of the sql	
	 */
	public String spell() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append(" " + property + " ");
		
		if(operator.equals(SqlSpellerOperatorType.LK)){
			sb.append(" like '%" + value + "%'");
		}else if(operator.equals(SqlSpellerOperatorType.EQ)){
			sb.append(" = " + valueToString(valueType,value) );
		}else if(operator.equals(SqlSpellerOperatorType.GT)){
			sb.append(" > " + valueToString(valueType,value) );
		}else if(operator.equals(SqlSpellerOperatorType.GE)){
			sb.append(" >= " + valueToString(valueType,value) );
		}else if(operator.equals(SqlSpellerOperatorType.LT)){
			sb.append(" < " + valueToString(valueType,value) );
		}else if(operator.equals(SqlSpellerOperatorType.LE)){
			sb.append(" <= " + valueToString(valueType,value) );
		}else if(operator.equals(SqlSpellerOperatorType.IN)){
			sb.append(" in (" + valueToString(valueType,value)+")" );
		}
		
		else if(operator.equals(SqlSpellerOperatorType.NEQ)){
			sb.append(" <> " + valueToString(valueType,value) );
		}else if(operator.equals(SqlSpellerOperatorType.NULL)){
			sb.append(" is null ");
		}else if(operator.equals(SqlSpellerOperatorType.NOTNULL)){
			sb.append(" is not null ");
		}
//		else if(operator.equals(SqlSpellerOperatorType.EMP)){
//			sb.append(" is empty ");
//		}else if(operator.equals(SqlSpellerOperatorType.NOTEMP)){
//			sb.append(" is not empty ");
//		}
		else{
			throw new RuntimeException("操作类型错误!");
		}
		
		return sb.toString();
	}
	/**
	 * 把值转换成sql语句
	 * @param type 值类型
	 * @param value 值
	 * @return 
	 * @throws Exception
	 */
	abstract public String valueToString(SqlSpellerDataType type,Object value) throws Exception;
	
	
	/**
	 * 2.获取拼写器类型
	 * @return 拼写器类型
	 */
	public SqlSpellerType getSpellType() {
		return spellType;
	}
	
	
	
	/**====================================================================
	 * 3.
	 */
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public SqlSpellerOperatorType getOperator() {
		return operator;
	}
	public void setOperator(SqlSpellerOperatorType operator) {
		this.operator = operator;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public SqlSpellerDataType getValueType() {
		return valueType;
	}
	public void setValueType(SqlSpellerDataType valueType) {
		this.valueType = valueType;
	}
	public SqlSpellerDbType getDbType() {
		return dbType;
	}
	public void setDbType(SqlSpellerDbType dbType) {
		this.dbType = dbType;
	}
	

}
