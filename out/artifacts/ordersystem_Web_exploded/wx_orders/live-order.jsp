<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>现场订餐</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no" />
    <link rel="stylesheet" type="text/css" href="${root }wx_orders/css/adapter.css">
    <link rel="stylesheet" type="text/css" href="${root }wx_orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="${root }wx_orders/css/self-order.css">
    <script type="text/javascript" src="${root }wx_orders/js/jquery.min.js"></script>
    <script type="text/javascript" src="${root }wx_orders/js/live-order.js"></script>
    <script type="text/javascript" src="<%=basePath%>wx_orders/js/msgbox.js"></script>
</head>

<style>
	.clearfixa{
		width:100%;
		height: 1.48rem;
	    font-size: 0.7rem;
	    box-sizing: border-box;
	    line-height: 1.48rem;
	    border: 1px solid #EEEEEE;
	    font-family: PingFang-SC-Medium;
	    color: #999999;
	    float: left;
	    text-align: center;
	}
	
	.clearfixa>span{
		
		text-align: center;
		line-height: 1.48rem;
		 box-sizing: border-box;
		  width: 100%;
		  height: 100%;
		  
		 
	}
	.clearfixa ul{
	width: 50%;	
    font-family: PingFang-SC-Medium;
    margin: 0 auto;
    position: relative;
    z-index: 9999;
    background-color: #FFFFFF;
    display: none;
    text-align: left;

	}
	.clearfixa ul li{
		line-height: 1.48rem;
		text-align: center;
		position: relative;
	}
	.choose {
	color:#FF8F33;
}
.choosed {
	background:url(../wx_orders/P-images/success.png) no-repeat center;
	background-size:100%;
}

.clearfixa li div {
    width: 0.7rem;
    height: 0.7rem;
    border: 1px solid #F2F2F2;
    border-radius: 50%;
    box-sizing: border-box;
    position: absolute;
    top: 0.4rem;
    right: 1.54666667rem;

}
#maska {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 96%;
    background: #000;
    opacity: 0.4;
    filter: alpha(opacity=40);
    display: none;
    margin-top: 3.4266666667rem;
}
.choosea {
    color: #FF8F33;
}

.chooseda {
    background: url(../wx_orders/P-images/success.png) no-repeat center;
    background-size: 100%;
}
</style>
<script type="text/javascript">
	
	$(function(){
		$(".clearfixa>span").click(function(){
			$("#maska").css("display","block");
			$(".clearfixa ul").css("display","none");
			$(".clearfixa ul").slideDown("normal");
		
		}) 
		
		var typeNew = "${type}";
		var leadIdNew = "${leadId}";
		var infosNew = "${infos}";
		var orderTypeNew = "${orderType}";
		var foodBusinessNew = "${foodBusiness}";
		var vistorNameNew = "${vistorName}";
		$("#checkedBusiness").val($("#defaultFoodBusiness").val());
		$(".clearfixa li").click(function(){
			$(".clearfixa li span").removeClass();
			$(".clearfixa li div").removeClass();
			$(this).find("span").addClass("choosea");
			$(this).find("div").addClass("chooseda");
			$(".clearfixa>span").html($(this).find("span").html());
			var s=$(this).find("span")[0].id;
			$(".clearfixa>span").removeClass();
			$(".clearfixa>span").addClass(s);
			$("#maska").css("display","none");
			$(".clearfixa ul").css("display","none");
			$(".buyCar").css("display","none");
			
			$("#checkedBusiness").val($(this).find("input").val());
			
			//切换不同的餐饮公司，展示不同的餐饮公司餐品
			window.location.href = "${root}wechat/toSelfOrderPageWeChat.do?foodBusiness="+$(this).find("input").val()+"&type="+typeNew+"&leadId="+leadIdNew+"&infos="+infosNew+"&orderType="+orderTypeNew+"&vistorName="+vistorNameNew;
			
		});
	
	})
