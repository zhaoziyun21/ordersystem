package com.kssj.frame.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;
import com.kssj.auth.model.XUser;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.se.command.SeQueryFilter;
import com.kssj.frame.web.paging.PagingBean;

/**
* @Description: web层使用对象查询
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-21 上午10:46:49
* @version V1.0
*/
@SuppressWarnings("rawtypes")
public class BaseAction  extends ActionSupport{
	protected transient final Logger logger = Logger.getLogger(this.getClass());
	
	private static final long serialVersionUID = 1L;
	
	public static final String SUCCESS = "success";
	public static final String INPUT = "input";
	protected static final String AJAX = "ajax";
	protected static final String MSG = "msg";

	private SqlQueryFilter qf;
	private SeQueryFilter seQueryFilter;
	
	//成功跳转的页面(jsp)
	private String forwardPage = "/jsonString.jsp";

	public static final String JSON_SUCCESS = "{success:true}";

	/**
	 * 结合Ext的分页功能： dir DESC limit 25 sort id start 50
	 */
	/**
	 * 当前是升序还是降序排数据
	 */
	protected String dir;
	/**
	 * 排序的字段
	 */
	protected String sort;
	/**
	 * 每页的大小
	 */
	protected Integer limit = 10;
	/**
	 * 开始取数据的索引号
	 */
	protected Integer start = 0;

	protected String jsonString = JSON_SUCCESS;
	
	
	private String url;
	
	private String msg;

	public final String CANCEL = "cancel";

	public final String VIEW = "view";

	
	
	public BaseAction() {
	}
	
	// Struts2 Action 异常 设置fowward
	public String err(Exception e) {
		forwardPage = "err";
		getRequest().setAttribute("err", e.getMessage());
		return forwardPage;
	}

	/**
	 * Convenience method to get the request
	 * 
	 * @return current request
	 */
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * Convenience method to get the response
	 * 
	 * @return current response
	 */
	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * Convenience method to get the session. This will create a session if one
	 * doesn't exist.
	 * 
	 * @return the session from the request (request.getSession()).
	 */
	protected HttpSession getSession() {
		return getRequest().getSession();
	}

	// ---------------------------Methods------------------------------

	protected PagingBean getInitPagingBean() {
		PagingBean pb = new PagingBean(start, limit);
		return pb;
	}

	public String list() throws Exception {
		return SUCCESS;
	}

	public String edit() throws Exception {
		return INPUT;
	}

	public String save() throws Exception {
		return INPUT;
	}

	public String delete() throws Exception {
		return SUCCESS;
	}

	public String multiDelete() {
		return SUCCESS;
	}

	public String multiSave() {
		return SUCCESS;
	}

	

	/**
	 * 按url返回其默认对应的jsp
	 * 
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception {
		return SUCCESS;

	}
	/**
	 * gson list的列表
	 * @param listData
	 * @param totalItems 
	 * @param onlyIncludeExpose 仅是格式化包括@Expose标签的字段
	 * @return
	 */
	public String gsonFormat(List listData,int totalItems,boolean onlyIncludeExpose){
		StringBuffer buff = new StringBuffer("{success:true,'totalCounts':")
		.append(totalItems).append(",result:");
		
		Gson gson=null;
		if(onlyIncludeExpose){
			gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		}else{
			gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		}
		buff.append(gson.toJson(listData));
		
		buff.append("}");
		
		return buff.toString();
	}
	public String gsonFormat(List listData,int totalItems){
		return gsonFormat(listData,totalItems,false);
	}

	/**
	 * 传递导出所需参数
	 */
	public void setExportParameter(List list) {
		getRequest().setAttribute("colId", getRequest().getParameter("colId"));
		getRequest().setAttribute("colName",
				getRequest().getParameter("colName"));
		getRequest().setAttribute("exportType",
				getRequest().getParameter("exportType"));
		getRequest().setAttribute("isExport",
				getRequest().getParameter("isExport"));
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String datetime = tempDate.format(new java.util.Date());
		getRequest().setAttribute("fileName", datetime);
		getRequest().setAttribute("__exportList", list);
	}

//*********************************新增************************************
	
	protected static final String LIST = "list";
	protected static final String STATUS = "status";
	protected static final String WARN = "warn";
	protected static final String ERROR = "error";
	protected static final String MESSAGE = "message";
	protected static final String CONTENT = "content";
	
