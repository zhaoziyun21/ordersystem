<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<script type="text/javascript">

	function cancel_myOrder(id,sendOutFlag,moneyTotal){
		if(sendOutFlag == "0"){
			$.MsgBox.Alert("取消订购产品", "产品已发货，不能取消订单");
		}else{
			$.MsgBox.Confirm("取消订购产品", "是否取消订单",function(){
				/* obj.mLoading({
				    text:"", //加载文字，默认值：加载中...
				    icon:"", //加载图标，默认值：一个小型的base64的gif图片
				    html:false, //设置加载内容是否是html格式，默认值是false
				    content:"", //忽略icon和text的值，直接在加载框中显示此值
				    mask:true //是否显示遮罩效果，默认显示
				   }); */
		    	//$("body").mLoading("show");//显示loading组件
				$.ajax({
				    url:"${root}/product/deleteOrderXProducts.do",    //请求的url地址
				    dataType:"json",   //返回格式为json
				    data:{
				    	"orderId":id,
				    	"moneyTotal":moneyTotal
				    	},    
				    type:"POST",   //请求方式
				    success:function(req){
				    	if(req.success == "false"){
				    		$.MsgBox.Alert("取消订购产品", "已经完成不能取消订单");
				    	}else{
				    		$.MsgBox.Alert("消息", "取消成功！",function(){
					    		//$("body").mLoading("show");//显示loading组件
				       			location.href="${root}/product/toMyReserveOrderXProducts.do";
					   		});
				    	}
				    	//$("body").mLoading("hide"); //隐藏loading组件
				    },
				    error:function(req){
				    	alert(req.msg);
				    	//$("body").mLoading("hide"); //隐藏loading组件
				    }
				}); 
			});
		}
			
	}
	
	function confirm_myOrder(id){
		$.MsgBox.Confirm("确认收货", "是否确认收货",function(){
			$.ajax({
			    url:"${root}product/upDateRecStatusXProducts.do",    //请求的url地址
			    dataType:"html",   //返回格式为json
			    data:{"orderId":id},    
			    type:"POST",   //请求方式
			    success:function(data){
			    	console.log(data)
			    	if(data == "Y"){
			    		$.MsgBox.Alert("消息", "手动确认收货成功！！！",function(){
			       			location.href="${root}product/toMyReserveOrderXProducts.do";
				   		});
			    	}else{
			       		$.MsgBox.Alert("消息", "手动确认收货失败！！！");
			    	}
			    }
			}); 
		});
			
	}
	
	//餐饮预定订单
	function myCanyinReserveOrder(){
		location.href="${root}/order/toMyReserveOrderXOrders.do";
	}
	//产品预定订单
	function myProReserveOrder(){
		location.href="${root}/product/toMyReserveOrderXProducts.do";
	}
</script>
<meta charset="UTF-8">
<title>我的预定</title>
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
	<%@include file="/orders/header.jsp"%>
	<!-- 订单信息开始部分 -->
	<div class="contain order-object">
		<div class="object-left">预定信息</div>
		<div class="object-right">
			<button onclick="myProReserveOrder();" style="line-height:30px">产品预定</button>
		</div>
		<div class="object-right">
			<button onclick="myCanyinReserveOrder();" style="line-height:30px;">餐饮预定</button>
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
							
							<!-- <div class="detail-top-left-1"><img src="" class="meal"></div> -->
								
							<div class="detail-top-lefta">
								<div class="p1"></div>
								<div class="p2">订购产品对象：<span>${myOrder.orderObj }</span></div>
							</div>
								
							<div class="detail-top-middle">
								<div class="p1"></div>
								<div class="p2">预定时间：<span>${myOrder.orderTime }<fmt:formatDate value="${rec.ins_time}" pattern="yyyy-MM-dd HH:mm:ss" /> </span></div>
							</div>
							<c:if test="${myOrder.sendOutFlag == '0'}">
								<div class="detail-top-middleb">
									<div class="p1"></div>
									<div class="p2">快递单号：<span>${myOrder.expressNum }</span></div>
								</div>
							</c:if>
							
							<div class="detail-top-right">
								<c:choose>
									<c:when test="${myOrder.sendOutFlag == '1'}">
										<div class="p1"><button onclick="cancel_myOrder('${myOrder.id}','${myOrder.sendOutFlag}','${myOrder.moneyTotal}');">取消订单</button></div>
									</c:when>
									<c:otherwise>
										<div class="p1"><button>已发货</button></div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						
						<div class="detail-bottom-dizhi">
							<img src="../products/images/weizhi.png">
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
								<li class="username" style="padding-top: 15px;">扣除账户：<span>${myOrder.orderObj}</span></li>
								<li class="pay" style="padding-top: 15px;">扣除余额：<span>${myOrder.moneyTotal}</span></li>
							</ul>
							<div class="detail-top-right">
								<c:if test="${myOrder.sendOutFlag == '0'}">
									<div class="p1"><button style="background-color: #FF8F33;color: #FFFFFF" onclick="confirm_myOrder('${myOrder.id}');">确认收货</button></div>
								</c:if>
							</div>
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
</html>