<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>我的指定订单</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/adapter.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/myorder.css">
    <script type="text/javascript" src="<%=basePath%>wx_orders/js/msgbox.js"></script>
</head>
<style>

	#consignee_infor {
	-width:18rem;
	height: 2.68rem;
	padding: 0.16rem 0.6rem 0.16rem 1rem;
	background: #fff;
	
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
	
	line-height: 1.2rem;
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
	<script type="text/javascript">
	function cancel_myOrder(id,num,odderDate,orderType){
		//location.href="${root}/order/deleteOrderXOrders.do?orderId="+id+"&foodNum="+num+"";
		var dayTime = new Date();
		//获取当前时间（时分秒）
		var year = dayTime.getFullYear();//年
		var month = dayTime.getMonth()+1;//月
		var day = dayTime.getDate();// 日
		var amSart = year+"-"+month+"-"+day+AMSTART; //上午开始
		var amEnd = year+"-"+month+"-"+day+AMEND; //上午结束
		var pmStart = year+"-"+month+"-"+day+PMSTART;// 下午开始
		var pmEnd = year+"-"+month+"-"+day+PMEND;// 下午结束
		//var night = year+"-"+month+"-"+day+" 22:30:00";//晚上
		var amSartTime = new Date(amSart.replace(/-/g,"/"));// 上开
		var amEndTime = new Date(amEnd.replace(/-/g,"/"));// 上结束
		var pmSartTime = new Date(pmStart.replace(/-/g,"/"));// 下开
		var pmEndTime = new Date(pmEnd.replace(/-/g,"/"));// 下结束
		var odderTime = new Date(odderDate.replace(/-/g,"/"));// 订单
		if(orderType == "LUN"){//上午
			if(dayTime>=amSartTime && dayTime<= amEndTime){
				q_my_order(id,num);
			}else{
				$.MsgBox.Alert("取消订餐", "已经超时不能取消订单");
				// $("#mb_con").css({"top": "230px"});
			}
		}else if(orderType == "DIN"){//下午
			if(dayTime>=pmSartTime && dayTime<=pmEndTime){
				q_my_order(id,num);
			}else{
				$.MsgBox.Alert("取消订餐", "已经超时不能取消订单");
				// $("#mb_con").css({"top": "230px"});
			}
		}else{
			$.MsgBox.Alert("取消订餐", "已经超时不能取消订单");
			//$("#mb_con").css({"top": "230px"});
		}
			
	}
	
	function q_my_order(id,num){
		$.ajax({
		    url:"${root}/wechat/deleteOrderWeChat.do",    //请求的url地址
		    dataType:"json",   //返回格式为json
		    data:{
		    	"orderId":id,
		    	"foodNum":num
		    	},    
		    type:"POST",   //请求方式
		    success:function(req){
		    	//$.MsgBox.Alert("取消订餐", "取消订餐成功",function(){
		       		location.href="${root}/wechat/toMyOrderWeChat.do";
		    	//});
		    },
		    error:function(req){
		    	alert(req.msg);
		    }
		}); 
			
	}
	
	
	
	//再来一单
	function again_myOrder(odderDate,orderObj,flag,orderId,sunNum,orderType){
		var dayTime = new Date();
		//获取当前时间（时分秒）
		var year = dayTime.getFullYear();//年
		var month = dayTime.getMonth()+1;//月
		var day = dayTime.getDate();// 日
		var amSart = year+"-"+month+"-"+day+AMSTART; //上午开始
		var amEnd = year+"-"+month+"-"+day+AMEND; //上午结束
		var pmStart = year+"-"+month+"-"+day+PMSTART;// 下午开始
		var pmEnd = year+"-"+month+"-"+day+PMEND;// 下午结束
		//var night = year+"-"+month+"-"+day+" 22:30:00";//晚上
		var amSartTime = new Date(amSart.replace(/-/g,"/"));// 上开
		var amEndTime = new Date(amEnd.replace(/-/g,"/"));// 上结束
		var pmSartTime = new Date(pmStart.replace(/-/g,"/"));// 下开
		var pmEndTime = new Date(pmEnd.replace(/-/g,"/"));// 下结束
		var odderTime = new Date(odderDate.replace(/-/g,"/"));// 订单
		if(orderType == "LUN"){//上午
			if(odderTime>=amSartTime && odderTime<= amEndTime){
				//ag_order(orderId,orderObj,flag,sunNum);
				location.href="${root}/wechat/toIndexWeChat.do";
			}else{
				$.MsgBox.Alert("消息", "已经超时不能订餐");
				 $("#mb_con").css({"top": "230px"});
			}
		}else if(orderType == "DIN"){//下午
			if(odderTime>=pmSartTime && odderTime<=pmEndTime){
				//ag_order(orderId,orderObj,flag,sunNum);
				location.href="${root}/wechat/toIndexWeChat.do";
			}else{
				$.MsgBox.Alert("消息", "已经超时不能订餐");
				 $("#mb_con").css({"top": "230px"});
			}
		}else{
			$.MsgBox.Alert("消息", "已经超时不能订餐");
			$("#mb_con").css({"top": "230px"});
		}
	}
	function ag_order(orderId,orderObj,flag,sunNum){
		 $.ajax({
			    url:"${root}/wechat/againOrderWeChat.do",    //请求的url地址
			    dataType:"json",   //返回格式为json
			    data:{
			    	"orderId":orderId,
			    	},    
			    type:"POST",   //请求方式
			    success:function(req){
			    	var carInfo = JSON.stringify(getInfo1(orderObj,flag,req,sunNum));
					$("#infos").val(carInfo);
					$("#returnCar").submit();
			    },
			    error:function(req){
			    	alert(req);
			    }
			});
	}
	
	function getInfo1(orderObj,flag,orderList,sunNum){
		//套餐信息
		var newOrderInfo = {};
		var orderInfo = orderList;
		if(orderInfo != null && orderInfo != ""){
			//var json = eval('('+orderInfo+')');
			//var json = JSON.stringify(orderInfo);
			//var flag = JSON.stringify(flag);
			//订餐对象(ZJ:自己、KR：客人、LD：领导、ZD：指定)
			 if(flag == "3"){
				//领导IDcdsdffsfef
				//newOrderInfo.ldId = json.ldId;
				 newOrderInfo.orderObj = "LD";
			}else if(flag == "2"){
				//被指定人ID
				//newOrderInfo.zdrId = json.zdrId;
				newOrderInfo.orderObj = "KR";
			}else{
				newOrderInfo.orderObj = "ZJ";
			}
			var carDetail = [];
			for ( var o in orderList) {
				var detailOne = {};
				detailOne.id = orderList[o].id;
				detailOne.food = orderList[o].food_name+"("+orderList[o].food_desc+")";   
				detailOne.num = orderList[o].food_num;
				detailOne.total =  orderList[o].food_num*25;
				detailOne.allTotal = sunNum;
				carDetail.push(detailOne);
			}
			newOrderInfo.carDetail = carDetail;
			newOrderInfo.obj = null;
		}
		
		//确认订餐
//		console.log("newOrderInfo--------"+JSON.stringify(newOrderInfo));
		return newOrderInfo;
	}
	
	
	</script>
