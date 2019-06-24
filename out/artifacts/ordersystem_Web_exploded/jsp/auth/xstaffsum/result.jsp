<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <base href="<%=basePath%>">
    
    <title>上传结果</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript">
	function goback(){
		window.location.href= "${root}staffSum/goExportStaffExcelXStaffSum.do";
	}
	</script>
  </head>
  
  <body style="padding-left: 30px;padding-top: 100px; background:#edf5fa;">
<!--   	<h3 style="font-size: 18px; text-align: center; color:#2a2a2a; font-family: 黑体;"><img style="margin-top:150px;" src="${root}/images/sahngchuan.png "/>上传失败,请重新加载</h3> -->
  	<c:if test="${uploadMsp.isFail=='true'}">
  	<p style="margin-left:30px; margin-top:20px; font-size: 18px; text-align: center; color:#2a2a2a; font-family: 黑体;">${uploadMsp.count}数据,${uploadMsp.success}<br/>错误数据:<span id="msgSpan" style="margin-left:5px;">${uploadMsp.failureMsgList}</span></p><br/>
  	</c:if>
  		<c:if test="${uploadMsp.isFail=='false'}">
  	<p style="margin-left:30px; margin-top:20px; font-size: 18px; text-align: center; color:#2a2a2a; font-family: 黑体;"><h2>上传成功   100%已经上传</h2><br/><span id="msgSpan" style="margin-left:5px;">总共${uploadMsp.count}条数据,${uploadMsp.success}</span></p><br/>
  	</c:if>
  	  <div class=" clear"></div>
 	  <div class="botton_box ">
 	     <input class="button_green" id="finishid" type="button" value="返回" onclick="goback();"/> 
       </div>
  </body>
</html>
