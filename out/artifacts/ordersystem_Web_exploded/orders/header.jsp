<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>头部</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>orders/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>orders/css/header.css">
<script type="text/javascript" src="<%=basePath%>orders/js/header.js"></script>
<script type="text/javascript" src="<%=basePath%>orders/js/msgbox.js"></script>
</head>
<script type="text/javascript">
	function myorder(){
		 location.href="${root}/order/toMyOrderXOrders.do";
		
		
	}
	function toorder(){
		 location.href="${root}/order/toOrderingPageXOrders.do";
		
	}
	function myReserveOrder(){
		 location.href="${root}/order/toMyReserveOrderXOrders.do";
		
	}
	function myLiveOrder(){
		 location.href="${root}/order/toMyLiveOrderXOrders.do";
		
	}
	function myAppointOrder(){
		 location.href="${root}/order/toAppointMyOrderXOrders.do";
		
	}
	function accuntBalance(){
		 location.href="${root}staffSum/getXStaByIdXStaffSum.do";
		
	}
	function logout(){
		 location.href="${root}xuser/orderLogoutXUser.do";
	}
/* 	$(function(){
		$(".myorder").click(function(){
			$(".myorder").removeClass("active");
			if($(this).id=="dingdan"){
				location.href="${root}/order/toMyOrderXOrders.do";
				$(this).addClass("active")
			}else if($(this).id=="shouye"){
				location.href="${root}/order/toOrderingPageXOrders.do";
				$(this).addClass("active")
			}else if($(this).id=="yuding"){
				location.href="${root}/order/toMyReserveOrderXOrders.do";
				$(this).addClass("active")
			}
			else if($(this).id=="zhiding"){
				location.href="${root}/order/toAppointMyOrderXOrders.do";
				$(this).addClass("active")
			}
			else if($(this).id=="yue"){
				location.href="${root}/order/toOrderingPageXOrders.do";
				$(this).addClass("active")
			}
		})
	}) */
</script>
<body>
	<!-- 头部开始 -->
	<div class="header">
		<div class="contain order">
			<div class="order-left">订餐系统</div>
			 <div class="order-right">
                <span class="myorder" onclick="toorder()" id="shouye">首页</span>
                <span class="myorder" onclick="myorder()" id="dingdan">正常订单</span>
                 <span class="myorder" onclick="myReserveOrder()" id="yuding">预定订单</span>
                 <span class="myorder" onclick="myLiveOrder()" id="yuding">现场订单</span>
                 <span class="myorder myAppointOrder" style="display: none;" onclick="myAppointOrder()" id="zhiding">我的指定</span>
                <span class="myorder" onclick="accuntBalance()" id="yue">账户余额</span>
                <span class="myorder accunt">${orderLoginUser.userName}</span>
          		<span class="myorder" onclick="logout()">退出</span>
                	<%-- <input type="hidden" id="user_name" value='${orderLoginUser.userName}'> --%>
            </div>
		</div>
	</div>
	<!-- 头部结束 -->
	<script>
		$(function(){
		/* 	$(".accuntBalance").click(function(){
				window.location.href = "${root}staffSum/getXStaByIdXStaffSum.do";
			});
			$(".logout").click(function(){
				window.location.href = "${root}xuser/orderLogoutXUser.do";
			}); */
			var isZD = '${isZD }';
			 if(isZD =="true"){
				$(".myAppointOrder").show();
			}else{
				$(".myAppointOrder").hide();
			}
		});
	</script>
</body>
</html>
