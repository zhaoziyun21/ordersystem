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
		.DIV ul{
			margin-top: 14px;
		}
		.DIV ul li{
			padding-top: 8px;
			
		}
		.DIV ul li span{
		padding-left: 5px;
		}
		.Div ul li input{
			width:136px;
			height:20px;
		
		}
		.Div ul li select{
			width:136px;
			height:20px;
		}
		.Div ul li textarea{
			width:136px;
			height:20px;
		}
		
	</style>
	<script>		
		
		$(document).ready(function(){
			var h = $(window).height(), h2;
		    $(".auditstaff_content").css("height", h);
		    $(window).resize(function() {
		    	h2 = $(this).height();
		    	$(".auditstaff_content").css("height", h2);
			});
		      
		});
		  
		//关闭
		function clos(){
			parent.$.ligerDialog.close(); 
			//移除遮罩层
			parent.$(".l-dialog,.l-window-mask").remove();
		}
		
	</script>
  </head>
  
  <body style="">	
  	<div id="tab">
  		<div title="产品详情" lselected="true" tabid="tab1" style="overflow:auto; ">
  			<div class="DIV" style="margin-right: 160px">
  				<ul>
  					<li><b>产品名称：</b> <span style="padding-left: 0px;">${proName }</span> </li>
  					<li><b>产品价格：</b><span>${proPrice } </span></li>
  					<li><b>产品库存量：</b><span>${proRemain } </span></li>
  					<li><b>产品描述：</b><span>${proDescribe }</span></li>
  					<li><b>外链地址：</b><span><a href="${proOutUrl }" target="_blank">${proOutUrl }</a></span></li>
  					<li><h4>产品图片：</h4>
		  				<div id="" style="width: 120px;height: 120px; border: 1px solid #A3C0E8;">
		  				<img src="${root }/${proImageUrl }" width='120' height='120'/>
					    </div>
					</li>
  				</ul>
  				
  				 <input type="button" class="Btn" value="关闭" onclick="clos();"/>
  			</div>
  		</div>
  	</div>
  	
  </body>
</html>
