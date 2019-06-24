package com.kssj.frame.command.oo;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.kssj.frame.web.pagination.Page;
import com.kssj.frame.web.paging.PagingBean;


/**
* @Description: object-oriented
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午05:01:17
* @version V1.0
*/
@SuppressWarnings({"unchecked","rawtypes"})
public class QueryFilter{
	public final static Logger logger=Logger.getLogger(QueryFilter.class);
	
	private HttpServletRequest request=null;
	
	/**  is/no exp  */
	private boolean isExport=false;
	/** sort */
	public static final String ORDER_DESC="desc";
	public static final String ORDER_ASC="asc";
	/** DAO层的querys Map中filter的key,表示用哪一个作为查询的条件 */
	private String filterName=null;
	/**  */
	private List<Object> paramValues=new ArrayList();
	/** standard-Object */
	private List<CriteriaCommand> commands = new ArrayList<CriteriaCommand>();
	/** alias（TO prevent alias） */
	private Set<String> aliasSet=new HashSet<String>();
	/** page bean */
	private PagingBean pagingBean=null;

	
    public QueryFilter() {

    }

	/**
     * ①：从请求对象获取查询参数,并进行构造  
     * 
     * @param request
     */
	public QueryFilter(HttpServletRequest request){
    	this.request=request;
    	Enumeration paramEnu= request.getParameterNames();
    	
    	while(paramEnu.hasMoreElements()){
    		String paramName=(String)paramEnu.nextElement();
    		if(paramName.startsWith("Q|")){
    			String paramValue=(String)request.getParameter(paramName);
    			addFilter(paramName,paramValue);
    		}
    	}
    	//取得分页的信息
    	Integer start=0;//start record
    	Integer limit=PagingBean.DEFAULT_PAGE_SIZE;
    	
//    	String s_start=request.getParameter("start");
//    	String s_limit=request.getParameter("limit");
    	String s_start=request.getParameter("page");//start page
    	String s_limit=request.getParameter("rows");//the number of each page
    	String s_pageSize=request.getParameter("pageSize");
    	
    	if(StringUtils.isNotEmpty(s_pageSize)){
    		limit=new Integer(s_pageSize);
    	}
    	if(StringUtils.isNotEmpty(s_start)){
    		start=(new Integer(s_start)-1)*limit;
    	}
    	
    	String sort=request.getParameter("sort");//order-property
    	String order=request.getParameter("order");//order-type：desc/asc
    	
    	if(StringUtils.isNotEmpty(sort)&&StringUtils.isNotEmpty(order)){
    		addSorted(sort, order);
    	}
    	
    	//report Export
    	if("true".equals(request.getParameter("isExport"))){
    		isExport=true;
    		request.setAttribute("colId", request.getParameter("colId"));
			request.setAttribute("colName", request.getParameter("colName"));
			request.setAttribute("exportType", request.getParameter("exportType"));
    	}
		request.setAttribute("isExport", isExport);
    	this.pagingBean=new PagingBean(start, limit);
    }
    
    /**
     * ②：添加过滤的查询条件
     * 
     * @param paramName 过滤的查询参数名称
     * 		过滤的查询参数名称格式必须为: 
     * 			Q|firstName|S|EQ
     * 			其中：①Q_表示该参数为查询的参数，
     * 				    ②firstName查询的字段名称，
     * 				    ③S代表该参数的类型为字符串类型,该位置的其他值有：
     * 					S	=字符串
     * 					BD	=BigDecimal，
     * 					FT	=float,
     * 					N	=Integer,
     * 					SN	=Short,
     * 					L	=Long,
     * 					D	=日期，
     * 					DL	=日期类型，并且把其时分秒设置为最小，即为0:0:0
     *					DG	=日期类型，并且把其时分秒设置为最大，即为23:59:59
     * 				    ④EQ代表“等于”，该位置的其他值有：
     * 							LT，GT，EQ，LE，GE, NEQ,  LK,   EMP,   NOTEMP, NULL, NOTNULL
     * 					要别代表    <,  >,  =, <=, >=, <>, like, empty,not empty, null, not null的条件查询
     * 
     * @param paramValue 过滤的查询参数值
     */
    public void addFilter(String paramName,String paramValue){  
    	
    	String []fieldInfo=paramName.split("[|]");
    	
    	Object value=null;
		if(fieldInfo!=null&&fieldInfo.length==4){
			
			//value convert
			value=ParamUtil.convertObject(fieldInfo[2], paramValue);
			
			if(value!=null){
    			FieldCommandImpl fieldCommand=new FieldCommandImpl(fieldInfo[1],value,fieldInfo[3],this);
    			commands.add(fieldCommand);
			}
		}else if(fieldInfo!=null&&fieldInfo.length==3){
			FieldCommandImpl fieldCommand=new FieldCommandImpl(fieldInfo[1],value,fieldInfo[2],this);
			commands.add(fieldCommand);
		}else{
			logger.error("Query param name ["+paramName+"] is not right format." );
		}
		
    }
    public void addParamValue(Object value){
    	paramValues.add(value);
    }
    
    public List getParamValueList(){
    	return paramValues;
    }
    
    public void addSorted(String orderBy,String ascDesc){
    	commands.add(new SortCommandImpl(orderBy,ascDesc,this));
    }
    
    public void addExample(Object object){
    	commands.add(new ExampleCommandImpl(object));
    }

  //*********************************************get : set********************************************
    
    public PagingBean getPagingBean() {
		return pagingBean;
	}
    
	public List<CriteriaCommand> getCommands() {
		return commands;
	}

	public Set<String> getAliasSet() {
		return aliasSet;
	}
	
	public boolean isExport() {
		return isExport;
	}

	public void setExport(boolean isExport) {
		this.isExport = isExport;
	}

	public HttpServletRequest getRequest() {
		return request;
	}
	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	

}
