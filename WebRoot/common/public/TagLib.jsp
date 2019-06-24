<%--<%@ page trimDirectiveWhitespaces="true" %>--%>
<%
if (!request.getServletPath().startsWith("/DCMP_JDP_YZB")){
	response.setHeader("Pragma","No-cache"); 
	response.setHeader("Cache-Control","no-cache"); 
	response.setDateHeader("Expires", -1); 
}
%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>  
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
/* 
 * request.getSchema()：返回当前页面使用的协议，http或是https
 * request.getServerName()：返回当前页面所在的服务器的名字
 * request.getServerPort()：返回当前页面所在的服务器使用的端口
 * request.getContextPath()：返回当前页面所在的应用的名字
 */
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String rootPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
request.setAttribute("basePath",basePath);
request.setAttribute("rootPath",rootPath);
%>
<c:set var="root" value="<%=basePath %>"/>
<c:set var="rootPath" value="<%=rootPath %>"/>
