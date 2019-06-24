<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>在线支付</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/adapter.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/detail.css">
    <script type="text/javascript">
$(function() {
		var type=getnum("type");
			if(type == 'Y'){
				$("#is_show").show();
			}else if(type == 'S'){
				$("#is_show1").show();
			}else{
				$("#is_show2").show();
			}
	
	});
	/**
		获取url链接
	*/
	function getnum(name){
		return decodeURIComponent((new RegExp('[?|&]'+name+'='+'([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g,'%20'))||null;
	}
</script>
<style type="text/css">
.JieGuo{
	width:60%;
	margin:80px auto 20px;
}
.JieGuoimg{
	width:80px;
	height:80px;
	margin:auto;
	
}
.JieGuo h1{
	text-align:center;
	font-size:20px;
	padding:30px 0;
	font-weight:500;
}
.JieGuoC .JieGuoimg{
	background:url('<%=basePath%>wx_orders/images/ChengG.png') no-repeat  center;
	background-size:100% 100%;
}
.JieGuoC h1{
	color:rgba(78,179,75,1);
}
.JieGuoS .JieGuoimg{
	background:url('<%=basePath%>wx_orders/images/ShiBai.png') no-repeat  center;
	background-size:100% 100%;
}
.JieGuoS h1{
	color:rgba(198,43,39,1);
}

.JieGuoY .JieGuoimg{
	background:url('<%=basePath%>wx_orders/images/YeEBuZu.png') no-repeat  center;
	background-size:100% 100%;
}
.JieGuoY h1{
	color:rgba(238,200,0,1);
}

</style>
</head>
<body>
	<div class="JieGuo JieGuoC" id="is_show" style="display: none;">
		<div class="JieGuoimg">
		
		</div>
		<h1>支付成功</h1>	
	</div>
	<div class="JieGuo JieGuoY" id="is_show1" style="display: none;">
		<div class="JieGuoimg">
		
		</div>
		<h1>余额不足</h1>	
	</div>
	<div class="JieGuo JieGuoS" id="is_show2" style="display: none;">
		<div class="JieGuoimg">
		
		</div>
		<h1>支付失败</h1>	
	</div>
	<div  style="width:80%; margin:30px auto;">
		<input style="width:100%; background:rgba(255,143,51,1); border-radius:4px; height:40px;line-height:40px; color:#fff; font-size:16px; font-weight:600;" type="button" onclick="window.location='${root}wechat/toIndexWeChat.do'" value="回到首页">
 	</div>
<%--  <div class="footer">
     <ul>
         <li class="balance">
             <div class="footer-title" onclick="window.location='${root}wechat/toIndexWeChat.do'">订餐</div>
         </li>
        <li class="myOrder">
             <div class="footer-title" onclick="window.location='${root}wechat/toMyOrderWeChat.do'">我的订单</div>
         </li>
         <li class="order">
             <div class="footer-title" onclick="window.location='${root}wechat/toBalanceWeChat.do'">余额</div>
         </li>
     </ul>
 </div> --%>
</body>
</html>
