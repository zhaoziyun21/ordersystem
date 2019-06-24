<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>

<meta charset="UTF-8">
<title>我的现场订购</title>
<link rel="stylesheet" type="text/css" href="../orders/css/common.css">
<link rel="stylesheet" type="text/css" href="../orders/css/myorder.css">
<link rel="stylesheet" href="${root }wx_orders/css/jquery.mloading.css" />
<script src="${root }wx_orders/js/jquery.min.js" type="text/javascript"></script>
<script src="${root }wx_orders/js/jquery.mloading.js" type="text/javascript"></script>
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
	
	.detail-bottom-dizhi{
		margin: 22px 10px;
	}
	.detail-bottom-dizhi img{
		margin-right:5px;
		float: left;
	}
	.detail-bottom-dizhi span:nth-child(2){
		font-size: 14px;float: left;
	}
	.detail-bottom-dizhi span:nth-child(3){
		font-size: 14px;
		margin-left: 10px;
	}
	.detail-bottom-dizhi span:nth-child(3) span{
		margin-left: 0px;
	}
</style>


<style>
	.yi-box{
		position: fixed;
    	top: 0;
    	left: 0;
		width: 100%;
		height:100%;
		background: rgba(0,0,0,.1);
		display: none;
	}
	.yi-content{
		    width: 358px;
		    height: 475px;
		    background: #fff;
		    border-radius: 8px;
		    font-size: 18px;
		    margin: auto;
		    position: relative;
		    top: 78px;
		    left: 0;

	}
	.yiImg{
		width: 15px;
		height: 15px;
		position: absolute;
		top: -16px;
		left: 360px;
	}

		.yi-content ul li{
			display: block;
			width: 100%;
			height:55px;
			line-height:55px;
			border-bottom: 1px solid #e9e9e9;

		}
		.yi-content ul li:nth-child(1){
			height: 48px;
			line-height: 48px;
		}
		.yi-content ul li span{
			float: left;
			padding-left:22px;
		}
		.yi-content ul li b{
			float: right;
		    padding-right: 15px;
		    font-weight: 400;
		    width: 223px;
		    text-align: right;
		    overflow: hidden;
		    text-overflow: ellipsis;
		    white-space: nowrap;
		}
</style>
</head>
<body>

	<!-- 头部结束 -->
	<%@include file="header.jsp"%>
	<!-- 订单信息开始部分 -->
	<div class="contain order-object">
		<div class="object-left">现场订单信息</div>
	</div>
	<!-- 订单信息部分结束 -->
	
	<!-- 订餐详情部分开始 -->
	<div class="contain det">
		<ul>
		<c:if test="${ not empty myOrderList}">
			<c:forEach items="${myOrderList}" var="myOrder">
				<li>
					<div class="detail">
						<div style="margin-left: 12px;font-size: 14px;color: red;font-weight: 700;margin-top: 10px;">餐饮公司：<span>${myOrder.xUser.foodCompanyName }</span></div>
						
						<div class="detail-top clearfix">
							<div class="detail-top-left">
								<div class="detail-top-left-2">
									<div class="p1" style="float: left;margin-top: 22px;">${myOrder.foodNameStr }</div>
									<div class="p2" style="float: left;margin-top: 22px;margin-left:20px">订餐对象：<span>${myOrder.orderObj }</span></div>
								</div>
							</div>
							<div class="detail-top-middle">
								<div class="p1"></div>
								<div class="p2" style="margin-left: 137px;">订购时间：<span>${myOrder.orderTime }<fmt:formatDate value="${rec.ins_time}" pattern="yyyy-MM-dd HH:mm:ss" /> </span></div>
							</div>
							<div class="detail-top-right">
								<div class="p1" style=" float: left;"><button>已完成</button></div>
							</div>
						</div>
						
					<div class="detail-bottom" style="margin-top: 2px">
						<div class="detail-bottom-1">菜品共<pan class="detail-bottom-11">${myOrder.foodTypeNum}</pan>份，总计<span class="detail-bottom-12">${ myOrder.moenyTotal}</span>元</div>
						<div class="detail-bottom-2">
							<ul class="clearfix">
								<c:if test="${!empty myOrder.detailList}">
									<c:forEach items="${myOrder.detailList }" var="detail">
										<li class="clearfix" style="width: 200px">
											<!-- 缺少图片 -->
										<%-- 	<img width="100%" height="100%"  src="${root}${detail.food_img}" class="meal"> --%>
											<img width="100%" height="100%"  src="${root}${empty detail.food_img ? '/images/default.jpg': detail.food_img}" class="meal">
											<span>${detail.food_desc }</span>
											<p>${detail.food_num }*${detail.pay_price }</p>
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
	
	
	<div class="yi-box">
		
		<div class="yi-content">
			<img src="../orders/images/311898663450277931.png" alt="" class="yiImg">
			<ul id="mealTicketDetail">
				<!-- <li style="text-align: center;">订餐详情</li>
				<li>
					<span>取餐时间</span>
					<b>2018/09/06 11:40</b>
					<div style="clear: both;"></div>
				</li> -->
			</ul>
		</div>
	</div>
		
		
	
	
	<form id="returnCar" action="${root }order/toOrderingPageXOrders.do"  method="post">
		<input name="infos" type="hidden" id="infos" value=""/>
	</form>
</body>

</html>



<script>
	$(".yiImg").click(function(){

		$(".yi-box").css("display","none")
	})	
	
	function mealTicketBox(id){
		var html = "";
		$.ajax({
		    url:"${root}wechat/mealTicketSelectWeChat.do",  //请求的url地址
		    dataType:"json",  //返回格式为json
		    data:{"orderId":id},    
		    type:"POST",   //请求方式
		    success:function(data){
		    	var foodName = "";
		    	var foodNum = Number(0);
		    	for(var i=0;i<data.length;i++){
		    		if(i<data.length-1){
		    			foodName += data[i].food_name+",";
		    		}else if(i==data.length-1){
		    			foodName += data[i].food_name;
		    		}
		    		foodNum = Number(foodNum)+Number(data[i].food_num);
		    	}
		    
		    	var insTime = data[0].ins_time.split(" ")[0]+" 11:30-13:30";
		    	html += "<li style='text-align: center;'>饭票详情</li>";
		    	html += "<li><span>取餐时间</span><b>"+insTime+"</b></li>";
		    	html += "<li><span>取餐门店</span><b>"+data[0].foodBusiness+"</b></li>";
		    	html += "<li><span>套餐名称</span><b>"+foodName+"</b></li>";
		    	html += "<li><span>套餐份数</span><b>"+foodNum+"</b></li>";
		    	html += "<li><span>取餐人员</span><b>"+data[0].real_name+"</b></li>";
		    	html += "<li><span>所属公司</span><b>"+data[0].company_name+"</b></li>";
		    	html += "<li><span>所属部门</span><b>"+data[0].dept_name+"</b></li>";
		    	
		    	$("#mealTicketDetail").html(html);
		    }
		});
		
		$(".yi-box").css("display","block");
	}
	
</script>