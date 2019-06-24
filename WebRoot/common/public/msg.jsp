<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="TagLib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<c:if test="${modaiDialog == 'true'}">
    	<base target="_self"/>
    </c:if>
    <title>提示</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	
	<script type="text/javascript">
		
	</script>
  </head>
  
  <body>
  	<c:if test="${scriptContent != null && scriptContent != ''}">
			${scriptContent}
		</c:if>
		
		<c:if test="${msg != null && msg != ''}">
			alert("${msg}");
		</c:if>
		<c:if test="${url != null && url != ''}">
			document.location.href="${url}";
		</c:if>
		
		<c:if test="${returnValue != null && returnValue != ''}">
			window.returnValue = "${returnValue}";
			window.close();
		</c:if>
		<c:if test="${msgByMe != null && msgByMe != ''}">
			alert("${msgByMe}");
			window.parent.parent.parent.cw();
		</c:if>
		<c:if test="${msgBySend != null && msgBySend != ''}">
			alert("${msgBySend}");
			window.parent.parent.reloadData();
			window.parent.parent.closeWin();
		</c:if>
  </body>
</html>
