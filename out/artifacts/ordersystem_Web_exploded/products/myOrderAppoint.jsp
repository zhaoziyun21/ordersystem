<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<% 
request.setAttribute("currentTime",System.currentTimeMillis());
%>
<!DOCTYPE html>
<html lang="en">
<head>
<script type="text/javascript">
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
	<%@include file="/orders/header.jsp"%>
	<!-- 头部结束 -->
	
	<!-- 订单信息开始部分 -->
	<div class="contain order-object">
		<div class="object-left">指定信息</div>
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
							<div class="detail-top clearfix">
								<div class="detail-top-left">
									<div class="detail-top-lefta">
										<div class="p2">订单人：<span>${myOrder.orderName }</span></div>
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
									<c:if test="${myOrder.sendOutFlag == '1'}">
										<div class="p1"><button>未发货</button></div>
									</c:if>
									<c:if test="${myOrder.sendOutFlag == '0' && myOrder.recFlag == 1 && myOrder.sendOutTime != '' && currentTime < myOrder.sendOutTime + 24*60*60*1000*7}">
										<div class="p1"><button>已发货</button></div>
									</c:if>
									<c:if test="${myOrder.recFlag != 1 || (myOrder.sendOutFlag == '0' && myOrder.sendOutTime != '' && currentTime >= myOrder.sendOutTime + 24*60*60*1000*7) }">
										<div class="p1"><button>已完成</button></div>
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
</html>