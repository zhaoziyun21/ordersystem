<%//description:用于显示formPanel提交的出错信息，方便调试 %>
 

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="TagLib.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>错误页面</title>
	<link href="${root}/css/pub.css" rel="stylesheet" type="text/css" />
	
<script type="text/javascript">
	var alltime=10;
	function setTime(){
	  if (alltime<=0){
		  top.location.href="${root}/indexBaseLogin.do";
	      //window.history.back();
	   }else{
	     alltime --;
	     document.getElementById('time').innerHTML=alltime;
	   }   
	}
	  //setInterval("setTime()",1000)   
	  
</script>
  </head>
  <body>
      <div id="error">
           <div class="error">
                <p>
					出现了错误，请联系系统管理员!<br/>
					                        <br/>
					错误信息：<br/>											
					 <span style="font-size:16px">${Exception.message }</span><br/><br/>
<%--					 <span style="font-size:16px">单击【返回上一页】,尝试继续操作，否则<label style="font-size:16px" id="time">10</label> 秒后自动返回</span><br/><br/>--%>
<%--					 <span style="font-size:16px">单击【返回登录页】,直接转到登陆页</span>--%>
					<br/>
					<br/>
					<center>
						<a style="cursor:pointer;font-size:16px;color:blue;padding:3px 0 3px 10px;text-decoration:underline;" onclick="javascript:window.history.back();">返回上一页</a>
<%--						<a style="cursor:pointer;font-size:16px;color:blue;padding:3px 0 3px 10px;text-decoration:underline;" onclick="javascript:top.location.href='${root}/indexBaseLogin.do'">返回登录页</a>--%>
					</center>
              </p>
           </div>
      </div>
	</body>
</html>
