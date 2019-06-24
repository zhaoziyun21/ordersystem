<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
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
			if(dayTime<=odderTime){
				q_my_order(id,num);
			}else{
				$.MsgBox.Alert("取消订餐", "已经超时不能取消订单");
				// $("#mb_con").css({"top": "230px"});
			}
		}else if(orderType == "DIN"){//下午
			if(dayTime<=odderTime){
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
		/* obj.mLoading({
		    text:"", //加载文字，默认值：加载中...
		    icon:"", //加载图标，默认值：一个小型的base64的gif图片
		    html:false, //设置加载内容是否是html格式，默认值是false
		    content:"", //忽略icon和text的值，直接在加载框中显示此值
		    mask:true //是否显示遮罩效果，默认显示
		   }); */
    	$("body").mLoading("show");//显示loading组件
		$.MsgBox.Confirm("取消订餐", "是否取消订单",function(){
			$.ajax({
			    url:"${root}/order/deleteOrderXOrders.do",    //请求的url地址
			    dataType:"json",   //返回格式为json
			    data:{
			    	"orderId":id,
			    	"foodNum":num,
			    	"orderType":"1"
			    	},    
			    type:"POST",   //请求方式
			    success:function(req){
			    	if(req.success == "false"){
			    		$.MsgBox.Alert("取消订餐", "已经完成不能取消订单");
			    	}else{
			    		$.MsgBox.Alert("消息", "取消成功！",function(){
				    		$("body").mLoading("show");//显示loading组件
				       		location.href="${root}/order/toMyReserveOrderXOrders.do";
				   		});
			    	}
			    	$("body").mLoading("hide"); //隐藏loading组件
			    },
			    error:function(req){
			    	alert(req.msg);
			    	$("body").mLoading("hide"); //隐藏loading组件
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
			//if(dayTime>=amSartTime && dayTime<= amEndTime){
			if(odderTime > dayTime ){
				ag_order(orderId,orderObj,flag,sunNum);
			}else{
				$.MsgBox.Alert("消息", "已经超时不能订餐");
				// $("#mb_con").css({"top": "230px"});
			}
		}else if(orderType == "DIN"){//下午
			//if(dayTime>=pmSartTime && dayTime<=pmEndTime){
			if(odderTime > dayTime ){
				ag_order(orderId,orderObj,flag,sunNum);
			}else{
				$.MsgBox.Alert("消息", "已经超时不能订餐");
				 //$("#mb_con").css({"top": "230px"});
			}
		}else{
			$.MsgBox.Alert("消息", "已经超时不能订餐");
			//$("#mb_con").css({"top": "230px"});
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
		<div class="object-left">预定订单信息</div>
		<div class="object-right">
			<button onclick="myProReserveOrder();" style="line-height:30px">产品预定</button>
		</div>
		<div class="object-right">
			<button onclick="myCanyinReserveOrder();" style="line-height:30px">餐饮预定</button>
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
						<div style="margin-left: 12px;font-size: 14px;color: red;font-weight: 700;margin-top: 10px;">餐饮公司：<span>${myOrder.xUser.foodCompanyName }</span></div>
						
						<div class="detail-top clearfix">
							<div class="detail-top-left">
								<!-- <div class="detail-top-left-1"><img src="" class="meal"></div> -->
								<div class="detail-top-left-2">
									<div class="p1" style="float: left;margin-top: 22px;">${myOrder.foodNameStr }</div>
									<div class="p2" style="float: left;margin-top: 22px;margin-left:20px">订餐对象：<span>${myOrder.orderObj }</span></div>
								</div>
							</div>
							<div class="detail-top-middle">
								<div class="p1"></div>
								<div class="p2" style="margin-left: 137px;">预定时间：<span>${myOrder.orderTime }<fmt:formatDate value="${rec.ins_time}" pattern="yyyy-MM-dd HH:mm:ss" /> </span></div>
							</div>
							<div class="detail-top-right">
								<c:if test="${myOrder.xUser.whetherSendStatus==1 }">
									<div class="p1 is_hiden" onclick="mealTicketBox('${myOrder.id}');" style=" float: left;margin-right: 25px;"><button>饭票</button></div>
								</c:if>
								<%-- <div class="p1"><button onclick="again_myOrder('${myOrder.orderTime }','${myOrder.orderObj}','${myOrder.flag}','${myOrder.id}','${ myOrder.foodTypeNum*25}','${myOrder.orderType }');">再来一单</button></div> --%>
								<div class="p1" style=" float: left;"><button onclick="cancel_myOrder('${myOrder.id}','${myOrder.foodTypeNum}','${myOrder.orderTime }','${myOrder.orderType }');">取消订单</button></div>
							</div>
						</div>
						
						<c:if test="${myOrder.xUser.whetherSendStatus==0 }">
							<div class="detail-bottom-dizhi" style="margin-top: 0px; margin-bottom: 28px;">
								<img src="../orders/images/weizhi.png">
								<span>收餐地址 : </span>
								<span><span>${myOrder.xFoodSendAddress.regionName }</span> ${myOrder.xFoodSendAddress.park } ${myOrder.xFoodSendAddress.highBuilding } ${myOrder.xFoodSendAddress.unit } ${myOrder.xFoodSendAddress.roomNum }</span>
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
		    	
		    	if(data[0].real_name == data[0].vistor_name){
		    		html += "<li><span>取餐人员</span><b>"+data[0].real_name+"</b></li>";
		    	}else{
		    		html += "<li><span>取餐人员</span><b>"+data[0].real_name+"(为"+data[0].vistor_name+")"+"</b></li>";
		    	}
		    	
		    	html += "<li><span>所属公司</span><b>"+data[0].company_name+"</b></li>";
		    	html += "<li><span>所属部门</span><b>"+data[0].dept_name+"</b></li>";
		    	
		    	$("#mealTicketDetail").html(html);
		    }
		});
		
		$(".yi-box").css("display","block");
	}
	
</script>