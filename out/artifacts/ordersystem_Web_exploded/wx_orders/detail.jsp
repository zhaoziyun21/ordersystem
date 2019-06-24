<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>明细</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/adapter.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/detail.css">
</head>

<body>
<div>
   <div>
		<div>
			<ul class="allDet">
				<c:if test="${!empty detailRecordList }">
					<s:iterator value="detailRecordList" var="dr">
						<li class="detail clearfix">
							<div class="detail-l">
								<p><s:if test="type==0">+</s:if><s:if test="type==1">-</s:if><s:property value="changeMoney"/></p>
								<div>
									<span><s:property value="%{getText('{0,date,yyyy-MM-dd HH:mm:ss}',{#dr.insTime})}" /></span>
									<span><s:property value="remark"/></span>
								</div>
							</div>
							<div class="detail-r">余额：<s:property value="balance"/></div>
						</li>
					</s:iterator>
				</c:if>
			</ul>
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
</body>
</html>
