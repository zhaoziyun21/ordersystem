<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>产品详情</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<style>
		label.error{
			color: red;
			font-size: 10px;
		}
		.DIV{
			
			width:260px;
			margin:auto;
			padding-top:10px;
		}
		h4{
			line-height:30px;
		}
		.DIV select{
			width:190px;
		}

		.DIV input.Btn{
			margin-top:14px;
			width:100px;
		}
		#tab{
		margin:50px;
		}
		#tab table th{
			background:#E2F0FF;
			border-right: 1px solid #A3C0E8;
			border-bottom: 1px solid #A3C0E8;
		}
		#tab table td{
			
			border-right: 1px solid #A3C0E8;
			border-bottom: 1px solid #A3C0E8;
		}
		
	</style>
	<script>	
		//关闭
		function clos(){
			parent.$.ligerDialog.close(); 
			//移除遮罩层
			parent.$(".l-dialog,.l-window-mask").remove();
		}
		
	</script>
  </head>
  
  <body style="">	
  	<div id="tab" >
  		<div title="订单详情" lselected="true" tabid="tab1" style="overflow:auto; width:100%">
  			<table  align="center" style="width:100%; border: 1px solid #A3C0E8;font-size: 12px;line-height: 24px;color: #333;">
  				<thead>
  					<tr>
  						<th></th>
  						<th>产品名称</th>
  						<th>产品描述</th>
  						<th>产品价格</th>
  						<th>产品数量</th>
  						<th>产品小计</th>
  					</tr>
  				</thead>
	  			<tbody>
  					<c:forEach items="${detailList }" var="detail">
	  					<tr style="font-size: 12px;line-height: 24px;color: #444;">
	  						<td>
	  							<img src="${root }${detail.pro_image_url }" width='60px' height='60px'/>
	  						</td>
	  						<td>${detail.pro_name }</td>
	  						<td>${detail.pro_describe }</td>
	  						<td>${detail.pro_price }</td>
	  						<td>${detail.pro_num }</td>
	  						<td>${detail.pro_price * detail.pro_num }</td>
	  					</tr>
	  					
		  			</c:forEach>
	  			</tbody>
  			</table>
	  		
	  		
  		</div>
  </div>	
  </body>
</html>
