<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="TagLib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>${SessionTimeOver }</title>
	<link href="${root}/css/pub.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript">
	    var alltime=5;
	    function setTime(){
	      if (alltime<=0){
	    	  top.location.href="${root}/login.jsp";
	    	  //window.history.back(-1);
	       }else{
	         alltime --;
	         document.getElementById('time').innerHTML=alltime;
		   }   
	      }
	      setInterval("setTime()",1000)   
     </script>
  </head>
  <body>
     <div id="error">
           <div class="error">
                <p style="margin-left: 120px;">页面已超时,请您重新登录！
                   <label id="time" style="font-size:16px">5</label>
                 	秒后，将自动转到登录页！
                   <br/>
                   <br/>
                   <span style="font-size:16px">单击【返回登录页】,直接转到登陆页</span>
                </p>
           </div>
           <center>
			<a style="cursor:pointer;font-size:16px;color:blue;padding:3px 0 3px 10px;text-decoration:underline;" onclick="javascript:top.location.href='${root}login.jsp'">返回登录页</a>
		   </center>
      </div>
	</body>
</html>

