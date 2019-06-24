
<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html lang="en">

<head>
	<meta charset="UTF-8">
    <!-- 登录页面 -->
    <title>登录</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no" />
    <link rel="stylesheet" type="text/css" href="${root }wx_orders/css/adapter.css">
    <link rel="stylesheet" type="text/css" href="${root }wx_orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="${root }wx_orders/css/orderlogin.css">
    <script type="text/javascript" src="${root }wx_orders/js/wx_orderlogin.js"></script>
</head>

<body>
    <!-- 登录框开始部分 -->
    <div class="denglu">
        <form id="denglu">
            <ul>
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

</body>

</html>