</script>
<body>
	<div id="maska"></div>
    <div>
        <div class="header">
	        <div class="title">现场订餐</div> 
	        <div class="back"></div>
        </div>
        
         <div class="clearfixa">
         	<c:if test="${!empty foodBusinessfirst }">
	       		<span class="lun_1a">${foodBusinessfirst.foodCompanyName }</span><b><img alt=""style="width:0.8rem;height:0.4rem;padding-left:0.1rem; background-size: 8%;" src="../wx_orders/P-images/999999.png"> </b>
	        </c:if>
	        <ul>
	        	<c:if test="${!empty foodBusinessfirst }">
	        		<li>
	        			<input id="defaultFoodBusiness" type="hidden" value="${foodBusinessfirst.userName }">
		        		<span class="choosea"><b>${foodBusinessfirst.foodCompanyName }</b></span>
		        		<div class="chooseda"></div>
	        		</li>
	        	</c:if>
	        	<c:if test="${!empty foodBusinessList }">
	        		<c:forEach items="${foodBusinessList }" var="fb">
	        			<c:if test="${fb.userName != foodBusinessfirst.userName }">
	        				<li>
		        				<input type="hidden" value="${fb.userName }">
		        				<span ><b>${fb.foodCompanyName }</b></span>
		        				<div></div>
		        			</li>
	        			</c:if>
	        		</c:forEach>
	        	</c:if>
        	</ul>	
        </div>
        
        <div style="clear: both;"></div>
        <!-- 今日公告 -->
		<c:if test="${!empty notice && notice!='no' }">
			<div style="width:100%;">
				<c:forEach items="${noticeList }" var="nt" varStatus="status">
					<c:if test="${status.index == 0 }">
						<c:if test="${nt.ft=='LUN' }">
							<h2 style="display:block;width:4.8rem;margin:auto;margin-top:0.4rem ;font-size:0.8rem;font-weight: 600;color: #FF8F33;margin-bottom: 0.2rem;">今日午餐公告</h2>
						</c:if>
						<c:if test="${nt.ft=='DIN' }">
							<h2 style="display:block;width:4.8rem;margin:auto;margin-top:0.4rem ;font-size:0.8rem;font-weight: 600;color: #FF8F33;margin-bottom: 0.2rem;">今日晚餐公告</h2>
						</c:if>
					</c:if>
					<h3 style="padding: 0 0.8rem;font-size:0.7rem;text-indent:1.4rem;color:#666">${nt.notice_desc }</h3>
				</c:forEach>	
			</div>
		</c:if>
        
        <div>
    		<ul>
            <c:if test="${!empty map }">
				<c:forEach items="${map.foodInfos }" var="food">	
					<li class="eve-meal">
                  			<div class="clearfix total-meal">
			                    <div class="meal">
			                       <img style="height: 100%;width:100%" src="${root}${empty food.food_img ? '/images/default.jpg': food.food_img}"/>
			                    </div>
			                    <div class="meal-det">
			                        <div class="meal-det-top clearfix">
			                            <div class="mealName">${food.food_name }</div>
			                        </div>
			                        <div class="clearfix">
				                        <div class="total">${food.food_desc }</div>
			                        </div>
			                        <div style="margin-bottom: 0.2rem;">
										套餐价格￥<span class="priceClass">${food.sell_price }</span>
			                        </div>
			                        <div>
			                            <div class="jia"></div>
			                        </div>
			                    	<input type="hidden" name="food_id" value='${food.food_id}'>
			                    </div>
                 			 </div>
                  		</li>
					</c:forEach>
				</c:if>
				<c:if test="${empty map.foodInfos }">
					<h2 style="padding: 0 0.8rem;font-size:0.7rem;text-indent:1.4rem;color:red;margin-top: 1rem;">亲爱的客户，还没到订餐时间！！！</h2>
				</c:if>
            </ul>
    	</div>
    <div style="height:2.613333333rem"></div>
    	<div class="buyCar">
    			<div class="buyCar-top">
    				<div class="buyCar-top-l">购物车</div>
    				<div class="buyCar-top-r">清空</div>
    			</div>
    			<div class="buyCar-bottom">
    				<ul id="append">
    					
    				</ul>
    			</div>
    	</div>
    	<div class="footer">
    		<div class="footer-l">
    			<div class="car">
    				<div>
    					<div class="totalNum">
    						0
    					</div>
    					<input type="hidden" class="totalPrice" value="0">
    				</div>
    			</div>
    			<span class="totalPay">选点什么吧！</span>
    		</div>
    		<div class="footer-r">
    			去结算
    		</div>
    		
    	</div>
    </div>
   <div id="mask"></div>
	<form name="myForm" id="myForm" action="${root }wechat/goLiveSumWeChat.do"
		method="POST">
		<input type="hidden" name="orderInfo" id="orderInfo" value="">
		<input type="hidden" name="vistorName" id="vistorName" value="${vistorName }">
		<input type="hidden" name="type" id="type" value="${type }">
		<input type="hidden" name="leadId" id="leadId" value="${leadId }">
		<input type="hidden" name="infos" id="infos" value="${infos }">
		<input type="hidden" name="orderType" id="orderType" value="${orderType }">
		<input type="hidden" name="foodBusiness" id="foodBusiness" value="${foodBusiness }">
		<input type="hidden" name="checkedBusiness" id="checkedBusiness">
	</form>
</body>
</html>
