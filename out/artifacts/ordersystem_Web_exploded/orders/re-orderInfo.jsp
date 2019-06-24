
<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html lang="en">

<head>
    <meta charset="UTF-8">
<title>订单信息</title>
<link rel="stylesheet" type="text/css" href="${root }orders/css/common.css">
<link rel="stylesheet" type="text/css" href="${root }orders/css/receiveAddress.css">
<link rel="stylesheet" type="text/css" href="${root }orders/css/re-orderInfo.css">
<link rel="stylesheet" href="${root }wx_orders/css/jquery.mloading.css" />
<script type="text/javascript" src="${root }orders/js/jquery.min.js"></script>
<script type="text/javascript" src="${root }orders/js/jquery.mloading.js" ></script>
<script type="text/javascript" src="${root }orders/js/re-orderInfo.js"></script>
<script type="text/javascript" src="${root }products/js/msgbox.js"></script>
</head>
<script type="text/javascript">
 Array.prototype.contains = function ( needle ) {
	  for (i in this) {
	    if (this[i] == needle) return true;
	  }
	  return false;
	}
	
	var weekArr = new Array();
	var totalMoney = 0;
	var totalNum = 0;
	$(function(){
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
			html+='<li class="shoppingcar-middle-top"><span class="middle-top-l">套餐</span>';
			html+='<span class="middle-top-r">份数</span> <span class="middle-top-rr">小计（元）</span>';
			html+='</li>';
			var carDetail = json.carDetail;
			if(carDetail.length > 0){
				for(var i = 0;i < carDetail.length; i ++){
						if(!weekArr.contains(carDetail[i]["weeknum"])){
							html += '<li name="'+carDetail[i]["weeknum"]+'">';
							html += '	<ul  >';
							html += '		<li  class="everyMeal">'+carDetail[i]["week"]+'</li>';
						}
						for(var j = 0;j < carDetail.length; j ++){
							if(carDetail[j]["weeknum"] == carDetail[i]["weeknum"] && !weekArr.contains(carDetail[i]["weeknum"])){
							
								html += '<li class="shoppingcar-middle-bot">';
								html += "<input type=\"hidden\" name=\"c_food_id\" value=\""+carDetail[j]["id"]+"\">";
								html += "<input type=\"hidden\" name=\"c_sell_price\" value=\""+carDetail[j]["price"]+"\">";
								html += '<span class="middle-bot-l">'+carDetail[j]["food"]+'</span>';
								html += ' <span class="middle-bot-r"> ';
								html += '<span class="jian">-</span> ';
								html += '<span	class="number">'+carDetail[j]["num"]+'</span>';
								html += '<span class="jia">+</span>';
								html += '</span>';
								html += '<span class="middle-bot-rr">'+carDetail[j]["total"]+'</span>';
								html += '</li>';
								totalMoney += Number(carDetail[j]["total"]);
								totalNum += Number(carDetail[j]["num"]);
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
				$(".cost-top-2").text(totalMoney);
				$("#totalNum").text(totalNum);
			}
    	}
    });
    
    var recId = null; //收餐地址id
    /* 提交订单 */
	function submitOrder(){
		if(totalMoney == 0){
			 $.MsgBox.Alert("消息", "您还未预定套餐！"); 
			 return;
		};
		
		if("${foodCompany.whetherSendStatus}"=="0"){
			if(recId == null || recId == ""){
				$.MsgBox.Alert("消息", "您还未选择收餐地址！"); 
				return;
			}
		}
		
		$("#mask").css("display","block");
		$("#confirmorder").css("disabled",true);
		//$("#orderInfo").val(JSON.stringify(getInfo()));
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
		    url:"${root}order/buyReserveFoodXOrders.do",    //请求的url地址
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
	    		// alert(req.msg);
	    		$("#mask").hide();
		    	if(req.status == "false"){
		    		  $.MsgBox.Alert("消息", req.msg);
		    	}else{
			    	$.MsgBox.Alert("消息", "点餐成功",function(){
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
	}
	
	$(function(){
	/*加減套餐數量*/
		$(".jia").unbind('click').click(function(e){
			var s=parseInt($(this).siblings("span[class='number']").html());
			s++;
			totalMoney += Number($(this).parent().siblings("input[name='c_sell_price']").val()); //$(this).parent().siblings("input[name='c_sell_price']").val()
			totalNum+=1;
			$(this).siblings("span[class='number']").html(s);
			$(".cost-top-2").text(totalMoney);
			$("#totalNum").text(totalNum);
			
			$(this).parent().siblings("span[class='middle-bot-rr']").html(Number(s)*Number($(this).parent().siblings("input[name='c_sell_price']").val())); 
	});
	$(".jian").unbind('click').click(function(){
		var n=parseInt($(this).siblings("span[class='number']").html());
		n--;
		totalMoney -= Number($(this).parent().siblings("input[name='c_sell_price']").val());
		totalNum-=1;
		if(n==0||n<0){
			if($(this).parent().parent().siblings().length==1||$(this).parent().parent().siblings().length<1){
				$(this).parent().parent().parent().parent().remove();
			}
			$(this).parent().parent().remove();
			if($("#append li span[class='middle-bot-l']").length==0){
				$("#append .shoppingcar-middle-top").remove();
			};
		}
		$(".cost-top-2").text(totalMoney);
		$("#totalNum").text(totalNum);
		$(this).siblings("span[class='number']").html(n);
		
		$(this).parent().siblings("span[class='middle-bot-rr']").html(Number(n)*Number($(this).parent().siblings("input[name='c_sell_price']").val())); 
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
		var newCarDetail = [];
		var times=[];
		$("#append>li.shoppingcar-middle-top").siblings().each(function(index,item){
			var week="";
			var weekname="";
			
			if($(item).find('ul li').attr('class')=='everyMeal'){
				week=$(item).attr('name');
				weekname=$(item).find('ul li').html();
				times.push(week);
			}
			$(item).find('ul li.everyMeal').siblings().each(function(index,item){
				var detailOne = {};
				detailOne.week = weekname;	
				detailOne.weeknum = week;	
				detailOne.id = $(item).find("input[name='c_food_id']").val();			//id
				detailOne.price = $(item).find("input[name='c_sell_price']").val();		//套餐价格
				detailOne.food = $(item).find("span.middle-bot-l").html();				//套餐名（描述）
				detailOne.num = $(item).find("span.middle-bot-r  span.number").html();	//数量
				detailOne.total = Number(detailOne.price)*Number(detailOne.num);								//小计
				newCarDetail.push(detailOne);
			});
		});
		newOrderInfo.time=times.sort()[0];
		newOrderInfo.carDetail = newCarDetail;
	}
	return newOrderInfo;
}
		
	
	
	
	
</script>
<body>
	<!-- 头部开始 -->
	 <%@include file="header.jsp"%>
	<!-- 头部结束 -->

	<!-- 订单信息开始部分 -->
	<div class="contain order-object">
		<div class="object-left">预约订单信息</div>
		<div class="object-right">
			<span><img src="${root }orders/images/mixed.png"></span> <span class="one">选择套餐</span>
			<span class="two">确认订单信息</span> <span class="three">成功提交订单</span>
		</div>

	</div>
	<!-- 订单信息部分结束 -->

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
										<span class="addr-info" limit="45" title="${xFoodSendAddressDefault.park }">${xFoodSendAddressDefault.highBuilding } ${xFoodSendAddressDefault.unit } </span>
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
													<span class="addr-info" limit="45" title="${xFoodSendAddress.park }">${xFoodSendAddress.highBuilding } ${xFoodSendAddress.unit } </span>
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
				<span class="shoppingcar-top-l">订单详情</span> <span
					class="shoppingcar-top-r">
<!-- 					<img alt=""	src="${root }orders/images/back.png">返回购物车修改</span> -->
			</div>
			<div class="shoppingcar-middle">
				<ul id="append">
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
				<li class="username">扣除账户：<span id="accName"></span></li>
				<li class="pay">扣除余额：<span class="cost-top-2"></span></li>
				<li class="rest">剩余：<span id="accMoney"></span></li>
			</ul>
			<button id="confirmorder" onclick="submitOrder();">确认订餐</button>
			
			<span style="display: none;" id="orderInfo">${orderInfo }</span>
	
	<form action="" method="post">
		<input id="orderInfo" name="orderInfo" value="" />
		<%-- <input id="vistorName" name="vistorName" value="${vistorName }" /> --%>
	</form>
		</div>
	</div>
	<div id="mask"></div>
</body>
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
