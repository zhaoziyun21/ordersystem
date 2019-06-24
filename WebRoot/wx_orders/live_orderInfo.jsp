<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>提交现场订单</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/adapter.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/orderInfo.css">
    <link rel="stylesheet" href="${root }wx_orders/css/jquery.mloading.css" />
	<script src="${root }wx_orders/js/jquery.min.js" type="text/javascript"></script>
	<script src="${root }wx_orders/js/jquery.mloading.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=basePath%>wx_orders/js/msgbox.js"></script>
    <script>
    	var totalMoney = 0;
		$(function(){
			
			/* 返回上一页 */
			$(".back").click(function(){
				window.history.back();
			})
			
			if("${!empty orderInfo}"){
				var orderInfo = ${orderInfo};
				var json = eval(orderInfo);
				//订餐对象(ZJ:自己、KR：客人、LD：领导、ZD：指定)
				var orderObj = json.orderObj;
				var ldId,zdrId;
				if(orderObj == "LD"){
					//领导ID
					ldId = orderInfo.ldId;
				}else if(orderObj == "ZD"){
					//被指定人ID
					zdrId = orderInfo.zdrId;
				}
				var html = "";
				var totalNum = 0; 
				var totalPrice = 0; 
				var username = json.obj.obj;
				var balance = json.obj.balance==null?"0":json.obj.balance;
				
				var carDetail = json.carDetail;
				for(var i = 0; i < carDetail.length; i ++){
					totalNum += Number(carDetail[i].num);
					totalPrice += Number(carDetail[i].total);
					html += "<li>";
					html += "	<div class=\"car-desc\">"+carDetail[i].food+"</div>";
					html += "	<div class=\"car-num\"> ";
					html += "		<span class=\"reduce\">-</span> ";
					html += "		<span class=\"number\">"+carDetail[i].num+"</span> ";
					html += "		<span class=\"add\">+</span>";
					html += "       <input class=\"food_id\" type=\"hidden\" name=\"m_food_id\" value=\""+carDetail[i].id+"\"/>";		
					html += "       <input class=\"sell_price\" type=\"hidden\" name=\"m_sell_price\" value=\""+carDetail[i].price+"\"/>";
					html += "	</div> ";
					html += "</li>";
				}
				$("#append").append(html);
				$("#totalNum").val(totalNum);//总份数
				$("#username").html(username);//扣除账户
				$("#pay").html(totalPrice);//扣除money
				$("#rest").html(Number(balance)); //扣除后余额
				$(".cost-top-2").html(totalPrice);
				totalMoney = totalNum * 25;
				//====
				/*加減套餐數量*/
				$(".add").off().on("click",function(){
					var s=$(this).siblings(".number").html();
					s++;
					$("#totalNum").val(Number($("#totalNum").val())+1);
					$("#pay").html(Number($("#pay").html())+Number($(this).siblings("input[name='m_sell_price']").val()));
					$(this).siblings(".number").html(s);
					totalMoney  = $("#pay").html();
				})
				$(".reduce").off().on("click",function(){
					var s=$(this).siblings(".number").html();
					s--;
					$("#totalNum").text(Number($("#totalNum").val())-1);
					$("#pay").html(Number($("#pay").html())-Number($(this).siblings("input[name='m_sell_price']").val()));
					totalMoney  = $("#pay").html();
					if(s==0||s<0){
						$(this).parent().parent().remove();
					}
					$(this).siblings(".number").html(s);
				})
				
			}
		});
		
		function dc_food(){
			if(totalMoney == 0){
				 $.MsgBox.Alert("消息", "您还未预定套餐！"); 
				 return;
			}
			
			var orderInfo = ${orderInfo};
			var vistorName = '${vistorName}';
			//var v = JSON.stringify(orderInfo.carDetail);
			var type = orderInfo.orderObj;
			var ldId = orderInfo.ldId;
			//var sumNum = $("#pay").html()/25;//套餐总份数
			var ul=document.getElementById("append").getElementsByTagName("li");
			var carDetail = [];
            for(var i=0;i<ul.length;i++){
	            var orderInfo = {};
	            orderInfo.id = $(ul[i]).find('.car-num .food_id').val();//套餐
	            orderInfo.price = $(ul[i]).find('.car-num .sell_price').val();//实付套餐价格
	            orderInfo.food=$(ul[i]).find('div.car-desc').html();//套餐名称
	            orderInfo.num =$(ul[i]).find('.car-num .number').html();//套餐数量
	            orderInfo.total = ($(ul[i]).find('.car-num .number').html())*25//
	            carDetail.push(orderInfo);
            }  
            var order = JSON.stringify(carDetail);
            
            /* obj.mLoading({
       		    text:"", //加载文字，默认值：加载中...
       		    icon:"", //加载图标，默认值：一个小型的base64的gif图片
       		    html:false, //设置加载内容是否是html格式，默认值是false
       		    content:"", //忽略icon和text的值，直接在加载框中显示此值
       		    mask:true //是否显示遮罩效果，默认显示
       	    }); */
       		$("body").mLoading("show");//显示loading组件
			$.ajax({
			    url:"${root}/wechat/buyLiveFoodWeChat.do",    //请求的url地址
			    dataType:"json",   //返回格式为json
			    data:{
			    	"orderInfo":order,
			    	"type":type,
			    	"ldId":ldId,
			    	"vistorName":vistorName
 			    	},    
			    type:"POST",   //请求方式
			    success:function(req){
			    	if(req.status == "false"){
			    		  $.MsgBox.Alert("消息", req.msg);
			    	}else{
				    	$.MsgBox.Alert("消息", "点餐成功！",function(){
				    		$("body").mLoading("show");//显示loading组件
				        	location.href="${root}/wechat/toMyLiveOrderWeChat.do";
				   		});
			    	}
			    	$("body").mLoading("hide"); //隐藏loading组件
			    },
			    error:function(req){
			    	alert(req.msg);
			    	$("body").mLoading("hide"); //隐藏loading组件
			    }
			});
		}
		
	</script>
