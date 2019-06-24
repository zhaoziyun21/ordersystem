<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html style="background:#f8f8f8;">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>在线支付</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/adapter.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/detail.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/mobiscroll.custom-2.5.2.min.css">
    <script type="text/javascript" src="<%=basePath%>wx_orders/js/msgbox.js"></script>
    <script type="text/javascript" src="<%=basePath%>wx_orders/js/mobiscroll.custom-2.5.2.min.js"></script>
    <script type="text/javascript">
$(function() {
	$("#commit").click(function() {
		var zh=$("#canYinId").val();//餐饮公司ID
		if(zh == "" || zh == null){
			 $.MsgBox.Alert("消息", "请选择餐饮公司");
			 return  false;
		}
		var input_value=$("#jine").val();
		if(!regNum(input_value)){
			return false;
		}
		var canYinName = $("#canYinName").val();
		var arr={'dianpu':zh,'jine':input_value};
		 $.MsgBox.Alert("店名:"+canYinName, "消费"+input_value+"元 ，确定支付吗",function(){
			 $.ajax({
				type:"post",
				url:'${root}onlinePay/doChargeOnlinePay.do',
				dataType:"html",
				data:arr,
				success:function(data) {
					window.location="${root}/wx_orders/online_pay_msg.jsp?type="+data;
				}
			}); 
			
		});
		 
	});
	/**
		公司下拉框
	*/
	$('#car_select' ).mobiscroll().select({
	        theme: 'android-ics light',
	        mode: 'scroller',
	        display: 'bottom',
	        lang: 'zh',
	        cancelText: "取消",
	        headerText: '选择餐饮公司',
	        onSelect: function(value) {
	            $("#canYinId").val($("#car_select").val());
	            $("#canYinName").val(value);
	        }
	    });
	});
	// 金额验证
	function regNum(input_value){
		var bon = true;
		var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
		if(input_value == "" || input_value == null){
			 $.MsgBox.Alert("消息", "金额不能为空");
			 bon =  false;
		}else if(!reg.test(input_value)){
			 $.MsgBox.Alert("消息", "请输入正确金额");
			 bon =  false;
		}
		return bon;
	}
	function getnum(name){
		return decodeURIComponent((new RegExp('[?|&]'+name+'='+'([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g,'%20'))||null;
	}
</script>
<style>
.Qian{
	position: relative; 
	background:#fff;
	box-sizing:border-box; 
	padding:0 4px; 
	overflow:hidden; 
	margin:18px 18px; 
	border-radius:4px; 
	height:44px; line-height:44px; 
	border:1px rgba(255,143,51,1) solid; 
	box-shadow:0 0 5px rgba(255,143,51,.4)
}
.spanA{
	padding-left:12px;  
	position:absolute;left:0;top:0; 
	color:#999;display:block; 
	font-size:16px; text-align:left; 
	width:80px; font-weight:600; 
	line-height:44px; height:44px;
}
.spanB{
	position:absolute;
	right:0;top:0;
	font-size:16px; 
	text-align:right; 
	padding-right:12px;
	line-height:44px; height:44px;
	color:#999;display:block; 
}
#car_select_dummy{
	/* background:#ca4341; */
	margin:0;border:0;
	vertical-align:inherit;
	padding-left:100px; width:98%;
	box-sizing:border-box; 
	line-height:44px; height:44px;
	font-size:16px;
	text-aglin:center;
}
</style>
</head>
<body style="background:#f8f8f8; padding-top:12px; box-sizing:border-box;">
	<div>
		<div class="Qian" style=" ">
		
			<span class="spanA" style="">金额：</span>
			<span class="spanB" style="">元</span>
			<input  style="margin:0;border:0;vertical-align:inherit;padding-left:76px;padding-right:40px; width:98%; box-sizing:border-box; line-height:42px; height:42px;font-size:16px;" type="number" name="jine" id="jine">
		</div>
		<div  class="Qian">
			<span class="spanA">餐饮公司：</span>
			<select class="selectDIv" id="car_select" data-type="selectP" style="display: none;">
			<option selected="selected" value="">请选择</option>
		     　　		<c:forEach items="${listUser}" var="user">
					<option value="${user.id}">${user.foodCompanyName}</option>
				</c:forEach>       
		 	</select><br><br>
	 	</div>
	 	<div style="width:80%; margin:30px auto;">
			<input style="width:100%; background:rgba(255,143,51,1); border-radius:4px; height:40px;line-height:40px; color:#fff; font-size:16px; font-weight:600;" type="submit" value="确定" id="commit">
			<input type="hidden" value="" id="canYinId">
			<input type="hidden" value="" id="canYinName">
		</div>
	</div>
 <div class="footer">
     <ul>
         <li class="balance">
             <div class="footer-title" onclick="window.location='${root}wechat/toIndexWeChat.do'">订餐</div>
         </li>
        <li class="myOrder">
             <div class="footer-title" onclick="window.location='${root}wechat/toMyOrderWeChat.do'">我的订单</div>
         </li>
         <li class="order">
             <div class="footer-title" onclick="window.location='${root}wechat/toBalanceWeChat.do'">余额</div>
         </li>
     </ul>
 </div>
</body>
</html>
