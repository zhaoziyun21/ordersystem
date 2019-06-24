package com.kssj.frame.se.command;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kssj.frame.util.CoreStringUtil;

/**
* @Description: object-oriented
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午05:01:17
* @version V1.0
*/
@SuppressWarnings("rawtypes")
public class SeQueryFilter{
	private int total	= 0; 		/** 总条目数量(不分页的总条目数) */
	private int page 	= 1; 		/** 当前页 */
	private int rows 	= 10;		/** 每页显示条目数量 */
	
	private String baseSE;			/** 基础SE语句 */
	
	private String split = "|";		/** 分隔符 */
	
	private Map<String,SeConditionSpeller> conditionMap = new HashMap<String,SeConditionSpeller>();/** 查询条件 */
	private Map<String,SeOrderSpeller> orderMap = new HashMap<String,SeOrderSpeller>();			 /** 排序字段 */
	
	HttpServletRequest request;		/** request对象 */
	
	/**〇
	 * construstor：joint filter
	 * 
	 * @param request
	 * @param dbType
	 * @throws Exception
	 */
	public SeQueryFilter(HttpServletRequest request) throws Exception{
		this.request=request;
		Enumeration paramEnu= request.getParameterNames();
		
		while(paramEnu.hasMoreElements()){
			String paramName = (String)paramEnu.nextElement();
			if(paramName.startsWith("Q|")){
    			String paramValue = request.getParameter(paramName);
    			
    			if(CoreStringUtil.isNotEmpty(paramValue) && !paramValue.equals("全站资源")){
	    			String[] vs = paramName.split("[" + split + "]");
	    			SeConditionSpeller speller = new SeConditionSpeller(vs[1],SeSpellerOperatorType.getType(vs[3]),paramValue,SeSpellerDataType.getType(vs[2]),SeSpellerQueryType.getType(vs[4]));
	    			
	    			conditionMap.put(vs[1],speller);
    			}
    		}
		}
		
		String sort = request.getParameter("sort");//is property name
		if(CoreStringUtil.isNotEmpty(sort)){
			String order = request.getParameter("order");
			orderMap.put(order,new SeOrderSpeller(sort,order));
		}
		
		String pageStr = request.getParameter("page") != null ? request.getParameter("page") : "0";
		String rowsStr = request.getParameter("rows") != null ? request.getParameter("rows") : "0";
		
		page = Integer.parseInt(pageStr);
		rows = Integer.parseInt(rowsStr);
	}
	
	/**
	 * ①：spell whole sql statement
	 * 
	 * @return whole sql
	 */
	public String toSql(){
		StringBuffer str = new StringBuffer();
		
		String condition = this.toCondition();
		@SuppressWarnings("unused")
		String order = this.toOrderBy();
		
		if(!condition.trim().equals("")){
			str.append(condition.substring(5));
		}
//		if(!order.trim().equals("")){
//			str.append(" order by " + order);
//		}
		
		return str.toString();
	}
	
	/**
	 * ②： spell the conditional statement of the afert "...where 1=1 "
	 * 
	 * @return where 条件语句
	 */
	public String toCondition(){
		StringBuffer str = new StringBuffer();
		Iterator<String> iter = conditionMap.keySet().iterator();
		while(iter.hasNext()){
			try{
				SeConditionSpeller speller = conditionMap.get(iter.next());
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
				SeOrderSpeller speller = orderMap.get(iter.next());
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
	public void addCondition(SeConditionSpeller spell) throws Exception{
		this.getConditionMap().put(spell.getProperty(), spell);
	}
	/**#########   ⑤ Manual   #########
	 *  Fill in spelling item of the order (It is mainly used for "manual")
	 * 	
	 * @param spell
	 * @throws Exception
	 */
	public void addOrder(SeOrderSpeller spell) throws Exception{
		this.getOrderMap().put(spell.getProperty(), spell);
	}
	
	
	
	
	

	public String getBaseSE() {
		return baseSE;
	}

	public void setBaseSE(String baseSE) {
		this.baseSE = baseSE;
	}

	public String getSplit() {
		return split;
	}
	
	public void setSplit(String split) {
		this.split = split;
	}
	
	/**
	 * 获取sql条件拼写器集合
	 * 
	 * 一般用于手动设置sql条件的情况
	 * @return sql条件拼写器集合
	 */
	public Map<String, SeConditionSpeller> getConditionMap() {
		return conditionMap;
	}
	/**
	 * 设置sql条件拼写器集合
	 * @param conditionMap
	 */
	public void setConditionMap(Map<String, SeConditionSpeller> conditionMap) {
		this.conditionMap = conditionMap;
	}
	
	/**
	 * 获取sql排序拼写器集合
	 * 一般用于手动设置sql排序的情况
	 * @return sql排序拼写器集合
	 */
	public Map<String, SeOrderSpeller> getOrderMap() {
		return orderMap;
	}
	/**
	 * 设置sql排序拼写器集合
	 * @param orderMap
	 */
	public void setOrderMap(Map<String, SeOrderSpeller> orderMap) {
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
