package com.kssj.frame.command.sql;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kssj.frame.util.CoreStringUtil;

/**
* @Description: To create sql
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-2 下午09:11:20
* @version V1.0
*/
public class SqlQueryFilter {
	
	private int total	= 0; 		/** 总条目数量(不分页的总条目数) */
	private int page 	= 1; 		/** 当前页 */
	private int rows 	= 10;		/** 每页显示条目数量 */
	
	private SqlSpellerDbType dbType;/** 数据库类型 */
	private String baseSql;			/** 基础sql语句 */
	
	private String split = "|";		/** 分隔符 */
	
	private Map<String,SqlConditionSpeller> conditionMap = new HashMap<String,SqlConditionSpeller>();/** 查询条件 */
	private Map<String,SqlOrderSpeller> orderMap = new HashMap<String,SqlOrderSpeller>();			 /** 排序字段 */
	
	HttpServletRequest request;		/** request对象 */
	
	public SqlQueryFilter(SqlSpellerDbType dbType) throws Exception{
		this.dbType = dbType;
	}
	
	/**〇
	 * construstor：joint filter
	 * 
	 * @param request
	 * @param dbType
	 * @throws Exception
	 */
	public SqlQueryFilter(HttpServletRequest request,SqlSpellerDbType dbType) throws Exception{
		this.dbType = dbType;
		
		this.request=request;
		@SuppressWarnings("rawtypes")
		Enumeration paramEnu= request.getParameterNames();
		
		while(paramEnu.hasMoreElements()){
			String paramName = (String)paramEnu.nextElement();
			if(paramName.startsWith("Q|")){
    			String paramValue = request.getParameter(paramName);
    			
    			if(CoreStringUtil.isNotEmpty(paramValue)){
	    			String[] vs = paramName.split("[" + split + "]");
	    			SqlConditionSpeller speller = null;
	    			if(dbType.equals(SqlSpellerDbType.SQLSERVER)){
	    				speller = new SqlConditionSpellerForSqlServer(vs[1],vs[3],paramValue,vs[2]);
	    			}else if(dbType.equals(SqlSpellerDbType.ORACLE)){
	    				speller = new SqlConditionSpellerForOracle(vs[1],vs[3],paramValue,vs[2]);
	    			}else if(dbType.equals(SqlSpellerDbType.MYSQL)){
	    				speller = new SqlConditionSpellerForMySql(vs[1],vs[3],paramValue,vs[2]);
	    			}
	    			conditionMap.put(vs[1],speller);
    			}
    		}
		}
		
		String sort = request.getParameter("sortname");//is property name
		if(CoreStringUtil.isNotEmpty(sort)){
			String order = request.getParameter("sortorder");
			orderMap.put(order,new SqlOrderSpeller(sort,order));
		}
		
		String pageStr = request.getParameter("page") != null ? request.getParameter("page") : "0";
		String rowsStr = request.getParameter("pageSize") != null ? request.getParameter("pageSize") : "0";
		
		page = Integer.parseInt(pageStr);
		rows = Integer.parseInt(rowsStr);
	}
	
	/**
	 * ①：spell whole sql statement
	 * 
	 * @return whole sql
	 */
	public String toSql(){
		StringBuffer str = new StringBuffer(this.baseSql);
		
		String condition = this.toCondition();
		String order = this.toOrderBy();
		
		if(!condition.trim().equals("")){
			str.append(condition);
		}
		if(!order.trim().equals("")){
			str.append(" order by " + order);
		}
		
		return str.toString();
	}
	
	/**
	 * ②： spell the conditional statement of the afert "...where 1=1 "
	 * 
	 * @return where 条件语句
	 */
	public String toCondition(){
		StringBuffer str = new StringBuffer("");
		Iterator<String> iter = conditionMap.keySet().iterator();
		while(iter.hasNext()){
			try{
				SqlConditionSpeller speller = conditionMap.get(iter.next());
				str.append(" and ");
				str.append(speller.spell());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return str.toString();
	}
	
	/**
	 * ③： Spell the "order by" statement 
	 * @return order by 语句
	 */
	public String toOrderBy(){
		StringBuffer str = new StringBuffer("");
		Iterator<String> iter = orderMap.keySet().iterator();
		while(iter.hasNext()){
			try{
				SqlOrderSpeller speller = orderMap.get(iter.next());
				str.append(speller.spell());
				str.append(",");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(CoreStringUtil.isNotEmpty(str.toString())){
			return str.toString().substring(0,str.toString().length() - 1);
		}else{
			return str.toString();
		}
	}
	
	/**#########  ④ Manual    #########
	 * Fill in spelling itme of the conditional (It is mainly used for "manual")
	 * @param spell
	 * @throws Exception
	 */
	public void addCondition(SqlConditionSpeller spell) throws Exception{
		this.getConditionMap().put(spell.getProperty(), spell);
	}
	/**#########   ⑤ Manual   #########
	 *  Fill in spelling item of the order (It is mainly used for "manual")
	 * 	
	 * @param spell
	 * @throws Exception
	 */
	public void addOrder(SqlOrderSpeller spell) throws Exception{
		this.getOrderMap().put(spell.getProperty(), spell);
	}
	
	public String getSplit() {
		return split;
	}
	
	public void setSplit(String split) {
		this.split = split;
	}
	/**
	 *  Get the base sql statement
	 *   
	 * Example：select * from user where 1=1 and userName like '%a%' 中 and之前的sql语句
	 * @return 基础sql语句  
	 */
	public String getBaseSql() {
		return baseSql;
	}
	/**
	 * Set the base sql statement
	 * 
	 * Example：select * from user where 1=1 and userName like '%a%' 中 and之前的sql语句
	 * @param baseSql 基础sql语句
	 */
	public void setBaseSql(String baseSql) {
		this.baseSql = baseSql;
	}
	
	public SqlSpellerDbType getDbType() {
		return dbType;
	}
	
	public void setDbType(SqlSpellerDbType dbType) {
		this.dbType = dbType;
	}
	/**
	 * 获取sql条件拼写器集合
	 * 
	 * 一般用于手动设置sql条件的情况
	 * @return sql条件拼写器集合
	 */
	public Map<String, SqlConditionSpeller> getConditionMap() {
		return conditionMap;
	}
	/**
	 * 设置sql条件拼写器集合
	 * @param conditionMap
	 */
	public void setConditionMap(Map<String, SqlConditionSpeller> conditionMap) {
		this.conditionMap = conditionMap;
	}
	
	/**
	 * 获取sql排序拼写器集合
	 * 一般用于手动设置sql排序的情况
	 * @return sql排序拼写器集合
	 */
	public Map<String, SqlOrderSpeller> getOrderMap() {
		return orderMap;
	}
	/**
	 * 设置sql排序拼写器集合
	 * @param orderMap
	 */
	public void setOrderMap(Map<String, SqlOrderSpeller> orderMap) {
		this.orderMap = orderMap;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getRows() {
		return rows;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
}
