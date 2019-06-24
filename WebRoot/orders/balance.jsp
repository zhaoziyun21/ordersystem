<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<head>
<meta charset="UTF-8">
<title>账户余额</title>
<link rel="stylesheet" type="text/css" href="../orders/css/common.css">
<link rel="stylesheet" type="text/css" href="../orders/css/balance.css">

<script type="text/javascript" src="../orders/js/jquery.min.js"></script>
<script type="text/javascript" src="../orders/js/balance.js"></script>
</head>
<body>
	<!-- 头部开始 -->
	<%@include file="header.jsp"%>
	<!-- 头部结束 -->
	
	<!-- 订单信息开始部分 -->
	<div class="contain order-object">
		<div class="object-left">账户余额：<span>${balance}</span></div>
	</div>
	<!-- 订单信息部分结束 -->
	
	<!-- 消费明细部分开始 -->
	<div class="consumption-list contain">
		<div class="list-name">消费明细</div>
		<div class="wrap">
		    <table border="1">
		        <thead>
		        <tr>
		            <th width="232px">记录</th>
		            <th width="210px">出入账金额</th>
		            <th width="210px">余额</th>
		            <th width="260px">时间</th>
		            <th width="160px">消费内容</th>
		        </tr>
		        </thead>
		        <tbody id="j_tb">
		        <c:forEach items="${recordList}" var="rec">
			        <tr>
			            <c:if test="${rec.type=='0'}">
			            	<td>收入</td>
			            	<td>+${rec.change_money}</td>
			            </c:if>
			             <c:if test="${rec.type=='1'}">
			            	<td>支出</td>
			            	<td>${rec.change_money}</td>
			            </c:if>
			            <td>${rec.balance}</td>
			            <td>
			            	<fmt:formatDate value="${rec.ins_time}" pattern="yyyy-MM-dd HH:mm:ss" /> 
			            </td>
			            <td>${rec.remark}</td>
			        </tr>
		        </c:forEach>
		       
		        </tbody>
		    </table>
		</div>
	</div>
</body>
</html>