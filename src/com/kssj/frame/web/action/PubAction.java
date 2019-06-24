package com.kssj.frame.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.kssj.frame.command.sql.SqlQueryFilter;
import com.kssj.frame.web.paging.Page;

@SuppressWarnings({"serial","rawtypes"})
public class PubAction extends ActionSupport implements RequestAware, SessionAware {
	
	/**
	 * json字符串，默认为
	 */
	protected String jsonString="{success:true}";
	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getJsonString() {
		return jsonString;
	}
	
	//sql过滤器,负责生成sql语句
	private SqlQueryFilter qf;
	
	
	/**Struts2 封装 HttpServletRequest */
	Map<String,Object> request;
	
	/**Struts2 封装  HttpSession */
	Map<String,Object> session;
	
	/**日志 */
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**分页信息 */
	public Page p = new Page();
	
	/**Struts2 Action 返回的字符串 */
	public String forward;
	
	/**Struts2 跳转页面路径 */
//	public String forwardPage="/jsonString.jsp";
	public String forwardPage;
	
	/**显示信息页 是否是modaiDialog */
	private String modaiDialog;
	
	/**信息页执行脚本的内容 */
	private String scriptContent;
	
	/**信息页返回的数据 */
	private String returnValue;
	
	/**信息页显示的信息 */
	private String msg;
	
	/**信息也跳转地址 */
	private String url;
	
	/**Struts2 Action 异常 设置fowward */
	public String err(Exception e){
		forward = "err";
		request.put("err",e.getMessage());
		return forward;
	}
	
	public String getForwardPage() {
		return forwardPage;
	}

	public void setForwardPage(String forwardPage) {
		this.forwardPage = forwardPage;
	}
	
	public String getModaiDialog() {
		return modaiDialog;
	}

	public void setModaiDialog(String modaiDialog) {
		this.modaiDialog = modaiDialog;
	}

	public String getScriptContent() {
		return scriptContent;
	}

	public void setScriptContent(String scriptContent) {
		this.scriptContent = scriptContent;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	//依赖注入HttpServletRequest
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}
	
	//依赖注入HttpSession
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public SqlQueryFilter getQf() {
		return qf;
	}

	public void setQf(SqlQueryFilter qf) {
		this.qf = qf;
	}

	
	public Map<String, Object> getRequest() {
		return request;
	}
	
	public Map<String, Object> getSession() {
		return session;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Page getP() {
		return p;
	}

	public void setP(Page p) {
		this.p = p;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public Logger getLogger() {
		return logger;
	}
	
	public HttpServletRequest getReq() {
		return ServletActionContext.getRequest();
	}
	
	public HttpSession getSess() {
		return getReq().getSession();
	}
//*********************************新增************************************
	
	protected static final String LIST = "list";
	protected static final String STATUS = "status";
	protected static final String WARN = "warn";
	protected static final String ERROR = "error";
	protected static final String MESSAGE = "message";
	protected static final String CONTENT = "content";
	protected static final String AJAX = "ajax";
	protected static final String MSG = "msg";

	/**
	 * Convenience method to get the response
	 * 
	 * @return current response
	 */
	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	
	// 设置页面不缓存
	public void setResponseNoCache() {
		getResponse().setHeader("progma", "no-cache");
		getResponse().setHeader("Cache-Control", "no-cache");
		getResponse().setHeader("Cache-Control", "no-store");
		getResponse().setDateHeader("Expires", 0);
	}
	// AJAX输出
	public void ajax(String content, String type) {
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
	}

	// AJAX输出文本
	public void ajaxText(String text) {
		ajax(text, "text/plain");
	}

	// AJAX输出HTML
	public void ajaxHtml(String html) {
		ajax(html, "text/html");
	}

	// AJAX输出XML
	public void ajaxXml(String xml) {
		ajax(xml, "text/xml");
	}

	// 根据字符串输出JSON
	public void ajaxJson(String jsonString) {
		ajax(jsonString, "text/html");
	}
	
	// 根据对象输出JSON
	public void ajaxJson(Object object) {
		JSONArray jsonArray=JSONArray.fromObject(object);
		ajax(jsonArray.toString(), "text/html");
	}
	
	// 输出JSON警告消息
	public void ajaxJsonWarnMessage(String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, WARN);
		jsonMap.put(MESSAGE, message);
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		ajax(jsonObject.toString(), "text/html");
	}
	
	// 输出JSON成功消息
	public void ajaxJsonSuccessMessage(String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, SUCCESS);
		jsonMap.put(MESSAGE, message);
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		ajax(jsonObject.toString(), "text/html");
	}
	
	// 输出JSON错误消息
	public void ajaxJsonErrorMessage(String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, ERROR);
		jsonMap.put(MESSAGE, message);
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		ajax(jsonObject.toString(), "text/html");
	}
	
//*********************************NEW BY ChenYW************************************
	
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
}
