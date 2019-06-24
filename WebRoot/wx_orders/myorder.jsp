<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>我的正常订单</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/reset.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/adapter.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/myorder.css">
    <link rel="stylesheet" href="${root }wx_orders/css/jquery.mloading.css" />
	<script src="${root }wx_orders/js/jquery.min.js" type="text/javascript"></script>
	<script src="${root }wx_orders/js/jquery.mloading.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=basePath%>wx_orders/js/msgbox.js"></script>
    
</head>

<style>

	#consignee_infor {
	-width:18rem;
	height: 2.98rem;
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

<style>

		.yi_box{
			background: rgba(0,0,0,.3);
			width: 100%;
			height: 100%;
			padding: 8.64rem 2.34rem 0rem 2.34rem;
			position: fixed;
			top:0;
			left:0;
			display: none;
		}
		.yiImg{
			width: 0.8rem;
    		height: 0.8rem;
    		position: relative;
    		top: -0.2rem;
    		left: 15.1rem;
		}
		.yi-content{
			width: 100%;
			height: 20.52rem;
			background: #fff;
			border-radius: 4%;
			font-size: 0.8rem;

		}
		.yi-content>ul>li{
			display: block;
			width: 100%;
			height:2.4rem;
			line-height: 2.4rem;
			border-bottom: 1px solid #e9e9e9;

		}
		.yi-content>ul>li:nth-child(1){
			height: 2.1rem;
			line-height: 2.1rem;
		}
		.yi-content>ul>li span{
			float: left;
			padding-left: 1.22rem;
		}
		.yi-content ul li b{
			float: right;
   			padding-right: 1.15rem;
		    font-weight: 400;
		    display: block;
		    width: 10.5rem;
		    text-align: right;
		    overflow: hidden;
		    text-overflow: ellipsis;
		    white-space: nowrap;
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
				// $("#mb_con").css({"top": "230px"});
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
	    	$("body").mLoading("show");//显示loading组件
			$.ajax({
			    url:"${root}wechat/deleteOrderWeChat.do",    //请求的url地址
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
				    		$("body").mLoading("show");//显示loading组件
				        	location.href="${root}/wechat/toMyOrderWeChat.do";
				   		});
			    	}
		       		$("body").mLoading("hide");//隐藏loading组件
			    },
			    error:function(req){
			    	alert(req.msg);
			    	$("body").mLoading("hide");//隐藏loading组件
			    }
			}); 
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
           	<span>正常订单</span>
           		<div class="triangle"></div>
           		<ul>
           			<li onclick="window.location='${root}wechat/toMyOrderWeChat.do'"><span class="choose" >正常订单 </span><div class="choosed" ></div></li>
           			<li onclick="window.location='${root}wechat/toMyReserveOrderWeChat.do'"><span>预约订单</span><div ></div></li>
           			<li onclick="window.location='${root}wechat/toMyLiveOrderWeChat.do'" style="border: 1px solid #f1f1f1;"><span>现场订单</span><div ></div></li>
           			<li class="appoint" style="display: none;"onclick="window.location='${root}wechat/toAppointMyOrderWeChat.do'"><span>指定订单</span><div ></div></li>
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
						<h3 style="display: block;width: 10rem;color: #333;font-weight: 600;font-size: 0.8rem;margin-bottom: 0.4rem;margin-left: 1.06rem;">餐饮公司：${order.xUser.foodCompanyName }</h3>
					</div>
                   	
                   	<c:if test="${order.xUser.whetherSendStatus==0 }">
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
	                         <div class="object">订餐对象：${order.orderObj }</div>
                        </div>
                        
                        <div>
                            <div class="orderTime">${order.orderTime }</div>
                            <c:if test="${ not empty order.isShow}">
                            	<div class="isCancel" onclick="cancel_myOrder('${order.id}','${order.foodTypeNum}','${order.orderTime }','${order.orderType }');">取消订单</div>
                            </c:if>
                             <c:if test="${empty order.isShow}">
                            	<div class="isCancel">已完成</div>
                            </c:if>
                            <c:if test="${order.xUser.whetherSendStatus==1 }">
								<div class="isCancel mealTicket" onclick="mealTicketBox('${order.id}');"  style="width: 2.333333rem;margin-right: 0.5rem;">饭票</div>
							</c:if>
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
        
        <!-- 饭票弹框 -->
		<div class="yi_box">
			<img src="../wx_orders/images/yi-shanchu.png" alt="" class="yiImg">
			<div class="yi-content">
				<ul id="mealTicketDetail">
					<!-- <li style="text-align: center;">饭票详情</li> -->
					<!-- <li>
						<span>取餐时间</span>
						<b>2018/09/06 11:40</b>
					</li> -->
				</ul>
			</div>
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
<script>
	$(".yiImg").click(function(){
		$(".yi_box").css("display","none")
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
		
		$(".yi_box").css("display","block");
	}
	
</script>