<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
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
			if(odderTime>=amSartTime && odderTime<= amEndTime){
				if(dayTime>=amSartTime && dayTime<= amEndTime){
					q_my_order(id,num);
				}else{
					$.MsgBox.Alert("取消订餐", "已经超时不能取消订单");
				}
			}else{
				$.MsgBox.Alert("取消订餐", "已经超时不能取消订单");
				// $("#mb_con").css({"top": "230px"});
			}
		}else if(orderType == "DIN"){//下午
			if(odderTime>=pmSartTime && odderTime<=pmEndTime){
				q_my_order(id,num);
			}else{
				$.MsgBox.Alert("取消订餐", "已经超时不能取消订单");
				 //$("#mb_con").css({"top": "230px"});
			}
		}else{
			$.MsgBox.Alert("取消订餐", "已经超时不能取消订单");
			//$("#mb_con").css({"top": "230px"});
		}
			
	}
	
	function q_my_order(id,num){
		$.MsgBox.Confirm("取消订餐", "是否取消订单",function(){
			/* obj.mLoading({
			    text:"", //加载文字，默认值：加载中...
			    icon:"", //加载图标，默认值：一个小型的base64的gif图片
			    html:false, //设置加载内容是否是html格式，默认值是false
			    content:"", //忽略icon和text的值，直接在加载框中显示此值
			    mask:true //是否显示遮罩效果，默认显示
			   }); */
	    	//$("body").mLoading("show");//显示loading组件
			$.ajax({
			    url:"${root}order/deleteOrderXOrders.do",    //请求的url地址
			    dataType:"json",   //返回格式为json
			    data:{
			    	"orderId":id,
			    	"foodNum":num,
			    	"orderType":"0"
			    	},    
			    type:"POST",   //请求方式
			    success:function(req){
			    	if(req.success == "false"){
			    		$.MsgBox.Alert("取消订餐", "已经完成不能取消订单");
			    	}else{
			    		$.MsgBox.Alert("消息", "取消成功！",function(){
				    		//$("body").mLoading("show");//显示loading组件
				       		location.href="${root}/order/toMyOrderXOrders.do";
				   		});
			    	}
			    	//$("body").mLoading("hide");//隐藏loading组件
			    },
			    error:function(req){
			    	alert(req.msg);
			    	//$("body").mLoading("hide");//隐藏loading组件
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
		var amSart = year+"-"+month+"-"+day+" 09:00:00"; //上午开始
		var amEnd = year+"-"+month+"-"+day+" 11:30:00"; //上午结束
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
	
	//餐饮订单
	function myCanyinOrder(){
		location.href="${root}/order/toMyOrderXOrders.do";
	}
	//产品订单
	function myProOrder(){
		location.href="${root}/product/toMyOrderXProducts.do";
	}
</script>
<meta charset="UTF-8">
<title>我的订单</title>
<link rel="stylesheet" type="text/css" href="../orders/css/common.css">
<script type="text/javascript" src="<%=basePath%>orders/js/jquery.min.js"></script>
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
	<%@include file="/orders/header.jsp"%>
	<!-- 订单信息开始部分 -->
	<div class="contain order-object">
		<div class="object-left">订单信息</div>
		<div class="object-right">
			<button onclick="myProOrder();" style="line-height:30px">产品订单</button>
		</div>
		<div class="object-right">
			<button onclick="myCanyinOrder();" style="line-height:30px">餐饮订单</button>
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
						<div class="detail-top clearfix">
							<div class="detail-top-left">
								<!-- <div class="detail-top-left-1"><img src="" class="meal"></div> -->
								<div class="detail-top-lefta">
									<div class="p1"></div>
									<div class="p2">订购产品对象：<span>${myOrder.orderObj }</span></div>
								</div>
							</div>
							<div class="detail-top-middle">
								<div class="p1"></div>
								<div class="p2">下单时间：<span>${myOrder.orderTime }<fmt:formatDate value="${rec.ins_time}" pattern="yyyy-MM-dd HH:mm:ss" /> </span></div>
							</div>
							<c:if test="${myOrder.sendOutFlag == '0'}">
								<div class="detail-top-middleb">
									<div class="p1"></div>
									<div class="p2">快递单号：<span>${myOrder.expressNum }</span></div>
								</div>
							</c:if>
							<div class="detail-top-right">
								<c:if test="${ not empty myOrder.isShow}">
									<div class="p1 is_show"><button onclick="cancel_myOrder('${myOrder.id}','${myOrder.foodTypeNum}','${myOrder.orderTime }','${myOrder.orderType }');">取消订单</button></div>
								</c:if>
								<c:if test="${empty myOrder.isShow}">
									<div class="p1 is_hiden"><button>已完成</button></div>
								</c:if>
							</div>
						</div>
					<div class="detail-bottom-dizhi">
							<img   src="../products/images/weizhi.png">
							<span>收货地址 : </span>
							<span>${myOrder.xReceiverInfo.recArea } ${myOrder.xReceiverInfo.recDetailAddress } <span>${myOrder.xReceiverInfo.recName } ${myOrder.xReceiverInfo.recPhone }</span></span>
						</div>
					<div class="detail-bottom">
						
						<div class="detail-bottom-two detail-bottom-20 ">
							<ul class="clearfixa">
								<c:if test="${!empty myOrder.detailList}">
									<c:forEach items="${myOrder.detailList }" var="detail">
										<li class="clearfix clearfixa"  style="width: 100%;height: 84px;">
											<img width="100%" height="100%"  src="${root}${detail.pro_image_url}" class="clearfix_img">
											
											<span>${detail.pro_name }</span>
											<span>${detail.pro_num }*${detail.pro_price }</span>
										</li>
										
									</c:forEach>
								</c:if>
							</ul>
						</div>
						<div class="accountMsga clearfix">
							<ul>
								<li class="username">扣除账户：<span>${myOrder.orderObj}</span></li>
								<li class="pay">扣除余额：<span>${myOrder.moneyTotal}</span></li>
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
<%-- <link rel="stylesheet" href="${root }products/css/jquery.mloading.css" />
<script type="text/javascript" src="${root }products/js/jquery.min.js"></script>
<script src="${root }products/js/jquery.mloading.js" type="text/javascript"></script> --%>
<script type="text/javascript" src="../orders/js/msgbox.js"></script>
</html>