<body>
 	<div id="mask"></div>
 	<div>
         <div class="header">
           	<span>指定订单</span>
           		<div class="triangle"></div>
           		<ul>
           			<li onclick="window.location='${root}wechat/toMyOrderWeChat.do'"><span >正常订单 </span><div></div></li>
           			<li onclick="window.location='${root}wechat/toMyReserveOrderWeChat.do'"><span>预约订单</span><div ></div></li>
           			<li onclick="window.location='${root}wechat/toMyLiveOrderWeChat.do'" style="border: 1px solid #f1f1f1;"><span>现场订单</span><div ></div></li>
           			<li class="appoint" style="display: none;"onclick="window.location='${root}wechat/toAppointMyOrderWeChat.do'"><span class="choose" >指定订单</span><div class="choosed"></div></li>
           		</ul>
        </div>
        <div>
            <ul class="allMeal">
            
            <c:if test="${!empty myOrderList }">
            	<s:iterator value="myOrderList" var="order">
                <li class="all-meal clearfix">
                <div class="clearfix total-meal">
                 <!--   <div class="meal">
                        <img src="">
                    </div> -->
                    
                    <div style="width:100%">
						<h3 style="width: 10rem;color: #333;font-weight: 600;font-size: 0.8rem;margin-bottom: 0.4rem;margin-left: 1.06rem;float: left;height: 1.2rem;line-height: 1.2rem">餐饮公司：${order.xUser.foodCompanyName }</h3>
						<c:if test="${order.orderCategory=='XC' }">
							<h3 style="width: 3.3rem;color: #FF8F33;font-size: 0.64rem;margin-bottom: 0.4rem;margin-left: 1.06rem;float: right;height: 1.2rem;line-height: 1.35rem">现场订餐</h3>
						</c:if>
						<div style="clear: both;"></div>
					</div>
                    
                    <c:if test="${order.xUser.whetherSendStatus==0 && order.orderCategory!='XC'}">
	                    <!-- 收货人信息 -->
						<ul id="consignee_infor">
							<a href="javascript:;">
								<li class="address">
									<input type="hidden" value="">
									<p><span>收餐地址：<span>${order.xFoodSendAddress.regionName }</span> ${order.xFoodSendAddress.park } ${order.xFoodSendAddress.highBuilding } ${order.xFoodSendAddress.unit } ${order.xFoodSendAddress.roomNum }</span></p>
								</li>
							</a>
						</ul>
					</c:if>
					
                    <div class="meal-det">
                        <div class="meal-det-top clearfix">
                            <div class="mealName">${order.foodNameStr }</div>
                            <div class="detail">详情</div>
                        </div>
                        <div class="clearfix">
	                        <div class="total">总计：${order.moenyTotal }元</div>
	                         <div class="object">订单人：${order.orderName}</div>
                        </div>
                        
                        <div>
                            <div class="orderTime">${order.orderTime }</div>
                           <%--  <c:if test="${ not empty order.isShow}">
                            	<div class="isCancel" onclick="cancel_myOrder('${order.id}','${order.foodTypeNum}','${order.orderTime }','${order.orderType }');">取消订单</div>
                            </c:if>
                             <c:if test="${empty order.isShow}">
                            	<div class="isCancel">已完成</div>
                            </c:if> --%>
                            <%-- <div class="isCancel" onclick="again_myOrder('${order.orderTime }','${order.orderObj}','${order.flag}','${order.id}','${ order.foodTypeNum*25}','${order.orderType }');">再来一单</div> --%>
                        </div>
                    </div>
                  </div>
                  <div class="detail-meal">
                  	<div class="total-num">总计${fn:length(order.detailList) }份</div>
                  	<%-- <div class="again" onclick="again_myOrder('${order.orderTime }','${order.orderObj}','${order.flag}','${order.id}','${ order.foodTypeNum*25}','${order.orderType }');">再来一单</div> --%>
                  	<ul>
                  		<c:forEach items="${order.detailList }" var="detail">
                  		<li class="eve-meal">
                  			<div class="clearfix total-meal">
			                    <div class="meal">
			                        <img style=" width: 5.76rem;height: 4.10666667rem;" src="${root}${empty detail.food_img ? '/images/default.jpg': detail.food_img}">
			                    </div>
			                    <div class="meal-det-b">
			                        <div class="meal-det-top clearfix">
			                            <div class="mealName">${detail.food_name }</div>
			                        </div>
			                        <div class="clearfix" style=" width: 10rem;">
				                        <div class="total">${detail.food_desc }</div>
			                        </div>
			                        
			                        <div>
			                            <div class="orderTime">${detail.food_num }*${detail.pay_price }</div>
			                        </div>
			                    </div>
                 			 </div>
                  		</li>
						</c:forEach>
                  	</ul>
                  </div>
                </li>
                </s:iterator>
			</c:if>
			
			 <c:if test="${empty myOrderList }">
			 	<center style="margin-top: 1rem;font-size: 0.7rem;">暂无订单</center>
			 </c:if>
                  	</ul>
                  </div>
                </li>
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
               <%--  <li class="myOrder">
                    <div class="footer-title" onclick="window.location='${root}wechat/toMyReserveOrderWeChat.do'">我的预定</div>
                </li> --%>
                <li class="balance">
                    <div class="footer-title" onclick="window.location='${root}wechat/toBalanceWeChat.do'">余额</div>
                </li>
            </ul>
        </div>
     <form id="returnCar" action="${root }wechat/toOrderingPageWeChat.do"  method="post">
		<input name="infos" type="hidden" id="infos" value=""/>
	</form>
    </div>
	<script>
	$(function(){
		$(".detail").click(function(){
			$(this).parent().parent().parent().siblings().slideToggle("slow");
		});
		$(".header>span").click(function(){
			$("#mask").css("display","block");
			$(".header ul").slideDown("normal");
			$(".triangle").css("display","block");
		})
		$(".header li").click(function(){
			$(".header li span").removeClass();
			$(".header li div").removeClass();
			$(this).find("span").addClass("choose");
			$(this).find("div").addClass("choosed");
			$(".header>span").html($(this).find("span").html())
			$("#mask").css("display","none");
			$(".header ul").css("display","none");
			$(".triangle").css("display","none");
		});
		$("#mask").click(function(){
			$("#mask").css("display","none");
			$(".header ul").css("display","none");
			$(".triangle").css("display","none");
		})
	})
	var isZD = '${isZD}';
	if(isZD =="true"){
		$(".appoint").show();
	}else{
		$(".appoint").hide();
	}
	</script>
</body>
</html>
