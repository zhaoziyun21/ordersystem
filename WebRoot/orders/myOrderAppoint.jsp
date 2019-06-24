<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<script type="text/javascript">
	$(function(){
		
	}); 
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
				 $("#mb_con").css({"top": "230px"});
			}
		}else if(orderType == "DIN"){//下午
			if(dayTime>=pmSartTime && dayTime<=pmEndTime){
				q_my_order(id,num);
			}else{
				$.MsgBox.Alert("取消订餐", "已经超时不能取消订单");
				 $("#mb_con").css({"top": "230px"});
			}
		}else{
			$.MsgBox.Alert("取消订餐", "已经超时不能取消订单");
			$("#mb_con").css({"top": "230px"});
		}
			
	}
	
	function q_my_order(id,num){
		$.MsgBox.Alert("取消订餐", "是否取消订单",function(){
			$.ajax({
			    url:"${root}/order/deleteOrderXOrders.do",    //请求的url地址
			    dataType:"json",   //返回格式为json
			    data:{
			    	"orderId":id,
			    	"foodNum":num
			    	},    
			    type:"POST",   //请求方式
			    success:function(req){
			    	//$.MsgBox.Alert("取消订餐", "取消订餐成功",function(){
			       		location.href="${root}/order/toMyOrderXOrders.do";
			    	//});
			    },
			    error:function(req){
			    	alert(req.msg);
			    }
			}); 
		}); 
		
	}
	//再来一单
