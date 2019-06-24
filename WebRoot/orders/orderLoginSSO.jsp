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

<body>
<div class="AdminBg">
    <!-- 头部开始 -->
    <div class="header">
        <div class="contain order">
            <div class="order-left">订餐系统</div>
            <div class="order-right">
                <span href="javascript:;" class="myorder">我的订单</span>
                <a href="javascript:;" class="login">请登录</a>
            </div>
        </div>
    </div>
    <!-- 头部结束 -->

    <!-- 登录框开始部分 -->
    <div class="denglu">
        <form id="denglu">
            <ul>
                <li class="denglu-1">登录</li>
                <li class="denglu-2">
                    <span>账号</span>
                    <input type="text" name="xuser.userName" id="userName">
                </li>
                <li class="denglu-3">
                    <span>密码</span>
                    <input type="password" name="xuser.password" id="password">
                </li>
                <li class="denglu-5">
                    <button id="denglubtn">登录</button>
                </li>
            </ul>
        </form>
    </div>
    <div class="mask"></div>
    </div>
</body>

</html>
