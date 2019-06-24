package com.kssj.frame.command.sql;

/**
* @Description: DB type
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 上午09:29:18
* @version V1.0
*/
public enum SqlSpellerDbType {
	/** oracle */
	ORACLE,
	/** mysql */
	MYSQL,
	/** sql server */
	SQLSERVER;
	
	/**
	 * Get the DB type
	 * @param type 
	 * @return 
	 */
	public static SqlSpellerDbType getType(String type){
		if(type.toUpperCase().equals("ORACLE")){
			return SqlSpellerDbType.ORACLE;
		}else if(type.toUpperCase().equals("MYSQL")){
			return SqlSpellerDbType.MYSQL;
		}else if(type.toUpperCase().equals("SQLSERVER")){
			return SqlSpellerDbType.SQLSERVER;
		}else{
			throw new RuntimeException("错误的数据库类型");
		}
	}
}
