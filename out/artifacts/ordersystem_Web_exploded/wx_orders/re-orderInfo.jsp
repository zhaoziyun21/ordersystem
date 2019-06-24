<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
 	 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>提交预定订单</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/adapter.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/re-orderInfo.css">
    <link rel="stylesheet" href="${root }wx_orders/css/jquery.mloading.css" />
	<script src="${root }wx_orders/js/jquery.min.js" type="text/javascript"></script>
	<script src="${root }wx_orders/js/jquery.mloading.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=basePath%>wx_orders/js/msgbox.js"></script>
    <script>
    Array.prototype.contains = function ( needle ) {
	  for (i in this) {
	    if (this[i] == needle) return true;
	  }
	  return false;
	}
	
	var weekArr = new Array();
	var totalMoney = 0;
	
	var recId = null; //收餐地址id
	/* 提交订单 */
	function submitOrder(){
		if(totalMoney == 0){
			 $.MsgBox.Alert("消息", "您还未预定套餐！"); 
			 return;
		}
		
		if("${foodCompany.whetherSendStatus}"=="0"){
			if(recId == null || recId == ""){
				$.MsgBox.Alert("消息", "您还未选择收餐地址！"); 
				return;
			}
		}
		
		/* $("#orderInfo").val(JSON.stringify(getInfo())); */
		var orderInfo = getInfo();
		var vistorName = '${vistorName}';
		var order = JSON.stringify(orderInfo.carDetail);
		var type = orderInfo.orderObj;
		var ldId = orderInfo.ldId;
		var time = orderInfo.time;
		//var sumNum = $("#pay").html()/25;//套餐总份数
		
		/* obj.mLoading({
  		    text:"", //加载文字，默认值：加载中...
  		    icon:"", //加载图标，默认值：一个小型的base64的gif图片
  		    html:false, //设置加载内容是否是html格式，默认值是false
  		    content:"", //忽略icon和text的值，直接在加载框中显示此值
  		    mask:true //是否显示遮罩效果，默认显示
  	    }); */
     	$("body").mLoading("show");//显示loading组件
		$.ajax({
		    url:"${root}/wechat/buyReserveFoodWeChat.do",    //请求的url地址
		    dataType:"json",   //返回格式为json
		    data:{
		    	"orderInfo":order,
		    	"type":type,
		    	"ldId":ldId,
		    	"vistorName":vistorName,
		    	"time":time,
		    	"recId":recId
			},    
		    type:"POST",   //请求方式
		    success:function(req){
		    	if(req.status == "false"){
		    		  $.MsgBox.Alert("消息", req.msg);
		    	}else{
			    	$.MsgBox.Alert("消息", "点餐成功",function(){
			    		$("body").mLoading("show");//显示loading组件
			        	location.href="${root}/wechat/toMyReserveOrderWeChat.do";
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
	
    $(function(){
    	var typeNew = "${type}";
		var leadIdNew = "${leadId}";
		var infosNew = "${infos}";
		var orderTypeNew = "${orderType}";
		var foodBusinessNew = "${foodBusiness}";
		var checkedBusiness = "${checkedBusiness}";
    
    	var orderInfoNew = JSON.stringify(${orderInfo});
		var vistorNameNew = "${vistorName}";
		
		$(".address img").click(function(){
			window.location.href="${root}order/getAddressListWx_YDXFoodSendAddress.do?orderInfo="+orderInfoNew+"&vistorName="+vistorNameNew+"&type="+typeNew+"&leadId="+leadIdNew+"&infos="+infosNew+"&orderType="+orderTypeNew+"&foodBusiness="+foodBusinessNew+"&checkedBusiness="+checkedBusiness;
		})
    	
	    /* 返回上一页 */
		$(".back").click(function(){
			window.location.href="${root}wechat/toSelfOrderPageWeChat.do?type="+typeNew+"&leadId="+leadIdNew+"&infos="+infosNew+"&orderType="+orderTypeNew+"&foodBusiness="+foodBusinessNew+"&vistorName="+vistorNameNew;
			//window.history.back();
		})
		
    	if("${!empty orderInfo}"){
    		var orderInfo = ${orderInfo};
			var json = eval(orderInfo);
    		//订餐对象(ZJ:自己、KR：客人、LD：领导、ZD：指定)
			var orderObj = json.orderObj;
			var ldId;
			if(orderObj == "LD"){
				//领导ID
				ldId = orderInfo.ldId;
			}
			//扣除账户
			$("#accName").text(json.obj.obj);
			//余额
			$("#accMoney").text(json.obj.balance);	
		
			var html = "";
			$("#append").html("");
			var carDetail = json.carDetail;
			if(carDetail.length > 0){
				for(var i = 0;i < carDetail.length; i ++){
					if(!weekArr.contains(carDetail[i]["weeknum"])){
						html += "<li name=\""+carDetail[i]["weeknum"]+"\" id=\"\">";
						html += "	<ul id=\"\">";
						html += "		<li  class=\"everyMeal\"  style='margin-left: 1.326667rem;   '>"+carDetail[i]["week"]+"</li>";
					}
					
					for(var j = 0;j < carDetail.length; j ++){
						if(carDetail[j]["weeknum"] == carDetail[i]["weeknum"] && !weekArr.contains(carDetail[i]["weeknum"])){
							html += "	<li>";
							html += "		<input type=\"hidden\" name=\"c_food_id\" value=\""+carDetail[j]["id"]+"\">";
							html += "		<input type=\"hidden\" name=\"c_sell_price\" value=\""+carDetail[j]["price"]+"\">";
							html += "		<div class=\"car-desc\">"+carDetail[j]["food"]+"</div>";
							html += "		<div class=\"car-num\">";
							html += "			<span class=\"reduce\"></span>";
							html += "			<span class=\"number\">"+carDetail[j]["num"]+"</span>";
							html += "			<span class=\"add\"></span>";
							html += "		</div>";
							html += "	</li>";
							totalMoney += Number(carDetail[j]["total"]);
						}
						
					}	
							
					if(!weekArr.contains(carDetail[i]["weeknum"])){
						html += "  </ul>";
						html += "</li>";
						weekArr.push(carDetail[i]["weeknum"]);
					}		
				}
				
				//
				$("#append").html(html);
				$(".totalPay").text("总计：￥"+totalMoney);
			}
    	}
    });
		
	</script>
</head>
<style>

#consignee_infor {
	-width:18rem;
	height: 3.08rem;
	padding: 0.16rem 1.54rem 0.46rem 1.28rem;
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
	        <div class="title">提交预定订单</div> 
	        <div class="back"></div>
        </div>
        <div class="accountMsg">
        	<div class="accountName">
        		账户：<span id="accName"></span>
        	</div>
        	<div class="accountBalance">
        		余额：<span id="accMoney"></span>
        	</div>
        </div>
        
        <div style="width:100%">
			<h3 style="display: block;width: 10rem;color: #333;font-weight: 600;font-size: 0.8rem;margin-top: 0.8rem;margin-bottom: 0.6rem;margin-left: 1.28rem;">餐饮公司：${foodCompany.foodCompanyName }</h3>
		</div>
        
        <c:choose>
        	<c:when test="${foodCompany.whetherSendStatus==0 }">
        		<!-- 收货人信息 -->
				<ul id="consignee_infor">
					<a href="javascript:;">
					<c:choose>
						<c:when test="${!empty xFoodSendAddressChecked }">
							<!-- 隐藏收货人信息id -->
							<li class="address">
								<input type="hidden" value="${xFoodSendAddressChecked.id }">
								<c:choose>
									<c:when test="${!empty xFoodSendAddressDefault && xFoodSendAddressDefault.id==xFoodSendAddressChecked.id }">
										<p><span>收餐地址：${xFoodSendAddressChecked.regionName } ${xFoodSendAddressChecked.park } ${xFoodSendAddressChecked.highBuilding } ${xFoodSendAddressChecked.unit } ${xFoodSendAddressChecked.roomNum }</span> <b class="default">默认</b></p>
									</c:when>
									<c:otherwise>
										<p style="line-height: 1.2rem;"><span>收餐地址：${xFoodSendAddressChecked.regionName } ${xFoodSendAddressChecked.park } ${xFoodSendAddressChecked.highBuilding } ${xFoodSendAddressChecked.unit } ${xFoodSendAddressChecked.roomNum }</span></p>
									</c:otherwise>
								</c:choose>
								<img src="<%=basePath%>wx_orders/images/jiantou_you.png">
							</li>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${!empty xFoodSendAddressDefault }">
									<!-- 隐藏收货人信息id -->
									<li class="address">
										<input type="hidden" value="${xFoodSendAddressDefault.id }">
										<p><span>收餐地址：${xFoodSendAddressDefault.regionName } ${xFoodSendAddressDefault.park } ${xFoodSendAddressDefault.highBuilding } ${xFoodSendAddressDefault.unit } ${xFoodSendAddressDefault.roomNum }</span> <b class="default">默认</b></p>
										<img src="<%=basePath%>wx_orders/images/jiantou_you.png">
									</li>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${!empty xFoodSendAddressList }">
											<c:forEach items="${xFoodSendAddressList }" var="fa" varStatus="status">
												<c:if test="${status.index==0 }">
													<!-- 隐藏收货人信息id -->
													<li class="address">
														<input type="hidden" id="addressOneId" value="${fa.id }">
														<p><span>收餐地址：${fa.regionName } ${fa.park } ${fa.highBuilding } ${fa.unit } ${fa.roomNum }</span></p>
														<img src="<%=basePath%>wx_orders/images/jiantou_you.png">
													</li>
												</c:if>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<li class="address">
												<p><span>收餐地址：无数据</span></p>
											</li>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
					</a>
				</ul>
        	</c:when>
        	<c:otherwise>
        		<div style="width:100%">
					<h3 style="display: block;width: 15.96rem;margin-left: 1.28rem;color: red;font-size: 0.7rem;margin-top: 0.8rem;margin-bottom: 0.8rem;">本餐饮公司不支持派送，需要凭证饭票到店取餐！</h3>
				</div>
        	</c:otherwise>
        </c:choose>
        
        <div class="buyCar">
    		<div class="buyCar-bottom">
    		 <ul id="append">
			</ul>
    	</div>
    </div>
     
    	<div class="footer">
    		<div class="footer-l">
    			<span class="totalPay">总计：￥65</span>
    		</div>
    		<div class="footer-r" onclick="submitOrder();">
    			提交订单
    		</div>
    		
    	</div>
    </div>
   <div id="mask"></div>
	
	<span style="display: none;" id="orderInfo">${orderInfo }</span>
	
	<form action="" method="post">
		<input id="orderInfo" name="orderInfo" value="" />
		<input type="hidden" id="vistorName" name="vistorName" value="${vistorName }" />
	</form>
	
</body>
<script type="text/javascript">
	$(function(){
		$(".reduce").off().on("click",function(){
			var s=$(this).siblings(".number").html();
			s--;
			totalMoney = Number(totalMoney)-Number($(this).parent().siblings("input[name='c_sell_price']").val());
			$(".totalPay").text("总计：￥"+totalMoney);
			if(s==0||s<0){
				if($(this).parent().parent().siblings().length==1||$(this).parent().parent().siblings().length<1){
					$(this).parent().parent().parent().parent().remove();
				};
				$(this).parent().parent().remove();
			}
			
			$(this).siblings(".number").html(s);
		})
		
		$(".add").off().on("click",function(){
			var s=$(this).siblings(".number").html();
			s++;
			totalMoney = Number(totalMoney)+Number($(this).parent().siblings("input[name='c_sell_price']").val());
			$(".totalPay").text("总计：￥"+totalMoney);
			$(this).siblings(".number").html(s);
		})
	})
	
	
//获取购物车信息
function getInfo(){
	//套餐信息
	var newOrderInfo = {};
	var orderInfo = $("#orderInfo").html();
	if(orderInfo != null && orderInfo != ""){
		var json = eval('('+orderInfo+')');
		newOrderInfo.orderObj = json.orderObj;
		if(json.orderObj == "LD"){
			newOrderInfo.ldId = json.ldId;
		}
		newOrderInfo.obj = json.obj;
		var carDetail = [];
		var names = [];
		$("#append>li").each(function(index,item){
			names.push($(item).attr("name"));
			var week = "";
			$(item).find("ul>li").each(function(ind,it){
				if($(it).attr("class") == "everyMeal"){
					week = $(it).text();
				}else{
					var detailOne = {};
					detailOne.week = week;
					detailOne.weeknum = $(item).attr("name");
					detailOne.id = $(it).find("input[name='c_food_id']").val();
					detailOne.price = $(it).find("input[name='c_sell_price']").val();
					detailOne.food = $(it).find("div.car-desc").text();
					detailOne.num = $(it).find("div.car-num").find("span.number").text();
					detailOne.total = Number(detailOne.price)*Number(detailOne.num);	
					carDetail.push(detailOne);
				
				}
			});
		});
		
		newOrderInfo.time = names.sort()[0];
		newOrderInfo.carDetail = carDetail;
	}
	return newOrderInfo;
}
		
	
	$(function(){
		//收餐地址id
		if("${xFoodSendAddressChecked}" != ""){
			recId = "${xFoodSendAddressChecked.id}";
		}else if("${xFoodSendAddressDefault}" != ""){
		    recId = "${xFoodSendAddressDefault.id}";
		}else if("${xFoodSendAddressList}" != ""){
			recId = $("#addressOneId").val();
		}
	})
	
	
</script>
</body>
</html>
