<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<%
//String path = request.getContextPath();
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<title>地址管理</title>
		<meta name="renderer" content="webkit|ie-comp|ie-stand">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="user-scalable=no,width=device-width,initial-scale=1,maximum-scale=1">
		<link rel="stylesheet" type="text/css" href="${root }wx_orders/css/ad_common.css" />
		<link rel="stylesheet" type="text/css" href="${root }wx_orders/css/ad_header.css">
		<link rel="stylesheet" type="text/css" href="${root }wx_orders/css/Management_address.css" />
		<script type="text/javascript" src="${root }wx_orders/js/common.js"></script>
	</head>

	<body>
		<div id="container">
			<!-- 头部 -->
			<header style="background:#f5f5f5;background-color: #FF8F33;color: #FFFFFF;">
				<img class="back" src="${root }wx_orders/P-images/left.png" />
				<p class="title">地址管理</p>
			</header>
			<div class="content">
				<!--地址-->
				<ul class="list" id="list">
					<c:if test="${!empty xFoodSendAddressDefault}">
						<li>
							<!-- 隐藏收货人信息id -->
							<input type="hidden" value="${xFoodSendAddressDefault.id }">
							<span class="li_box">
								<span class="li_top">
									<p>${xFoodSendAddressDefault.roomNum }</p>
									<div style="display:block;">默认</div>
								</span>
								<text class="address">							
									<b>${xFoodSendAddressDefault.regionName } <b>${xFoodSendAddressDefault.park }</b> <b>${xFoodSendAddressDefault.highBuilding }</b> <b>${xFoodSendAddressDefault.unit }</b> <b>${xFoodSendAddressDefault.roomNum }</b></b>
								</text>
							</span>
							<span class="li_bottombox">							
								<text class="leftone">
									<img src="${root }wx_orders/images/moren.png" class="default_click" />
									<p>默认地址</p>
								</text>
							
							</span>
						</li>
					</c:if>
					
					<c:if test="${!empty xFoodSendAddressList}">
						<c:forEach items="${allAvailableRegion }" var="region">
							<c:forEach items="${xFoodSendAddressList }" var="xFoodSendAddress">
								<c:if test="${region.id==xFoodSendAddress.regionId }">
									<li>
										<!-- 隐藏收货人信息id -->
										<input type="hidden" value="${xFoodSendAddress.id }">
										<span class="li_box">
											<span class="li_top">
												<p>${xFoodSendAddress.roomNum }</p>	
												<div >默认</div>
											</span>
											<text class="address">							
												<b>${xFoodSendAddress.regionName } <b>${xFoodSendAddress.park }</b> <b>${xFoodSendAddress.highBuilding }</b> <b>${xFoodSendAddress.unit }</b> <b>${xFoodSendAddress.roomNum }</b></b>
											</text>
										</span>
										<span class="li_bottombox">						
											<text class="leftone">
												<img src="${root }wx_orders/images/z_check.png" class="default_click" />
												<p>默认地址</p>
											</text>
										</span>
									</li>
								</c:if>
							</c:forEach>
						</c:forEach>
					</c:if>
				</ul>
				
			</div>
		
		</div>
		
		<script type="text/javascript" src="${root }wx_orders/js/jquery-3.3.1.min.js" ></script>
		<script>
			$(function() {
				var typeNew = "${type}";
				var leadIdNew = "${leadId}";
				var infosNew = "${infos}";
				var orderTypeNew = "${orderType}";
				var foodBusinessNew = "${foodBusiness}";
				var checkedBusiness = "${checkedBusiness}";
			
				var orderInfoNew = JSON.stringify(${orderInfo});
				var vistorNameNew = "${vistorName}";
				
				//返回上一层
				$(".back").click(function(){
					window.location.href="${root}wechat/goReInfoWeChat.do?flag=0&orderInfo="+orderInfoNew+"&vistorName="+vistorNameNew+"&type="+typeNew+"&leadId="+leadIdNew+"&infos="+infosNew+"&orderType="+orderTypeNew+"&foodBusiness="+foodBusinessNew+"&checkedBusiness="+checkedBusiness;
					//window.history.back();
				});
				//设置默认地址
				$("#list").find("li img").click(function(){
					var index = $("#list li img").index(this);
					$('#list').find('li img').attr("src",'${root }wx_orders/images/z_check.png')
			        $('#list').find('li div').css('display', 'none');
			        $(this).attr("src",'${root }wx_orders/images/moren.png')
			        $('#list').find('li div').eq(index).css('display', 'block');
			        
			        var makeDefaultAddress = $(this).parent().parent().parent().find("input").val();
			        $.ajax({
					    url:"${root }xuser/setUpAddressDefaultWxXUser.do", //请求的url地址
					    dataType:"json", //返回格式为json
					    data:{"addressId":makeDefaultAddress},    
					    type:"POST", //请求方式
					    success:function(data){
					    	if(data == true){
					    		alert("设为默认地址", "设置成功！"); 
					    	}else{
					       		alert("设为默认地址", "设置失败！");
					    	}
					    }
					});
				})
				//点击地址，进行切换
				$("#list").find("li .li_box").click(function(){
					//alert($("#list li .li_box").index(this))
					var checkedAddress = $(this).parent().find("input").val();
					window.location.href="${root}wechat/goReInfoWeChat.do?flag=0&orderInfo="+orderInfoNew+"&vistorName="+vistorNameNew+"&checkedAddress="+checkedAddress+"&type="+typeNew+"&leadId="+leadIdNew+"&infos="+infosNew+"&orderType="+orderTypeNew+"&foodBusiness="+foodBusinessNew+"&checkedBusiness="+checkedBusiness;
				})
			})
		</script>		
	</body>
</html>
