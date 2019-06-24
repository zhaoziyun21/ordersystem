
<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html lang="en">

<head>
    <meta charset="UTF-8">
<title>订单信息</title>
<link rel="stylesheet" href="${root }orders/css/jquery.mloading.css" />
<link rel="stylesheet" type="text/css" href="${root }products/css/common.css">
<link rel="stylesheet" type="text/css" href="${root }products/css/re-orderInfo.css">
<link href="${root }products/css/city-picker.css" rel="stylesheet" type="text/css" />
<link href="${root }products/css/city-picker.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${root }orders/js/jquery.min.js"></script>
<script src="${root }orders/js/jquery.mloading.js" type="text/javascript"></script>
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
    		//订餐对象(ZJ：自己、KR：客人、LD：领导、ZD：指定)
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
			html+='<li class="shoppingcar-middle-top"><span class="middle-top-l">产品</span>';
			html+='<span class="middle-top-r">件数</span> <span class="middle-top-rr">小计（元）</span>';
			html+='</li>';
			var carDetail = json.carDetail;
			if(carDetail.length > 0){
				for(var i = 0;i < carDetail.length; i ++){
					if(!weekArr.contains(carDetail[i]["weeknum"])){
						html += '<li name="'+carDetail[i]["weeknum"]+'">';
						html += '<ul>';
						html += '<li class="everyMeal">'+carDetail[i]["week"]+'</li>';
					}
					for(var j = 0;j < carDetail.length; j ++){
						if(carDetail[j]["weeknum"] == carDetail[i]["weeknum"] && !weekArr.contains(carDetail[i]["weeknum"])){
							html += '<li class="shoppingcar-middle-bot">';
							html += "<input type=\"hidden\" name=\"c_pro_code\" value=\""+carDetail[j]["id"]+"\">";
							html += "<input type=\"hidden\" name=\"c_pro_price\" value=\""+carDetail[j]["price"]+"\">";
							html += '<span class="middle-bot-l">'+carDetail[j]["product"]+'</span>';
							html += '<span class="middle-bot-r"> ';
							html += '	<span class="jian">-</span> ';
							html += '	<span class="number">'+carDetail[j]["num"]+'</span>';
							html += '	<span class="jia">+</span>';
							html += '</span>';
							html += '<span class="middle-bot-rr">'+parseInt(carDetail[j]["num"]*carDetail[j]["price"])+'</span>';
							html += '</li>';
							
							totalMoney += parseInt(carDetail[j]["total"]);
							totalNum += Number(carDetail[j]["num"]);
						}
							
					}	
								
					if(!weekArr.contains(carDetail[i]["weeknum"])){
						html += "</ul>";
						html += "</li>";
						weekArr.push(carDetail[i]["weeknum"]);
					}		
				}
				
				$("#append").html(html);
				$(".cost-top-2").text(totalMoney);
				$("#totalNum").text(totalNum);
			}
    	}
    });
    
    $(function(){
		//“+”
		$(".jia").unbind('click').click(function(e){
			var s=parseInt($(this).siblings("span[class='number']").html());
			s++;
			totalMoney += parseInt($(this).parent().parent().find("input[name='c_pro_price']").val());
			totalNum += 1;
			$(this).siblings("span[class='number']").html(s);
			$(this).parent().parent().find("span.middle-bot-rr").text(parseInt($(this).parent().parent().find("span.middle-bot-rr").text())+parseInt($(this).parent().parent().find("input[name='c_pro_price']").val()));
			$(".cost-top-2").text(parseInt(totalMoney));
			$("#totalNum").text(totalNum);
		});
		//“-”
		$(".jian").unbind('click').click(function(){
			var n=parseInt($(this).siblings("span[class='number']").html());
			n--;
			totalMoney -= parseInt($(this).parent().parent().find("input[name='c_pro_price']").val());
			totalNum -= 1;
			if(n==0||n<0){
				if($(this).parent().parent().siblings().length==1||$(this).parent().parent().siblings().length<1){
					$(this).parent().parent().parent().parent().remove();
				}
				$(this).parent().parent().remove();
				if($("#append li span[class='middle-bot-l']").length==0){
					$("#append .shoppingcar-middle-top").remove();
				};
			}
			$(this).parent().parent().find("span.middle-bot-rr").text(parseInt($(this).parent().parent().find("span.middle-bot-rr").text())-parseInt($(this).parent().parent().find("input[name='c_pro_price']").val()));
			$(".cost-top-2").text(parseInt(totalMoney));
			$("#totalNum").text(totalNum);
			$(this).siblings("span[class='number']").html(n);
		})
	})
    
  	var recId = null; //收货人id
    //提交订单
	function submitOrder(){
		if(totalMoney == 0){
			$.MsgBox.Alert("消息", "您还未预定套餐！"); 
			return;
		};
		if(recId == null || recId == ""){
			$.MsgBox.Alert("消息", "您还未添加收货人地址！"); 
			return;
		}
		//$("#mask").css("display","block");
		$("#confirmorder").css("disabled",true);
		var orderInfo = getInfo();
		var vistorName = '${vistorName}';
		var order = JSON.stringify(orderInfo.carDetail);
		var type = orderInfo.orderObj;
		var ldId = orderInfo.ldId;
		var time = orderInfo.time;
		
		$.ajax({
		    url:"${root}product/buyReserveProductsXProducts.do",    //请求的url地址
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
		    	$("#mask").hide();
		    	if(req.status == "false"){
					$.MsgBox.Alert("消息", req.msg);
		    	}else{
		    		$.MsgBox.Alert("消息", "订购产品成功",function(){
		        		location.href="${root}/product/toMyReserveOrderXProducts.do";
		   			});
		    	}
		    },
		    error:function(req){
		    	//alert(req.msg);
		    }
		});
	}
	
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
					detailOne.id = $(item).find("input[name='c_pro_code']").val();			//id
					detailOne.product = $(item).find("span.middle-bot-l").html();			//产品名（描述）
					detailOne.num = $(item).find("span.middle-bot-r  span.number").html();	//数量
					detailOne.total = $(item).find("input[name='c_pro_price']").val()*Number(detailOne.num);								//小计
					newCarDetail.push(detailOne);
				});
			});
			newOrderInfo.time=times.sort()[0];
			newOrderInfo.carDetail = newCarDetail;
		}
		return newOrderInfo;
	}
		
