<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>余额</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/adapter.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/balance.css">
</head>

<body>
<div>
        <div class="header">
            余额
        </div>
        <div class="current">
        	<p>当前余额（元）</p>
        	<span>${balance }</span>
        </div>
        <div class="detail" onclick="toDetail();">
        	<span >明细</span>
        </div>
        <div class="footer">
            <ul>
                <li class="order">
                    <div class="footer-title" onclick="window.location='${root}wechat/toIndexWeChat.do'">订餐</div>
                </li>
               <li class="myOrder">
                    <div class="footer-title" onclick="window.location='${root}wechat/toMyOrderWeChat.do'">我的订单</div>
                </li>
                <li class="balance">
                    <div class="footer-title" onclick="window.location='${root}wechat/toBalanceWeChat.do'">余额</div>
                </li>
            </ul>
        </div>
    </div>
    <script>
    	function toDetail(){location.href = "${root}wechat/toDetailWeChat.do";}
    </script>
</body>
</html>