</head>
<style>

#consignee_infor {
	-width:18rem;
	height: 3.08rem;
	padding: 0.16rem 1.54rem 0.86rem 1.28rem;
	background: #fff;
	border-bottom: 1px solid #F3F3F3;
}
#consignee_infor .name {
	font-size: 0.8rem;
	color: #414141;
	line-height: 0.8rem;
	height:1.2rem;
}
#consignee_infor .name p {
	float: left;
	margin-right: 0.45rem;
}
#consignee_infor .default {
	width: 2.48rem;
	height: 0.88rem;
	border: 1px solid #ff0000;
	color: #ff0000;
	font-size: 0.8rem;
	text-align: center;
	line-height: 1.8rem;
	margin-left:0.5rem;
	border-radius: 0.05rem;
}
#consignee_infor .address {
	font-size: 0.8rem;
	color: #414141;
	position: relative;
}
#consignee_infor .address p {
	
	line-height: 0.8rem;
	margin-top: 0.2rem;
}
#consignee_infor .address img {
	width: 0.64rem;
    height: 0.64rem;
    position: absolute;
    top: 0.8rem;
    right: -0.76rem;
}

</style>



	<body>
    <div>
        <div class="header">
	        <div class="title">提交现场订单</div>
	        <div class="back"></div>
        </div>
        <div class="accountMsg">
        	<div class="accountName">
        		账户：<span id="username"></span>
        	</div>
        	<div class="accountBalance">
        		余额：<span id="rest">0.00</span>
        	</div>
        </div>
        
        <div style="width:100%">
			<h3 style="display: block;width: 10rem;color: #333;font-weight: 600;font-size: 0.8rem;margin-top: 0.8rem;margin-bottom: 0.6rem;margin-left: 1.28rem;">餐饮公司：${foodCompany.foodCompanyName }</h3>
		</div>
		
		<div style="width:100%">
			<h3 style="display: block;width: 17.96rem;margin-left: 1.28rem;color: red;font-size: 0.7rem;margin-top: 0.8rem;">温馨提示：现场订餐不支持“取消订单”功能，望客户小心谨慎选购！</h3>
		</div>
		
        <div>
    		<div class="buyCar-bottom">
  				<ul id="append">
  				
  				</ul>
  			</div>
    	</div>
    	
    	<div class="footer">
    		<div class="footer-l">
    			<span class="totalPay" id="pay">0.00</span>
    		</div>
    		<div class="footer-r" onclick="dc_food();">
    			提交订单
    		</div>
    		
    	</div>
    </div>
    <div id="mask"></div>
	<input type="hidden" id="totalNum"/>
</body>

</html>