</script>
<body style="width:100%;position: relative">
	<!-- 头部开始 -->
	<%@include file="/orders/header.jsp"%>
	<!-- 头部结束 -->
	 
	<!-- 订单信息开始部分 -->
	<div class="contain order-object">
		<div class="object-left">产品订单信息</div>
		<div class="object-right">
			<span><img src="${root }orders/images/mixed.png"></span> <span class="one">选择产品</span>
			<span class="two">确认订单信息</span> <span class="three">成功提交订单</span>
		</div>
	</div>
	<!-- 订单信息部分结束 -->

	<!-- 订单详情部分开始 -->
	<div class="contain roderdetails clearfix">
		<!-- 收货人信息开始 -->
		<div class="shouhuo">
			<div class="step-tit">
                <h3>收货人信息</h3>
                <div class="extra-r">
                   <a href="#none" class="ftx-05 J_consignee_global" onclick="use_NewConsigneeOversea()">新增收货地址</a>
               
                </div>
            </div>
			<div class="consignee-content">
				<ul id="aul">
					<c:if test="${!empty xReceiverInfoDefault}">
						<li class="li">
							<!-- 隐藏收货人信息id -->
							<input type="hidden" value="${xReceiverInfoDefault.id }">
							<em class="shouhuo_mo" id="shouhuo_moid"><span class="">${xReceiverInfoDefault.recName }</span><b></b></em>
							<div class="addr-detail">
								<span class="addr-name" limit="6" title="${xReceiverInfoDefault.recName }">${xReceiverInfoDefault.recName }</span>
								<span class="addr-info" limit="45" title="${xReceiverInfoDefault.recArea } ${xReceiverInfoDefault.recDetailAddress }">${xReceiverInfoDefault.recArea } ${xReceiverInfoDefault.recDetailAddress }</span>
								<span class="addr-tel">${xReceiverInfoDefault.recPhone }</span>
								<span class="addr-default">默认地址</span>
							</div>
							<div class="op-btns" consigneeid="691073381" isoldaddress="false">
								<a href="#none" class="ftx-05 edit-consignee" >编辑</a>
								<a href="#none" class="ftx-05 del-consignee hide" id="adhide">删除</a>
							</div>
						</li>
					</c:if>
					<c:if test="${!empty xReceiverInfoList}">
						<c:forEach items="${xReceiverInfoList }" var="xReceiverInfo">
							<li class="li">
								<!-- 隐藏收货人信息id -->
								<input type="hidden" value="${xReceiverInfo.id }">
								<em class="shouhuo_mo"><span class="">${xReceiverInfo.recName }</span><b></b></em>
								<div class="addr-detail">
									<span class="addr-name" limit="6" title="${xReceiverInfo.recName }">${xReceiverInfo.recName }</span>
									<span class="addr-info" limit="45" title="${xReceiverInfo.recArea } ${xReceiverInfo.recDetailAddress }">${xReceiverInfo.recArea } ${xReceiverInfo.recDetailAddress }</span>
									<span class="addr-tel">${xReceiverInfo.recPhone }</span>
								</div>
								<div class="op-btns" consigneeid="691073381" isoldaddress="false">
									<a href="#none" class="ftx-05 setdefault-consignee" ><center>设为默认地址</center></a>					
									<a href="#none" class="ftx-05 edit-consignee" >编辑</a>
									<a href="#none" class="ftx-05 del-consignee hide" id="ahide">删除</a>
								</div>
							</li>
						</c:forEach>
					</c:if>
				</ul>
			</div>
			
			<div class="addr-switch switch-on" id="consigneeItemAllClick"  clstag="pageclick|keycount|trade_201602181|2">
                <span>更多地址</span><b>︾</b>
            </div>
		</div>
		<!-- 收货人信息结束 -->

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
				<div class="cost-bottom">共<label id="totalNum">0</label>件产品</div>
			</div>
		</div>

		<!-- 账户信息 -->
		<div class="accountMsg clearfix">
			<ul>
				<li class="username">扣除账户：<span id="accName"></span></li>
				<li class="pay">扣除余额：<span class="cost-top-2"></span></li>
				<li class="rest">剩余：<span id="accMoney"></span></li>
			</ul>
			<button id="confirmorder" onclick="submitOrder();">确认订购</button>
			
			<span style="display: none;" id="orderInfo">${orderInfo }</span>
	
	<form action="" method="post">
		<input id="orderInfo" name="orderInfo" value="" />
		<%-- <input id="vistorName" name="vistorName" value="${vistorName }" /> --%>
	</form>
		</div>
	</div>
	<div id="mask"></div>



	<!-- 新增收货地址开始-->
	<div class="xinzeng" id="xinzeng">
		<div class="ui-dialog">
			<div class="ui-dialog-title" style="width: 538px;">     
			 	<span>新增收货人信息</span>    
			 	<a class="ui-dialog-close" id="uidialogclos" title="关闭">
			 		<span class="ui-icon ui-icon-delete"></span>
			 	</a>
			</div>
			<div class="ui-dialog-content">
				<div class="form-new">
					<div class="item" >
						<span class="label"><em>*</em>所在地区</span>
						
							<div class="container">

								<div class="docs-methods">
									<form class="form-inline">
										<div id="distpicker">
											<div class="form-group">
												<div id="picker3a" style="position: relative;">
													<input id="city-picker3a" name="recArea" class="form-control" readonly type="text" value="" data-toggle="city-picker">
												</div>
											</div>
										</div>
									</form>
								</div>
								
							</div>
						
						<span class="error-msg message" id="xinzeng_div_errora">请您填写收货人所在区域</span>
					</div>
					<div class="item" >
						<span class="label"><em>*</em>收货人</span>
						<input type="text" name="recName"  class="input" id="xinzeng_shouhuo" placeholder="收货人姓名"> 
						<span class="error-msg message" id="xinzeng_div_errorb">请您填写收货人姓名</span>
					</div>
					<div class="item" >
						<span class="label"><em>*</em>详细地址</span>
						<input type="text" name="recDetailAddress"  class="input" id="xinzeng_dizhi" placeholder="收货人详细地址"> 
						<span class="error-msg message" id="xinzeng_div_errorc">请您填写收货人详细地址</span>
					</div>
					<div class="item" >
						<span class="label"><em>*</em>手机号码</span>
						<input type="text" name="recPhone"   class="input" id="xinzeng_shouji" placeholder="收货人手机号码"> 
						<span class="error-msg message" id="xinzeng_div_errord" style="opacity: 0">请您填写收货人手机号码</span>
					</div>
					<div class="item mt20">
						<span class="label">&nbsp;</span>
						<div class="fl">
							<a href="#none" class="btn-1" onclick="save_xinzeng()"><span id="saveConsigneeTitleDiv">保存收货人信息</span></a>
							<div class="loading loading-1" style="display:none"><b></b>正在提交信息，请等待！</div>
						</div>
						<div style="display:none"><input id="consignee_form_reset" name="" type="reset"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 新增收货地址结束-->
		
	<div class="ui-mask" id="uimask">
	</div>

	<!-- 编辑开始-->
	<div class="bianji" id="bianji">
		<div class="ui-dialog">
			<div class="ui-dialog-title" style="width:538px;">     
			 	<span>编辑收货人信息</span>    
			 	<a class="ui-dialog-close" id="uidialogclosguanbi" title="关闭">
			 		<span class="ui-icon ui-icon-delete"></span>
			 	</a>
			</div>
			<div class="ui-dialog-content">
				<div class="form-new">
					<!-- 编辑收货人的id -->
					<input type="hidden" id="bianji_id">
					<div class="item" >
						<span class="label"><em>*</em>所在地区</span>
						<div class="container">

								<div class="docs-methods">
									<form class="form-inline">
										<div id="distpicker">
											<div class="form-group">
												<div id="acontrol" style="position: relative;">
													<input id="Acontrol" name="recArea" class="form-control" readonly type="text" value="安徽省/芜湖市/鸠江区" data-toggle="city-picker">
												</div>
											</div>
										</div>
									</form>
								</div>
								
							</div>
						<span class="error-msg message" id="mobile_div_error">请您填写收货人手机号码</span>
					</div>
					<div class="item" >
						<span class="label"><em>*</em>收货人</span>
						<input type="text" name="recName" id="bianji_xingming" class="input" placeholder="收货人姓名"> 
						<span class="error-msg message" id="mobile_errorz">请您填写收货人姓名</span>
					</div>
					<div class="item" >
						<span class="label"><em>*</em>详细地址</span>
						<input type="text" name="recDetailAddress"  id="bianji_dizhi" class="input" placeholder="收货人详细地址"> 
						<span class="error-msg message" id="mobile_errorx">请您填写收货人详细地址</span>
					</div>
					<div class="item" >
						<span class="label"><em>*</em>手机号码</span>
						<input type="text" name="recPhone" id="bianji_phone"  class="input" placeholder="收货人手机号码"> 
						<span class="error-msg message" id="mobile_errorc">请您填写收货人手机号码</span>
					</div>
					<div class="item mt20">
						<span class="label">&nbsp;</span>
						<div class="fl">
							<a href="#none" class="btn-1" onclick="save_bianji()"><span id="saveConsigneeTitleDiv">保存收货人信息</span></a>
							<div class="loading loading-1" style="display:none"><b></b>正在提交信息，请等待！</div>
						</div>
						<div style="display:none"><input id="consignee_form_reset" name="" type="reset"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 编辑结束-->

