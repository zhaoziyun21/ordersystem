//$Id: SQLServerDialect.java 9069 2006-01-16 19:33:11Z steveebersole $
package com.kssj.base.util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.type.StringType;

/**
 * Modified version based on the work found at {@link http://opensource.atlassian.com/projects/hibernate/browse/HHH-2655}
 * 
 */
public class SQLServer2008Dialect extends SQLServerDialect {

	public SQLServer2008Dialect() {
		super();
		registerColumnType(Types.VARCHAR, 1073741823, "NVARCHAR(MAX)");
		registerColumnType(Types.VARCHAR, 2147483647, "VARCHAR(MAX)");
		registerColumnType(Types.VARBINARY, 2147483647, "VARBINARY(MAX)");
		registerHibernateType(Types.NVARCHAR, Hibernate.STRING.getName());
		registerHibernateType(Types.VARCHAR, Hibernate.STRING.getName());
		registerHibernateType(Types.LONGVARCHAR, Hibernate.TEXT.getName());
		 registerHibernateType(-1, "string");   
	        registerHibernateType(-9, "string");   
	        registerHibernateType(-16, "string");   
	        registerHibernateType(3, "double");   
	}

	
	/**
	 * Add a LIMIT clause to the given SQL SELECT
	 * 
	 * The LIMIT SQL will look like:
	 * <pre>
	 * WITH query AS (SELECT ROW_NUMBER() OVER (ORDER BY orderby) as __hibernate_row_nr__, original_query_without_orderby) 
	 * SELECT * FROM query WHERE __hibernate_row_nr__ BEETWIN offset AND offset + last 
	 * --ORDER BY __hibernate_row_nr__: Don't think that wee need this last order by clause
	 * </pre>

	 * @param querySqlString
	 *            The SQL statement to base the limit query off of.
	 * @param offset
	 *            Offset of the first row to be returned by the query (zero-based)
	 * @param limit
	 *            Maximum number of rows to be returned by the query
	 * @return A new SQL statement with the LIMIT clause applied.
	 */
	@Override
	public String getLimitString(String querySqlString, int offset, int limit) {
		String querySqlLowered = querySqlString.trim().toLowerCase();
		if(querySqlLowered.indexOf("with(nolock)")<0){
			StringBuilder sb = new StringBuilder(querySqlString.trim());
			int fromIndex = querySqlLowered.indexOf(" from ");
			int fromCnt = 1;
			while(1==1){
				String tempStr = querySqlLowered.trim().substring(0,fromIndex+6);
				int selectCnt = 0;
				int selectIndex = 0;
				while(selectIndex>=0){
					selectIndex = tempStr.indexOf("select ", selectIndex+7);
					selectCnt++;
				}
				if(selectCnt>fromCnt){
					fromIndex = querySqlLowered.trim().indexOf(" from ",fromIndex+6);
					fromCnt++;
				}else{
					break;
				}
			}
			List<Integer> insertIndexList = new ArrayList<Integer>();
			int onIndex = querySqlLowered.indexOf(" on ",fromIndex);
			while(onIndex>0){
				insertIndexList.add(onIndex);
				onIndex = querySqlLowered.indexOf(" on ",onIndex+4);
			}
			int whereIndex = querySqlLowered.indexOf(" where ",fromIndex);
			int commaIndex = querySqlLowered.indexOf(",",fromIndex);
			while(commaIndex>0&&whereIndex>commaIndex){
				insertIndexList.add(commaIndex);
				commaIndex = querySqlLowered.indexOf(",",commaIndex+1);
			}
			List<String> joinKeyList = Arrays.asList(new String[]{" left "," inner "," join "});
			if(!insertIndexList.isEmpty()){
				Collections.sort(insertIndexList);
				int firstIndex = insertIndexList.get(0);
				if(querySqlLowered.substring(firstIndex).startsWith(" on ")){
					for(String joinKey:joinKeyList){
						if(querySqlLowered.substring(fromIndex,firstIndex).indexOf(joinKey)>0){
							insertIndexList.add(querySqlLowered.indexOf(joinKey,fromIndex));
							break;
						}
					}
				}
				Collections.sort(insertIndexList);
				int lastIndex = insertIndexList.get(insertIndexList.size()-1);
				if(querySqlLowered.substring(lastIndex).startsWith(",")){
					insertIndexList.add(whereIndex);
				}
			}
			Collections.sort(insertIndexList);
			String preKey = "";
			int preIndex = fromIndex;
			for(int k=0;k<insertIndexList.size();k++){
				int insertIndex = insertIndexList.get(k);
				String key = "";
				if(querySqlLowered.substring(insertIndex).startsWith(" on ")){
					key = "on";
				}else if(querySqlLowered.substring(insertIndex).startsWith(",")){
					key = ",";
				}
				if(!"".equals(key)&&!"".equals(preKey)&&!key.equals(preKey)){
					if("on".equals(key)){
						for(String joinKey:joinKeyList){
							if(querySqlLowered.substring(preIndex,insertIndex).indexOf(joinKey)>0){
								insertIndexList.add(k,querySqlLowered.indexOf(joinKey,preIndex));
								k++;
								break;
							}
						}
					}else if(",".equals(key)){
						insertIndexList.remove(k);
						k--;
					}
				}
				preIndex = insertIndexList.get(k);
				preKey = key;
			}
			if(insertIndexList.isEmpty()){
				if(whereIndex>0){
					insertIndexList.add(whereIndex);
				}else{
					int groupByIndex = querySqlLowered.indexOf(" group by ",fromIndex);
					if(groupByIndex>0){
						insertIndexList.add(groupByIndex);
					}else{
						int orderByIndex = querySqlLowered.indexOf(" order by ",fromIndex);
						if(orderByIndex>0){
							insertIndexList.add(orderByIndex);
						}else{
							insertIndexList.add(querySqlLowered.length());
						}
					}
				}
			}
			Collections.sort(insertIndexList);
			List<String> tempInsertIdxList = new ArrayList<String>();
			int i=0;
			String insertStr = " with(nolock)";
			int insertLen = insertStr.length();
			for(Integer index:insertIndexList){
				if(tempInsertIdxList.contains(String.valueOf(index))){
					continue;
				}
				tempInsertIdxList.add(String.valueOf(index));
				sb.insert(index+(insertLen*i), insertStr);
				i++;
			}
			querySqlString = sb.toString();
			querySqlLowered = querySqlString.trim().toLowerCase();
		}
		StringBuilder sb = new StringBuilder(querySqlString.trim());
		if (offset == 0) return super.getLimitString(querySqlString, offset, limit);
		int orderByIndex = querySqlLowered.toLowerCase().indexOf("order by");
		String orderby = orderByIndex > 0 ? querySqlString.substring(orderByIndex) : "ORDER BY CURRENT_TIMESTAMP";
		
		// Delete the order by clause at the end of the query
		if (orderByIndex > 0) sb.delete(orderByIndex, orderByIndex + orderby.length());
		
		// Find the end of the select statement
		int fromIndex = querySqlLowered.trim().indexOf(" from ");
		int fromCnt = 1;
		while(1==1){
			String tempStr = querySqlLowered.trim().substring(0,fromIndex+6);
			int selectCnt = 0;
			int selectIndex = 0;
			while(selectIndex>=0){
				selectIndex = tempStr.indexOf("select ", selectIndex+7);
				selectCnt++;
			}
			if(selectCnt>fromCnt){
				fromIndex = querySqlLowered.trim().indexOf(" from ",fromIndex+6);
				fromCnt++;
			}else{
				break;
			}
		}
		// Isert after the select statement the row_number() function:
		sb.insert(fromIndex, ",ROW_NUMBER() OVER (" + orderby + ") as __hibernate_row_nr__");
		
		//Wrap the query within a with statement:
		sb.insert(0, "WITH query AS (").append(") SELECT * FROM query ");
		sb.append("WHERE __hibernate_row_nr__ ");
		if (offset > 0) sb.append("BETWEEN ").append(offset+1).append(" AND ").append(limit);
		else sb.append(" <= ").append(limit);
		
		//I don't think that we really need this last order by clause
		//sb.append(" ORDER BY __hibernate_row_nr__");
		
		return sb.toString();
	}

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public boolean supportsLimitOffset() {
		return true;
	}
	
	@Override
	public boolean bindLimitParametersFirst() {
		return false;
	}

	@Override
	public boolean useMaxForLimit() {
		return true;
	}
}

