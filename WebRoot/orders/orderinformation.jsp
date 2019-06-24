<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>订单信息</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>orders/css/common.css">
<link rel="stylesheet" type="text/css" href="${root }orders/css/receiveAddress.css">
<link rel="stylesheet" href="${root }wx_orders/css/jquery.mloading.css" />
<script type="text/javascript" src="<%=basePath%>orders/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>orders/js/orderinformation.js"></script>
<script src="${root }orders/js/jquery.mloading.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=basePath%>orders/js/msgbox.js"></script>
<script type="text/javascript" src="${root }products/js/msgbox.js"></script>
</head>
<body>
	<!-- 头部开始 -->
	<%@include file="header.jsp"%>
	<!-- 头部结束 -->

	<script>
	var totalMoney=0;
		$(function(){
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
					html += "<li class=\"shoppingcar-middle-bot\">";
					html += "	<span class=\"middle-bot-l\">"+carDetail[i].food+"</span>";
					html += "		<span class=\"middle-bot-r\"> ";
					html += "		<span class=\"jian\">-</span> ";
					html += "		<span class=\"number\">"+carDetail[i].num+"</span> ";
					html += "		<span class=\"jia\">+</span>";
					html += "       <input class=\"food_id\" type=\"hidden\" name=\"m_food_id\" value=\""+carDetail[i].id+"\"/>";		
					html += "       <input class=\"sell_price\" type=\"hidden\" name=\"m_sell_price\" value=\""+carDetail[i].price+"\"/>";		
					html += "		</span> ";
					html += "<span class=\"middle-bot-rr\">"+carDetail[i].total+"</span></li>";
				}
				$("#append").append(html);
				$("#totalNum").text(totalNum);//总份数
				$("#username").html(username);//扣除账户
				$("#pay").html(totalPrice);//扣除money
				$("#rest").html(Number(balance)); //扣除后余额
				$(".cost-top-2").html(totalPrice);
				totalMoney = totalNum * 25;
				//====
						/*加減套餐數量*/
						$(".jia").unbind('click').click(function(e){
							var s=parseInt($(this).siblings("span[class='number']").html());
							s++;
							
							$("#totalNum").text(Number($("#totalNum").text())+1);
							$("#pay").html(Number($("#pay").html())+Number($(this).siblings("input[name='m_sell_price']").val()));
							$(".cost-top-2").html(Number($(".cost-top-2").html())+Number($(this).siblings("input[name='m_sell_price']").val()));
							/* $("#rest").html(Number($("#rest").html())-25); */
							 $(this).parent().siblings("span[class='middle-bot-rr']").html(Number(s)*Number($(this).siblings("input[name='m_sell_price']").val())); 
							 totalMoney = Number(s)*25;
							
							$(this).siblings("span[class='number']").html(s);
					});
					$(".jian").unbind('click').click(function(){
						var n=parseInt($(this).siblings("span[class='number']").html());
						n--;
							$("#totalNum").text(Number($("#totalNum").text())-1);
							$("#pay").html(Number($("#pay").html())-Number($(this).siblings("input[name='m_sell_price']").val()));
							$(".cost-top-2").html(Number($(".cost-top-2").html())-Number($(this).siblings("input[name='m_sell_price']").val()));
							/* $("#rest").html(Number($("#rest").html())-25); */
							$(this).parent().siblings("span[class='middle-bot-rr']").html(Number(n)*Number($(this).siblings("input[name='m_sell_price']").val())); 
							totalMoney = Number(n)*25;
						if(n==0){
							$(this).parent().parent().remove();
							if($("#append li span[class='middle-bot-l']").length==0){
								$("#append .shoppingcar-middle-top").remove();
							};
						}
						$(this).siblings("span[class='number']").html(n);
					})
				
				//====
			}
		});
		
		var recId = null; //收餐地址id
		function dc_food(){
			if(totalMoney == 0){
				 $.MsgBox.Alert("消息", "您还未定套餐！"); 
				 return;
			};
			
			if("${foodCompany.whetherSendStatus}"=="0"){
				if(recId == null || recId == ""){
					$.MsgBox.Alert("消息", "您还未选择收餐地址！"); 
					return;
				}
			}
			
			var orderInfo = ${orderInfo};
			var vistorName = '${vistorName}';
			var json = eval(orderInfo);
			//var v = JSON.stringify(orderInfo.carDetail);
			var type = orderInfo.orderObj;
			var ldId = orderInfo.ldId;
			var ul=document.getElementById("append").getElementsByTagName("li");
			var carDetail = [];
            for(var i=1;i<ul.length;i++){
            	var orderInfo = {};
	            orderInfo.id = $(ul[i]).find('.food_id').val();//套餐
	            orderInfo.price = $(ul[i]).find('.sell_price').val();//套餐价格
	            orderInfo.food=$(ul[i]).find('.middle-bot-l').html();//套餐名称
	            orderInfo.num =$(ul[i]).find('.number').html();//套餐数量
	            orderInfo.total = ($(ul[i]).find('.middle-bot-rr').html())*Number(orderInfo.price)//
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
			    url:"${root}/order/buyFoodXOrders.do",    //请求的url地址
			    dataType:"json",   //返回格式为json
			    data:{
			    	"orderInfo":order,
			    	"type":type,
			    	"ldId":ldId,
			    	"vistorName":vistorName,
			    	"recId":recId
			    	},    
			    type:"POST",   //请求方式
			    success:function(req){
			    	if(req.status == "false"){
			    		$.MsgBox.Alert("消息", req.msg);
			    	}else{
				    	$.MsgBox.Alert("消息", "点餐成功",function(){
				    		$("body").mLoading("show");//显示loading组件
				        	location.href="${root}/order/toMyOrderXOrders.do";
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


	<!-- 订单信息开始部分 -->
	<div class="contain order-object">
		<div class="object-left">订单信息</div>
		<div class="object-right">
			<span><img src="${root }orders/images/mixed.png"></span> <span class="one">选择套餐</span>
			<span class="two">确认订单信息</span> <span class="three">成功提交订单</span>
		</div>

	</div>
	<!-- 订单信息部分结束 -->
	<span style="display:none;" id="orderInfo">${orderInfo}</span>
	<!-- 订单详情部分开始 -->
	<div class="contain roderdetails clearfix">
		<c:choose>
			<c:when test="${foodCompany.whetherSendStatus==0 }">
				<!-- 收货人信息开始 -->
				<div class="shouhuo">
					<h3   style=" padding: 20px 0px;margin-left: -15px;color: #333333;font-weight: 700;font-size: 14px;"> 收餐地址</h3>
					<div class="consignee-content">
						<ul id="aul">
							<c:if test="${!empty xFoodSendAddressDefault}">
								<li class="li">
									<!-- 隐藏收货人信息id -->
									<input type="hidden" value="${xFoodSendAddressDefault.id }">
									<em class="shouhuo_mo" id="shouhuo_moid" style="border: 1px solid rgb(228, 57, 60);"><span class="">${xFoodSendAddressDefault.roomNum }</span><b style="display:block;"></b></em>
									<div class="addr-detail">
										<span class="addr-name" limit="6" title="${xFoodSendAddressDefault.regionName }">${xFoodSendAddressDefault.regionName }</span>
										<span class="addr-info" limit="45" title="${xFoodSendAddressDefault.park }">${xFoodSendAddressDefault.park } ${xFoodSendAddressDefault.highBuilding } ${xFoodSendAddressDefault.unit } </span>
										<span class="addr-tel">${xFoodSendAddressDefault.roomNum }</span>
										<span class="addr-default">默认地址</span>
									</div>
								</li>
							</c:if>
							<c:if test="${!empty xFoodSendAddressList}">
								<c:forEach items="${allAvailableRegion }" var="region">
									<c:forEach items="${xFoodSendAddressList }" var="xFoodSendAddress">
										<c:if test="${region.id==xFoodSendAddress.regionId }">
											<li class="li">
												<!-- 隐藏收货人信息id -->
												<input type="hidden" value="${xFoodSendAddress.id }">
												<em class="shouhuo_mo" id="shouhuo_moid"><span class="">${xFoodSendAddress.roomNum }</span><b></b></em>
												<div class="addr-detail">
													<span class="addr-name" limit="6" title="${xFoodSendAddress.regionName }">${xFoodSendAddress.regionName }</span>
													<span class="addr-info" limit="45" title="${xFoodSendAddress.park }">${xFoodSendAddressDefault.park } ${xFoodSendAddress.highBuilding } ${xFoodSendAddress.unit } </span>
													<span class="addr-tel">${xFoodSendAddress.roomNum }</span>
												</div>
												<div class="op-btns" consigneeid="691073381" isoldaddress="false">
													<a href="#none" class="ftx-05 setdefault-consignee" ><center>设为默认地址</center></a>					
												</div>
											</li>
										</c:if>
									</c:forEach>
								</c:forEach>
							</c:if>
						</ul>
					</div>
				</div>
				<!-- 收货人信息结束 -->
			</c:when>
			<c:otherwise>
			<div style="width:100%">
				<h3 style="display:block;width:360px;margin:auto;margin-bottom:20px;color: red;">本餐饮公司不支持派送，需要凭证饭票到店取餐！</h3>
			</div>
				
			</c:otherwise>
		</c:choose>
		
		<h3 style=" padding-bottom: 20px;margin-left: 118px;color: #333333;font-weight: 700;font-size: 14px;"> 餐饮公司：${foodCompany.foodCompanyName }</h3>
		<div class="shoppingcar">
			<div class="shoppingcar-top">
				<span class="shoppingcar-top-l">订单详情</span> 
				<span onclick="returnCar();" class="shoppingcar-top-r">
				<img alt="" src="${root }orders/images/back.png">返回购物车修改</span>
			</div>
			
			<div class="shoppingcar-middle">
				<ul id="append">
					<li class="shoppingcar-middle-top"><span class="middle-top-l">套餐</span>
						<span class="middle-top-r">份数</span> <span class="middle-top-rr">小计（元）</span>
					</li>
				</ul>
			</div>
			<div class="cost">
				<div class="cost-top">
					总价：<span class="cost-top-1">￥</span><span class="cost-top-2"></span>
				</div>
				<div class="cost-bottom">共<label id="totalNum">0</label>份套餐</div>
			</div>
		</div>
		
		<!-- 账户信息 -->
		<div class="accountMsg clearfix">
			<ul>
				<li class="username">扣除账户：<span id="username"></span></li>
				<li class="pay">扣除余额：<span id="pay">0.00</span></li>
				<li class="rest">剩余：<span id="rest">0.00</span></li>
			</ul>
			<button id="confirmorder" onclick="dc_food();">确认订餐</button>
		</div>
	</div>
	
	<form id="returnCar" action="${root }order/toOrderingPageXOrders.do"  method="post">
		<input name="infos" type="hidden" id="infos" value=""/>
		<%-- <input id="vistorName" name="vistorName" value="${vistorName }" /> --%>
	</form>
</body>
<link rel="stylesheet" type="text/css" href="<%=basePath%>orders/css/orderinformation.css">
</html>

<script>	
	//收餐地址id
	if("${xFoodSendAddressDefault}" != ""){
	    recId = "${xFoodSendAddressDefault.id}";
	}
    
    //收货人选中样式
    if("${xReceiverInfoDefault.id}" != null && "${xReceiverInfoDefault.id}" != ""){
    	$("#shouhuo_moid").css("border","1px solid rgb(228, 57, 60)");
    	$("#shouhuo_moid b").css("display","block");
    	$("#adhide").css("display","none");
	}
	
	//点击选中红色框  删除消失  
	$('#aul').find('li em').click(function() {
		var index=$("#aul li em").index(this);
		$('#aul').find('li em').css("border","1px solid #ddd" );
        $('#aul').find('li em').eq(index).css("border","1px solid #e4393c" );
        $('#aul').find('li b').css('display', 'none');
        $('#aul').find('li b').eq(index).css('display', 'block');
        //css({"propertyname":"value","propertyname":"value",...});
        $('#aul').find('li .op-btns .hide').css({'display':'block',"float":"right"});
        $('#aul').find('li .op-btns .hide').eq(index).css('display', 'none');
        
        //获取选中的收货人id
       	recId = $(this).parent().find("input").val();
    })
    
    //移入字体背景变色 
    $('#aul li').mousemove(function(){
    	$(this).find(".op-btns").css("display","block")
    })
    //移入字体背景消失
    $('#aul ').find('li').mouseout(function(){
    	$(this).find(".op-btns").css("display","none")
    })
    
    $('#aul').find('li center').click(function() {
    	var acenter=$("#aul li center").index(this);
    	$('#aul').find('li center').css("display","block");
    	$('#aul').find('li center').eq(acenter).css("display","none");
    	$('#aul').find('li .addr-default').css("display","none");
    	$('#aul').find('li .addr-default').eq(acenter).css({"display":"block","float":"right"})
    })

	//设为默认地址
	$(".setdefault-consignee").click(function(){
		var id = $(this).parent().parent().find("input").val();
		$.ajax({
		    url:"${root }xuser/setUpAddressDefaultXUser.do", //请求的url地址
		    dataType:"json", //返回格式为json
		    data:{"addressId":id},    
		    type:"POST", //请求方式
		    success:function(data){
		    	if(data == true){
		    		$.MsgBox2.Alert("设为默认地址", "设置成功！");
		    	}else{
		       		$.MsgBox2.Alert("设为默认地址", "设置失败！");
		    	}
		    }
		});
	})
	
</script>