</body>
</html>
<script type="text/javascript" src="${root }products/js/jquery.min.js"></script>
<script type="text/javascript" src="${root }products/js/msgbox.js"></script>
<script src="${root }products/js/bootstrap.js"></script>
<script src="${root }products/js/city-picker.data.js"></script>
<script src="${root }products/js/city-picker.js"></script>
<script src="${root }products/js/main.js"></script>
<script>	
	//收货人id
    recId = "${xReceiverInfoDefault.id}";
    //收货人选中样式
    if("${xReceiverInfoDefault.id}" != null && "${xReceiverInfoDefault.id}" != ""){
    	$("#shouhuo_moid").css("border","1px solid rgb(228, 57, 60)");
    	$("#shouhuo_moid b").css("display","block");
    	$("#adhide").css("display","none");
	}
	
	//新增收货地址显现		
	function use_NewConsigneeOversea(){
		//收货人上限控制，最多添加五个
		$.ajax({
		    url:"${root }receiver/findReceiverNumXReceivers.do", //请求的url地址
		    dataType:"json", //返回格式为json
		    type:"POST",   //请求方式
		    success:function(data){
		    	if(data == false){
		    		$("#xinzeng").css("display","none");
					$("#uimask").css("display","none");	
		    		$.MsgBox2.Alert("添加收货人上限", "最多添加五个收货人地址！");
		    	}else{
		    		//显示添加框
		    		$("#xinzeng").css("display","block");
					$("#uimask").css("display","block");	
		    	}
		    }
		});
		
	}
	//新增收货地址关闭
	$("#uidialogclos").click(function(){
		$("#xinzeng").css("display","none");
		$("#uimask").css("display","none");
		$(".placeholder").css("display","block");
		$(".title").css("display","none")
		xinzeng_shouhuo.value="";
		xinzeng_dizhi.value="";
		xinzeng_shouji.value="";
		xinzeng_div_errora.style.opacity="0" ;
		xinzeng_div_errorb.style.opacity="0" ;
		xinzeng_div_errorc.style.opacity="0" ;
		xinzeng_div_errord.style.opacity="0" ;
		
		/* $("#city-picker3a").val();
		$("#xinzeng_shouhuo").val();
		$("#xinzeng_dizhi").val();
		$("#xinzeng_shouji").val(); */
	})
	
	//编辑收货地址关闭
	$("#uidialogclosguanbi").click(function(){
		$("#bianji").css("display","none");
		$("#uimask").css("display","none");
	})
	
	//编辑收货地址显现
	$('.consignee-content').delegate('.edit-consignee','click',function(){
		$("#bianji").css("display","block")
		$("#uimask").css("display","block");
		var id = $(this).parent().parent().find("input").val();
		$.ajax({
		    url:"${root }receiver/findReceiverByIdXReceivers.do", //请求的url地址
		    dataType:"json", //返回格式为json
		    data:{"id":id},
		    type:"POST",   //请求方式
		    success:function(data){
		    	//省市县
		    	var arr = new Array();
		    	arr = data.recArea.split("/");
		    	var province = arr[0];
	    		var city = arr[1];
	    		var district = arr[2];
		    	$(".title span").eq(0).html(province);
		    	$(".title span").eq(1).html(city);
		    	$(".title span").eq(2).html(district);
		    	
		    	
		    	$("#bianji_id").val(data.id);
		    	$("#bianji_xingming").val(data.recName);
		    	$("#bianji_dizhi").val(data.recDetailAddress);
		    	$("#bianji_phone").val(data.recPhone);
		    	
		    	//编辑地址显现
				/* $("#bianji").css("display","block")
				$("#uimask").css("display","block"); */
		    }
		});
	})

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


    //收起地址 
    var aLI=aul.getElementsByTagName("li");
    if(aLI.length>1){
    	consigneeItemAllClick.style.display="block";
    	$("#aul li").css("display","none");
		aLI[0].style.display="block";
		$("#consigneeItemAllClick").click(function(){
    		Num =! Num;
    		if(Num){
    			$("#aul li").css("display","block");

    			$("#consigneeItemAllClick span").html("收起地址");
    		}else{
    			$("#aul li").css("display","none");
    			aLI[0].style.display="block";
    			$("#consigneeItemAllClick span").html("更多地址");
    		}
    	})
    }else{
    	consigneeItemAllClick.style.display="none";
    	$("#aul li").css("display","none");
		aLI[0].style.display="block"
    }
    var Num=false;
    
	/*判断新增收货地址的保存收货人信息*/
 	var tele_photo=/^1[3|4|5|7|8][0-9]{9}$/;
 	var kong=/\S/;

 	//新增收货地址获取焦点
 	$("#xinzeng_shouhuo").blur(function(){
			if(xinzeng_shouhuo.value.match(kong)){
				xinzeng_div_errorb.style.opacity="0" ;
			}else{
				xinzeng_div_errorb.style.opacity="1" ;
			}
	}) 
	$("#xinzeng_shouhuo").focus(function(){
		xinzeng_div_errorb.style.opacity="0" ;
	})
	
	
	$("#xinzeng_dizhi").blur(function(){
		if(xinzeng_dizhi.value.match(kong)){
			xinzeng_div_errorc.style.opacity="0" ;
		}else{
			xinzeng_div_errorc.style.opacity="1" ;
		}
	}) 
	$("#xinzeng_dizhi").focus(function(){
		xinzeng_div_errorc.style.opacity="0" ;
	})
	
	
	$("#xinzeng_shouji").blur(function(){
			if(xinzeng_shouji.value.match(tele_photo)){
			xinzeng_div_errord.style.opacity="0";

		}else{
			if(xinzeng_shouji.value==""){
				xinzeng_div_errord.style.opacity="1" ;
				xinzeng_div_errord.innerHTML="请您填写收货人手机号码";
			 	return
			}
			xinzeng_div_errord.style.opacity="1"
			xinzeng_div_errord.innerHTML="手机号码格式不正确";
			return
		}
	}) 
	$("#xinzeng_shouji").focus(function(){
		xinzeng_div_errord.style.opacity="0" ;
	})
	
	
	
	
	function save_xinzeng(){
		
		
			
		if($(".title").html()==""){
			xinzeng_div_errora.style.opacity="1";
		}else{
			xinzeng_div_errora.style.opacity="0";
		}
		if(xinzeng_shouhuo.value.match(kong)){
			xinzeng_div_errorb.style.opacity="0" ;
		}else{
			xinzeng_div_errorb.style.opacity="1" ;
		}
		
		if(xinzeng_dizhi.value.match(kong)){
			xinzeng_div_errorc.style.opacity="0" ;
		}else{
			xinzeng_div_errorc.style.opacity="1" ;
		}

		if(xinzeng_shouji.value.match(tele_photo)){
			xinzeng_div_errord.style.opacity="0";

		}else{
			if(xinzeng_shouji.value==""){
				xinzeng_div_errord.style.opacity="1" ;
				xinzeng_div_errord.innerHTML="请您填写收货人手机号码";
			 	return
			}
			xinzeng_div_errord.style.opacity="1"
			xinzeng_div_errord.innerHTML="手机号码格式不正确";
			return
		}
		
		
		var recArea = $("#city-picker3a").val();
		var recName = $("#xinzeng_shouhuo").val();
		var recDetailAddress = $("#xinzeng_dizhi").val();
		var recPhone = $("#xinzeng_shouji").val();
		$.ajax({
		    url:"${root }receiver/saveOrUpdateReceiverInfoXReceivers.do", //请求的url地址
		    dataType:"json", //返回格式为json
		    data:{
		    	"recArea":recArea,
		    	"recName":recName,
		    	"recDetailAddress":recDetailAddress,
		    	"recPhone":recPhone
		    	},    
		    type:"POST", //请求方式
		    success:function(data){
		    	if(data == true){
		    		$("#xinzeng").css("display","none")
					$("#uimask").css("display","none")	
		    		$.MsgBox2.Alert("添加收货人", "添加成功！");
		    	}else{
		    		$("#xinzeng").css("display","none")
					$("#uimask").css("display","none")	
		       		$.MsgBox2.Alert("添加收货人", "添加失败！");
		    	}
		    }
		});
		
	}
	
	 	//编辑收货地址获取焦点
 	$("#bianji_xingming").blur(function(){
			if(bianji_xingming.value.match(kong)){
				mobile_errorz.style.opacity="0" ;
			}else{
				mobile_errorz.style.opacity="1" ;
			}
	}) 
	$("#bianji_xingming").focus(function(){
		mobile_errorz.style.opacity="0" ;
	})
	
	$("#bianji_dizhi").blur(function(){
			if(bianji_dizhi.value.match(kong)){
				mobile_errorx.style.opacity="0" ;
			}else{
				mobile_errorx.style.opacity="1" ;
			}
	}) 
	$("#bianji_dizhi").focus(function(){
		mobile_errorx.style.opacity="0" ;
	})
 	$("#bianji_phone").blur(function(){
	 	if(bianji_phone.value.match(tele_photo)){
				mobile_errorc.style.opacity="0";
	
			}else{
				if(bianji_phone.value==""){
					 mobile_errorc.style.opacity="1" ;
					 mobile_errorc.innerHTML="请您填写收货人手机号码";
					 return
				}
				mobile_errorc.style.opacity="1";
				mobile_errorc.innerHTML="手机号码格式不正确";
				return
			}
 	})
 	
 	$("#bianji_phone").focus(function(){
 		mobile_errorc.style.opacity="0";
 	})

	/*判断编辑收货地址的保存收货人信息*/
	function save_bianji(){
		if(bianji_xingming.value.match(kong)){
			 mobile_errorz.style.opacity="0" ;
		}else{
			mobile_errorz.style.opacity="1" ;
		}
		
		if(bianji_dizhi.value.match(kong)){
			 mobile_errorx.style.opacity="0" ;
		}else{
			 mobile_errorx.style.opacity="1" ;
		}

		if(bianji_phone.value.match(tele_photo)){
			mobile_errorc.style.opacity="0";

		}else{
			if(bianji_phone.value==""){
				 mobile_errorc.style.opacity="1" ;
				 mobile_errorc.innerHTML="请您填写收货人手机号码";
				 return
			}
			mobile_errorc.style.opacity="1"
			mobile_errorc.innerHTML="手机号码格式不正确";
			return
		}
		
		
		var province = $(".title span").eq(0).html();
    	var city = $(".title span").eq(1).html();
    	var district = $(".title span").eq(2).html();
		var recArea = province+"/"+city+"/"+district;
		
		var id = $("#bianji_id").val();
    	var recName = $("#bianji_xingming").val();
    	var recDetailAddress = $("#bianji_dizhi").val();
    	var recPhone = $("#bianji_phone").val();
		$.ajax({
		    url:"${root }receiver/saveOrUpdateReceiverInfoXReceivers.do", //请求的url地址
		    dataType:"json", //返回格式为json
		    data:{
		    	"id":id,
		    	"recArea":recArea,
		    	"recName":recName,
		    	"recDetailAddress":recDetailAddress,
		    	"recPhone":recPhone
		    	},    
		    type:"POST", //请求方式
		    success:function(data){
		    	if(data == true){
		    		$("#bianji").css("display","none")
					$("#uimask").css("display","none")	
		    		$.MsgBox2.Alert("编辑收货人", "编辑成功！");
		    	}else{
		    		$("#bianji").css("display","none")
					$("#uimask").css("display","none")	
		       		$.MsgBox2.Alert("编辑收货人", "编辑失败！");
		    	}
		    	//location.reload();
		    }
		});
	}
	
	//设为默认地址
	$(".setdefault-consignee").click(function(){
		var id = $(this).parent().parent().find("input").val();
		$.ajax({
		    url:"${root }receiver/setUpRecDefaultStatusXReceivers.do", //请求的url地址
		    dataType:"json", //返回格式为json
		    data:{"id":id},    
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
	
	//删除
	$('.consignee-content').delegate('.hide','click',function(){
		var id = $(this).parent().parent().find("input").val();
		$.MsgBox2.Confirm("删除收货人", "是否删除收货人",function(){
			$.ajax({
			    url:"${root }receiver/deleteReceiverXReceivers.do", //请求的url地址
			    dataType:"json", //返回格式为json
			    data:{"id":id},    
			    type:"POST" //请求方式
			    /* success:function(data){
			    	if(data == true){
			    		$.MsgBox2.Alert("删除收货人", "删除成功！");
			    	}else{
			       		$.MsgBox2.Alert("删除收货人", "删除失败！");
			    	}
			    } */
			});
		});
		
	});
	
	
</script>
