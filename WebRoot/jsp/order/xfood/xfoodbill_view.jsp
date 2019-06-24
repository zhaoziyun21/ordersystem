<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>头部</title>
<link rel="stylesheet" type="text/css" href="${root }orders/css/common.css">
<style type="text/css">
	.table{
	    border:1px solid #DDDDDD;
	    background-color: #FFFFFF;
	    color:#333333;
	} 
	.table th,.table td {
		height:42px;
		text-align:center;
	}
	.table tr td {
		font-size:14px;
	}
</style>
</head>
<script type="text/javascript">
	function goEdit(){
		location.href="${root}jsp/order/xfood/xfoodbill_form.jsp";
	}
</script>
<body>
	<input type="button" value="编辑" onclick="goEdit();"/>
	<table border="1" class="table">
		<tr>
			<th colspan="2">时间</th>
			<th>套餐名</th>
			<th>描述</th>
			<th>单价</th>
			<th>份数</th>
		</tr>
		<tr>
			<td rowspan="5" width="106px">周一</td>
			<td rowspan="3" width="102px">午餐</td>
			<td width="196px">套餐A</td>
			<td width="253px">猪肉粉条+可乐</td>
			<td width="204px">25</td>
			<td width="208px">14</td>
		</tr>
		<tr>
			<td>套餐B</td>
			<td>鱼香茄子+橙汁</td>
			<td>25</td>
			<td width="208px">14</td>
		</tr>
		<tr>
			<td>套餐C</td>
			<td>宫保鸡丁+可乐</td>
			<td>25</td>
			<td width="208px">14</td>
		</tr>
		<tr>
			<td rowspan="2">晚餐</td>
			<td>套餐D</td>
			<td>宫保鸡丁+橙汁</td>
			<td>25</td>
			<td width="208px">14</td>
		</tr>
		<tr>
			<td>套餐C</td>
			<td>宫保鸡丁+橙汁</td>
			<td>25</td>
			<td width="208px">14</td>
		</tr>
	</table>
	</script>
</body>
</html>

