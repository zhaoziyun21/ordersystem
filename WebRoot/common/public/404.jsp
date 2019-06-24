<%@page import="com.dcmp.base.util.RequestUtil" pageEncoding="UTF-8"%>
<%@ include file="TagLib.jsp"%>
<%!private final static transient org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog("404_jsp");%>
<%
	String errorUrl = RequestUtil.getErrorUrl(request);
	//boolean isContent = (errorUrl.endsWith(".html") || errorUrl.endsWith(".jsp"));
	logger.warn("Requested url not found: " + errorUrl+" Referrer: "+request.getHeader("REFERER"));
	response.addHeader("__404_error","true");
%>
<html>
	<head>
		<title>找不到该页面</title>
		<link href="<%=request.getContextPath()%>/css/pub.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
	    <div id="error">
           <div class="error">
                <p>
                                                      找不到该页面<br/><br/>
		            url:<%=errorUrl %>
                </p>
           </div>
      </div>
		
	</body>
	
</html>