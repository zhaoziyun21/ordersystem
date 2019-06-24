<!DOCTYPE HTML>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <!-- 登录页面 -->
    <title>登录</title>
    <link rel="stylesheet" type="text/css" href="${root }orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="${root }orders/css/orderlogin.css">
    <script type="text/javascript" src="${root }orders/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>orders/js/msgbox.js"></script>
    <script type="text/javascript" src="${root }orders/js/orderlogin.js"></script>
</head>

<script type="text/javascript">
	$().ready(function(){
		$.ajax({
            type: 'post',
            url: "http://"+window.location.host+"/ordersystem/xuser/orderLoginXUser.do",
            cache: false,
            dataType: "html",
            data:{"flag":"sso"},
            success: function(data) {
            	var username = $("#userName").val(); 
            	var url = "http://"+window.location.host+"/ordersystem/order/toOrderingPageXOrders.do";
                if(data=='Y'){
            		window.location.href = url;
            	}else if(data == "N"){
            		$.MsgBox.Alert("消息","对不起，您没有此权限！");
            		setTimeout(function (){
			        	window.location.href = "${root}/orders/orderLoginSSO.jsp";
			        }, 1000);
            	}else{
					window.location.href = "${root}/orders/orderLoginSSO.jsp";
				}
            },
            error: function(data) {
               
            }
        });
	
	});
</script>
<body>

</body>

</html>