	// 设置页面不缓存
	public void setResponseNoCache() {
		getResponse().setHeader("progma", "no-cache");
		getResponse().setHeader("Cache-Control", "no-cache");
		getResponse().setHeader("Cache-Control", "no-store");
		getResponse().setDateHeader("Expires", 0);
	}
	// AJAX输出
	public String ajax(String content, String type) {
		try {
			getResponse().setContentType(type + ";charset=UTF-8");
			getResponse().setHeader("Pragma", "No-cache");
			getResponse().setHeader("Cache-Control", "no-cache");
			getResponse().setDateHeader("Expires", 0);
			getResponse().getWriter().write(content);
			getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	// AJAX输出文本
	public String ajaxText(String text) {
		return ajax(text, "text/plain");
	}

	// AJAX输出HTML
	public String ajaxHtml(String html) {
		return ajax(html, "text/html");
	}

	// AJAX输出XML
	public String ajaxXml(String xml) {
		return ajax(xml, "text/xml");
	}

	// 根据字符串输出JSON
	public String ajaxJson(String jsonString) {
		return ajax(jsonString, "text/html");
	}
	
	// 根据对象输出JSON
	public String ajaxJson(Object object) {
		JSONArray jsonArray=JSONArray.fromObject(object);
		return ajax(jsonArray.toString(), "text/html");
	}
	
	// 输出JSON警告消息
	public String ajaxJsonWarnMessage(String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, WARN);
		jsonMap.put(MESSAGE, message);
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	// 输出JSON成功消息
	public String ajaxJsonSuccessMessage(String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, SUCCESS);
		jsonMap.put(MESSAGE, message);
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		return ajax(jsonObject.toString(), "text/html");
	}
	// 输出JSON成功消息
	public String ajaxJsonSuccessMessage(Map<String, String> jsonMap,String message) {
		jsonMap.put(STATUS, SUCCESS);
		jsonMap.put(MESSAGE, message);
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		return ajax(jsonObject.toString(), "text/html");
	}
	// 输出JSON错误消息
	public String ajaxJsonErrorMessage(String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, ERROR);
		jsonMap.put(MESSAGE, message);
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		return ajax(jsonObject.toString(), "text/html");
	}

	//*********************************Ajax************************************
	
	/**
	* @method: ajaxJsonOutMap
	* @Description: 根据Map输出json
	*
	* @param jsonMap
	* @return void
	*
	* @author ChenYW
	* @date 2013-7-15 下午03:06:23
	*/
	public void ajaxJsonOutMap(Map jsonMap){
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		ajaxOutMap(jsonObject, "text/html");
	}
	
	/**
	* @method: ajaxJsonOutList
	* @Description: 根据List输出json
	*
	* @param jsonList
	* @return void
	*
	* @author ChenYW
	* @date 2013-7-15 下午03:06:40
	*/
	public void ajaxJsonOutList(List jsonList){
		JSONArray jsonArray = JSONArray.fromObject(jsonList);
		ajaxOutList(jsonArray, "text/html");
	}
	
	// AJAX输出
	public void ajaxOutMap(JSONObject content, String type) {
		try {
			getResponse().setContentType(type + ";charset=UTF-8");
			getResponse().setHeader("Pragma", "No-cache");
			getResponse().setHeader("Cache-Control", "no-cache");
			getResponse().setDateHeader("Expires", 0);
			getResponse().getWriter().print(content);
			getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// AJAX输出
	public void ajaxOutList(JSONArray content, String type) {
		try {
			getResponse().setContentType(type + ";charset=UTF-8");
			getResponse().setHeader("Pragma", "No-cache");
			getResponse().setHeader("Cache-Control", "no-cache");
			getResponse().setDateHeader("Expires", 0);
			getResponse().getWriter().print(content);
			getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	* @Title: jsonToClass
	* @Description: json转List<T>
	* 
	* @author liyy
	* @param jsonList 	json字段需要与对象字段一致
	* @param obj		new一个对象
	* @return
	*/
	public List jsonToClassList(String jsonList,Object obj){
		List<Object> ls = new ArrayList<Object>();
		JSONArray jsonArray = JSONArray.fromObject(jsonList);  
        JSONArray new_jsonArray=JSONArray.fromObject(jsonArray.toArray());  
        Collection java_collection=JSONArray.toCollection(new_jsonArray);  
        if(java_collection!=null && !java_collection.isEmpty())  
        {  
            Iterator it=java_collection.iterator();  
            while(it.hasNext())  
            {  
                JSONObject jsonObj=JSONObject.fromObject(it.next());  
                JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"}));
                Object t=JSONObject.toBean(jsonObj,obj.getClass());  
                ls.add(t);
            }  
        }
        return ls;
	}
	//********************************通用方法*****************************
	protected XUser getOrderUserBySession() {
		HttpSession session=  getRequest().getSession();
		 if(session!=null){
			 return (XUser)session.getAttribute("orderLoginUser");
		 }else{
			 return null;
		 }
	}
	protected XUser getWxOrderUserBySession() {
		 HttpSession session=  getRequest().getSession();
		 if(session!=null){
			 return (XUser)session.getAttribute("wxOrderLoginUser");
		 }else{
			 return null;
		 }
	}
	protected XUser getSysUserBySession() {
		HttpSession session=  getRequest().getSession();
		 if(session!=null){
			 return (XUser)session.getAttribute("loginUser");
		 }else{
			 return null;
		 }
	}
	//********************************get：set*****************************
	
	public Logger getLogger() {
		return logger;
	}

	public SqlQueryFilter getQf() {
		return qf;
	}

	public void setQf(SqlQueryFilter qf) {
		this.qf = qf;
	}
	
	public SeQueryFilter getSeQueryFilter() {
		return seQueryFilter;
	}

	public void setSeQueryFilter(SeQueryFilter seQueryFilter) {
		this.seQueryFilter = seQueryFilter;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}
	public String getForwardPage() {
		return forwardPage;
	}

	public void setForwardPage(String forwardPage) {
		this.forwardPage = forwardPage;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getJsonString() {
		return jsonString;
	}
	
}