/* 	function again_myOrder(odderDate){
		var dayTime = new Date();
		//获取当前时间（时分秒）
		var year = dayTime.getFullYear();//年
		var month = dayTime.getMonth()+1;//月
		var day = dayTime.getDate();// 日
		var amSart = year+"-"+month+"-"+day+AMSTART; //上午开始
		var amEnd = year+"-"+month+"-"+day+AMEND; //上午结束
		var pmStart = year+"-"+month+"-"+day+" 14:00:00";// 下午开始
		var pmEnd = year+"-"+month+"-"+day+" 16:30:00";// 下午结束
		//var night = year+"-"+month+"-"+day+" 22:30:00";//晚上
		var amSartTime = new Date(amSart);// 上开
		var amEndTime = new Date(amEnd);// 上结束
		var pmSartTime = new Date(pmStart);// 下开
		var pmEndTime = new Date(pmEnd);// 下结束
		var odderTime = new Date(odderDate);// 订单
		if((odderTime>=amSartTime && odderTime<= amEndTime) ||(odderTime>=pmSartTime && odderTime<=pmEndTime)){
			 location.href="${root}/order/toOrderingPageXOrders.do";
		}else{
			$.MsgBox.Alert("再来一单", "不在订餐时间内");
		}
	} */
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
			if(dayTime>=amSartTime && dayTime<= amEndTime){
				ag_order(orderId,orderObj,flag,sunNum);
			}else{
				$.MsgBox.Alert("消息", "已经超时不能订餐");
				 $("#mb_con").css({"top": "230px"});
			}
		}else if(orderType == "DIN"){//下午
			if(dayTime>=pmSartTime && dayTime<=pmEndTime){
				ag_order(orderId,orderObj,flag,sunNum);
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
			    url:"${root}/order/againOrderXOrders.do",    //请求的url地址
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
	
	//餐饮指定订单
	function myCanyinAppointOrder(){
		location.href="${root}/order/toAppointMyOrderXOrders.do";
	}
	//产品指定订单
	function myProAppointOrder(){
		location.href="${root}/product/toAppointMyOrderXProducts.do";
	}
</script>
<meta charset="UTF-8">
<title>我的指定</title>
<link rel="stylesheet" type="text/css" href="../orders/css/common.css">
<style type="text/css">
	.object-right {
	    float: right;
	    margin-right: 35px;
	}
	.object-right button {
	    width:80px;
	    height:30px;
	    background-color:#FF8F33;
	    color:#FFFFFF;
	    font-size:14px;
	    cursor:pointer;
	    margin-top: 20px;
	}
</style>
</head>
<body>
	<!-- 头部开始 -->
<%-- 	<div class="header">
		<div class="contain order">
			<div class="order-left">订餐系统</div>
			<div class="order-right">
				<a href="javascript:;" class="myorder" onclick="myorder()">我的订单</a> <a
					href="javascript:;" class="login">${orderLoginUser.userName}<span class="login-r">∨</span></a>
			</div>
		</div>
	</div> --%>
	<!-- 头部结束 -->
	<%@include file="header.jsp"%>
	<!-- 订单信息开始部分 -->
	<div class="contain order-object">
		<div class="object-left">指定订单信息</div>
		<div class="object-right">
			<button onclick="myProAppointOrder();" style="line-height:30px">产品指定</button>
		</div>
		<div class="object-right">
			<button onclick="myCanyinAppointOrder();" style="line-height:30px">餐饮指定</button>
		</div>
	</div>
	<!-- 订单信息部分结束 -->
	
	<!-- 订餐详情部分开始 -->
		<div class="contain det">
			<ul>
			<c:if test="${ not empty myOrderList}">
				<c:forEach items="${myOrderList}" var="myOrder">
					<li>
						<div class="detail">
							<div style="margin-left: 12px;font-size: 14px;color: red;font-weight: 700;margin-top: 10px;">餐饮公司：<span>${myOrder.xUser.foodCompanyName }</span> </div>
							<div style="clear: both;"></div>
							<div class="detail-top clearfix">
								<div class="detail-top-left">
									<!-- <div class="detail-top-left-1"><img src="" class="meal"></div> -->
									<div class="detail-top-left-2">
										<div class="p1" style=" float: left; margin-top: 22px; ">${myOrder.foodNameStr }</div>
										<div class="p2" style=" float: left; margin-top: 22px; margin-left: 20px;">订单人：<span>${myOrder.orderName }</span></div>
										<div style="clear: both;"></div>
									</div>
								</div>
								<div class="detail-top-middle">
									<div class="p1"></div>
									<div class="p2" style="margin-left: 137px;">下单时间：<span>${myOrder.orderTime }<fmt:formatDate value="${rec.ins_time}" pattern="yyyy-MM-dd HH:mm:ss" /> </span></div>
									
									<div style="clear: both;"></div>
								</div>
								<span  style="float: right;margin-top: 15px;font-weight: 600;color:red"><c:if test="${myOrder.orderCategory=='XC' }">现场订餐</c:if> </span>
								<div class="detail-top-right">
									<%-- <div class="p1"><button onclick="again_myOrder('${myOrder.orderTime }','${myOrder.orderObj}','${myOrder.flag}','${myOrder.id}','${ myOrder.foodTypeNum*25}','${myOrder.orderType }');">再来一单</button></div> --%>
									<%-- <c:if test="${ not empty myOrder.isShow}">
										<div class="p1 is_show"><button onclick="cancel_myOrder('${myOrder.id}','${myOrder.foodTypeNum}','${myOrder.orderTime }','${myOrder.orderType }');">取消订单</button></div>
									</c:if>
									<c:if test="${empty myOrder.isShow}">
										<div class="p1 is_hiden"><button>已完成</button></div>
									</c:if> --%>
								</div>
							</div>
							
							<c:if test="${myOrder.xUser.whetherSendStatus==0 && myOrder.orderCategory!='XC'}">
								<div class="detail-bottom-dizhi" style="margin-bottom: 12px;margin-left: 10px;height:19px;">
									<img src="../orders/images/weizhi.png">
									<span>收餐地址 : </span>
									<span><span style="margin-left: 2px;">${myOrder.xFoodSendAddress.regionName }</span> ${myOrder.xFoodSendAddress.park } ${myOrder.xFoodSendAddress.highBuilding } ${myOrder.xFoodSendAddress.unit } ${myOrder.xFoodSendAddress.roomNum }</span>
								</div>
							</c:if>
							
						<div class="detail-bottom">
							<div class="detail-bottom-1">菜品共<pan class="detail-bottom-11">${myOrder.foodTypeNum}</pan>份，总计<span class="detail-bottom-12">${ myOrder.moenyTotal}</span>元</div>
							<div class="detail-bottom-2">
								<ul class="clearfix">
									<c:if test="${!empty myOrder.detailList}">
										<c:forEach items="${myOrder.detailList }" var="detail">
											<li class="clearfix" style="width: 200px">
												<!-- 缺少图片 -->
												<img width="100%" height="100%"  src="${root}${empty detail.food_img ? '/images/default.jpg': detail.food_img}" class="meal">
												<span>${detail.food_desc }</span>
												<span>${detail.food_num }*${detail.pay_price }</span>
											</li>
										</c:forEach>
									</c:if>
								</ul>
							</div>
							<div class="accountMsg clearfix">
								<ul>
									<li class="username">扣除账户：<span>${myOrder.orderObj}</span></li>
									<li class="pay">扣除余额：<span>${ myOrder.moenyTotal}</span></li>
									<!-- <li class="rest">剩余：<span>300.00</span></li> -->
								</ul>
							</div>
						</div>
					</div>
					</li>
				</c:forEach>
			</c:if>
			<c:if test="${empty myOrderList}">
				<div class="object-left">暂无订单</div>
			</c:if>
			
			</ul>
		</div>
	<form id="returnCar" action="${root }order/toOrderingPageXOrders.do"  method="post">
		<input name="infos" type="hidden" id="infos" value=""/>
	</form>
</body>
<link rel="stylesheet" type="text/css" href="../orders/css/myorder.css">
</html>