<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>预约订餐</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/adapter.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>wx_orders/css/reserve.css">
<!--     <script type="text/javascript" src="<%=basePath%>wx_orders/js/jquery.min.js"></script> -->
    <script type="text/javascript" src="<%=basePath%>wx_orders/js/msgbox.js"></script>
    <script type="text/javascript" src="<%=basePath%>wx_orders/js/reserve.js"></script>
</head>

<script>
	var totalNum=0,mealTime=0,mealtime=1,element=null;
	/*
	 * 加载菜单方法 
	 * week:周几、type：午晚餐
	 */
	function loadFood(week,type){
		var foodContent = $("#foodContent");
		var noticeContent = $("#noticeContent");
		
		//清空内容
		foodContent.html("");
		noticeContent.html("");
		
		var html = "";
		var noticehtml = "";
		$.ajax({
			url:"${root}wechat/getFoodListWeChat.do",
			data:{"week":week,"type":type, "checkedFoodBusiness":$("#checkedFoodBusiness").val()},
			dataType:"json",
			type:"post",
			success:function(data){
				console.log(data.noticeList)
				if(data != null){
					if(data.noticeList != ""){
						//公告
						for(var i=0; i<data.noticeList.length; i++){
							if(i == 0){
								if(data.noticeList[i]["ft"] == "LUN"){
									noticehtml += "<h2 style='display:block;width:3.6rem;margin:auto;margin-top:0.4rem ;font-size:0.8rem;font-weight: 600;color: #FF8F33;margin-bottom: 0.2rem;'>午餐公告</h2>";
								}else if(data.noticeList[i]["ft"] == "DIN"){
									noticehtml += "<h2 style='display:block;width:3.6rem;margin:auto;margin-top:0.4rem ;font-size:0.8rem;font-weight: 600;color: #FF8F33;margin-bottom: 0.2rem;'>晚餐公告</h2>";
								}
							}
							noticehtml += "<h3 style='padding: 0 0.8rem;font-size:0.7rem;text-indent:1.4rem;color:#666'>"+data.noticeList[i]["notice_desc"]+"</h3>";
						}
					}
					
					for(var i=0; i<data.foodList.length; i++){
						html += "";
						html += "	<li class=\"eve-meal\">";
						html += "		<input type=\"hidden\" name=\"food_id\" value=\""+data.foodList[i]["food_id"]+"\"/>";
						html += "		<input type=\"hidden\" name=\"week\" value=\""+data.foodList[i]["week"]+"\"/>";
	                  	html += "		<div class=\"clearfix total-meal\">";
				        html += "            <div class=\"meal\">";
				        /* html += "                <img style=\"width:100%;height:100%;\" src=\"${root}"+data[i]["food_img"]+"\">"; */
				        html += "                <img style=\"width:100%;height:100%;\" src=\"${root}"+((data.foodList[i]["food_img"] =="" ||data.foodList[i]["food_img"] ==null)?"/images/default.jpg": data.foodList[i]["food_img"])+ "\"/>";
				        html += "            </div>";
				        html += "            <div class=\"meal-det\">";
				        html += "                <div class=\"meal-det-top clearfix\">";
				        html += "                    <div class=\"mealName\">"+data.foodList[i]["food_name"]+"</div>";
				        html += "                </div>";
				        html += "                <div class=\"clearfix\">";
					    html += "                    <div class=\"total\">"+data.foodList[i]["food_desc"]+"</div>";
				        html += "                </div>";
				        
				        html += "                <div style=\"margin-bottom: 0.2rem;\">";
				        html += "                   <div>套餐价格￥<span class=\"priceClass\">"+data.foodList[i]["sell_price"]+"</span></div>";
				        html += "                </div>";
				        html += "                <div>";
				        html += "                   <div class=\"orderTime\">剩余<span class=\"orderNum\">"+data.foodList[i]["food_num"]+"</span>份</div>";
				      /*   html += "                    <div class=\"orderTime\">25元*"+data[i]["food_num"]+"</div>"; */
				        html += "                    <div class=\"jia\"> </div>";
				        html += "                </div>";
				        html += "            </div>";
	                 	html += "		 </div>";
	                  	html += "	</li>";
					}
					html +="<div style=\"height:2.613333333rem\"></div>";
				}else{
				
				}
				foodContent.html(html);
				noticeContent.html(noticehtml);
				
				$(".mealName").each(function(i,e){
					var that=$(this)
					var week_time1=$(".week").children("span").html()+$(".time").children("span").html()
					$(".number").each(function(i,e){
						var week_time2=$(this).parent().parent().siblings('.everyMeal').html()
						
						try{
							var names=$(e).parent().siblings(".car-desc").html().split('(')[0]
							if(week_time1==week_time2){
								if($(that).html()==names){
									var num=$($(that).parent().siblings()[1]).find(".orderNum").html()-$(e).html()
									$($(that).parent().siblings()[1]).find(".orderNum").html(num)
								}
							}
						}catch(err){
							
						}
						
					})
				})
				
/***************************************加号绑定事件***************************************/
				/*点击加号，添加菜品*/
				$(".jia").click(function(){
					//add  by dingzhj   at date 2017-03-14  验证订餐时间是否在订餐时间内
					//var odderTime = dayTime.
					if($(this).prev().children().html() == 0 || $(this).prev().children().html() < 0){
						 $.MsgBox.Alert("消息", "套餐不足"); 
						 return false;
					}
					if(true){
						var TIME=$(".week>span").html()+$(".time>span").html();
						var dayNum = weekType();
						var weekNum = week_num(TIME);
						if(weekNum%2 == 0){
							$.MsgBox.Alert("消息", "晚餐暂不开放"); 
							return false;
						}
						if(dayNum >= weekNum){
							$.MsgBox.Alert("消息", "不在预定时间内"); 
							return false;
						}
						totalNum++;
						var num = $(this).prev().children().html();
						num--;
						$(".car .totalNum").html(totalNum);
						$(".car .totalPrice").val(Number($(".car .totalPrice").val())+Number($(this).parent().prev().find("span").html()));
						$(".totalPay").html("总共消费"+$(".car .totalPrice").val()+"元");
						
						$(this).prev().children().html(num);
						var that=this;
						var flag=false;
						var temp=false;
						/*if($("#append li div[class='car-desc']").length==0){
							flag=true;
						};*/
						if($("#append li").length==0){
							flag=true;
						};
						$("#append ul").each(function(i,e){
							var s=$(".week>span").html()+$(".time>span").html()
							if($($(this).children()[0]).html()==s){
								flag=false;
								return false;
							}
							flag=true;
						})
						if(flag){
							var string="";
							var time=$(".week>span").html()+$(".time>span").html();
							var app=document.getElementById("#append");
							var li=document.createElement("li");
							var append=document.createElement("ul");
							mealTime++;
							append.id="append"+mealTime+"";
							append.name=$(".week>span").html()+$(".time>span").html();
							li.id="append_li"+mealtime+"";
							li.name=$(".week>span").prop("className").substr(5,1)+$(".time>span").prop("className").substr(4,1);
							mealtime++;
							$("#append").append(li);
							$("#"+li.id).append(append);
							var string="",time=$(".week>span").html()+$(".time>span").html();
							string+='<li class="everyMeal">'+time+'</li>';
							$("#"+append.id).append(string);
							
						
						if($("#append"+mealTime+" li div[class='car-desc']").length==0){
							temp=true;
						};
						}
						
						$("#append ul").each(function(){
							if($($(this).children()[0]).html()==TIME){
								element=$(this);
								$(this).find("div[class='car-desc']").each(function(i,e){
									if($(e).html().split('(')[0]==$(that).parent().parent().find(".mealName").html()){
										var s=parseInt($(e).siblings().find("span[class='number']").html());
										s++;
										$(e).siblings().find("span[class='number']").html(s);
										temp=false;
										return false;
									}
									temp=true;
								})
							}
						});
						if(temp){
							var str="";
							var can=$($($(this).parent().siblings()[0]).children()).html()+"("+$($($(this).parent().siblings()[1]).children()).html()+")";
							var food_id = $(this).parent().parent().parent().parent().find("input[name='food_id']").val();
							var sell_price = $(this).parent().siblings().find(".priceClass").html(); 
							str+='<li>';
							str+='<input type="hidden" name="c_food_id"  value="'+food_id+'"/>';	
							str+='<input type="hidden" name="c_sell_price"  value="'+sell_price+'"/>';	
							str+='	<div class="car-desc">'+can+'</div>';
							str+='	<div class="car-num">';
							str+='		<span class="reduce"></span>'	;
							str+='		<span class="number">1</span>';
							str+='		<span class="add"></span>'	;
							str+='	</div>';
							str+='</li>';
							element.append(str);
							$(".reduce").off().on("click",function(){
								
								var str1=$(this).parent().parent().siblings('.everyMeal').html()
								var str2=$(this).parent().siblings('.car-desc').html().split('(')[0]
								var str3=$(".week").children("span").html()+$(".time").children("span").html()
								$(".mealName").each(function(i,e){
									if(str1==str3){
										if(str2==$(e).html()){
											var num=$($(e).parent().siblings()[2]).children(".orderTime").children("span").html();
											num++;
											$($(e).parent().siblings()[2]).children(".orderTime").children("span").html(num);
											
											$(".car .totalPrice").val(Number($(".car .totalPrice").val())-Number($(this).parent().siblings().find(".priceClass").html()));
											$(".totalPay").html("总共消费"+$(".car .totalPrice").val()+"元");
											
										}
									}
								})
								
								
								
								totalNum--;
								$(".car .totalNum").html(totalNum);
								var s=$(this).siblings(".number").html();
								s--;
								
								if(s==0||s<0){
									if($(this).parent().parent().siblings().length==1||$(this).parent().parent().siblings().length<1){
										$(this).parent().parent().parent().parent().remove();
				//						console.log($(this).parent().parent().parent())
									};
									$(this).parent().parent().remove();
								}
								
								$(".totalPay").html("总共消费"+$(".car .totalPrice").val()+"元");
								if($("#append li").length==0){
									$("#mask").css("display","none");
									$(".buyCar").css("display","none");
									$(".totalPay").html("选点什么吧！")
								}
								$(this).siblings(".number").html(s);
							})
							$(".add").off().on("click",function(){
								var that=$(this)
								var str1=$(this).parent().parent().siblings('.everyMeal').html()
								var str2=$(this).parent().siblings('.car-desc').html().split('(')[0]
								var str3=$(".week").children("span").html()+$(".time").children("span").html()
								$(".mealName").each(function(i,e){
									if(str1==str3){
										if(str2==$(e).html()){
											var num=$($(e).parent().siblings()[2]).children(".orderTime").children("span").html();
											
											if(num==0||num<0){
								    			num=0;
								    			$.MsgBox.Alert("消息", "该套餐数量不足"); 
								    			return;
								    		}
											num--;
											$($(e).parent().siblings()[2]).children(".orderTime").children("span").html(num);
											$(".car .totalPrice").val(Number($(".car .totalPrice").val())+Number($(this).parent().siblings().find(".priceClass").html()));
											$(".totalPay").html("总共消费"+$(".car .totalPrice").val()+"元");
											
											totalNum++;
											$(".car .totalNum").html(totalNum);
											var s=$(that).siblings(".number").html();
											s++;
											$(that).siblings(".number").html(s);
											$(".totalPay").html("总共消费"+$(".car .totalPrice").val()+"元");
										}
									}
								})
							})
						}
						$(".totalPay").html("总共消费"+$(".car .totalPrice").val()+"元");
					}else{
						 $.MsgBox.Alert("消息", "不在订餐时间内"); 
						 return;
					}
					
				});
	/**************************************************************************************************/		
			}
		});
	}

	$(function(){
		loadFood("WEEK_7","LUN");
	});
	//获取 当前时间
function weekType(){
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
	var week="";
	var weekNum = 0;
	if(dayTime <= pmSartTime){//上午
		week = "周" + "日一二三四五六".split("")[new Date().getDay()]+"午餐";
		weekNum = week_num(week);
	}else{
		week = "周" + "日一二三四五六".split("")[new Date().getDay()]+"晚餐";
		weekNum = week_num(week);
	}
	return weekNum;
}
function week_num(type){
	var num =0;
	if(type =="周一午餐"){
		num=3;
	}else if(type =="周一晚餐"){
		num=4;
	}else if(type =="周二午餐"){
		num=5;
	}else if(type =="周二晚餐"){
		num=6;
	}else if(type =="周三午餐"){
		num=7;
	}else if(type =="周三晚餐"){
		num=8;
	}else if(type =="周四午餐"){
		num=9;
	}else if(type =="周四晚餐"){
		num=10;
	}else if(type =="周五午餐"){
		num=11;
	}else if(type =="周五晚餐"){
		num=12;
	}else if(type =="周六午餐"){
		num=13;
	}else if(type =="周六晚餐"){
		num=14;
	}else if(type =="周日午餐"){
		num=1;
	}else if(type =="周日晚餐"){
		num=2;
	}
	return num;
}
</script>
<style>
	.clearfixa{
		width:100%;
		height: 1.48rem;
	    font-size: 0.75rem;
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
<body>
 <input type="hidden" id="vistorName" value="${vistorName }"/><!-- 客人姓名 -->
 <input type="hidden" id="leadId" value="${leadId }"/>		  <!--领导ID -->
  <input type="hidden" id="type" value="${type }"/>		  <!--type -->
 <div id="mask"></div>
 <div id="maska"></div>
    <div>
        <div class="header">
	        <div class="title">预约订餐</div> 
	        <div class="back"></div>
        </div>
        
        <input type="hidden" id="checkedFoodBusiness">
        <div class="clearfixa">
	       	<c:if test="${!empty foodBusinessfirst }">
	       		<span class="lun_1a">${foodBusinessfirst.foodCompanyName }</span><b><img alt=""style="width:0.8rem;height:0.4rem;padding-left:0.1rem; background-size: 8%;" src="../wx_orders/P-images/999999.png"> </b>
	        </c:if>
	        <ul>
	        	<c:if test="${!empty foodBusinessfirst }">
	        		<li>
	        			<input type="hidden" id="defaultFoodBusiness" value="${foodBusinessfirst.userName }">
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
        <div class="clearfix">
        	
        	<div class=week>
        		<span class="week_7">周日</span>
        		<ul>
        			<li id="weeka"><span id="week_7" class="choose">周日</span><div id="week7" class="choosed"></div></li>
        			<li><span id="week_1" class="">周一</span><div id="week1" class=""></div></li>
        			<li><span id="week_2" class="">周二</span><div id="week2" class=""></div></li>
        			<li><span id="week_3" class="">周三</span><div id="week3" class=""></div></li>
        			<li><span id="week_4" class="">周四</span><div id="week4" class=""></div></li>
        			<li><span id="week_5" class="">周五</span><div id="week5" class=""></div></li>
        			<li><span id="week_6" class="">周六</span><div id="week6" class=""></div></li>
        		</ul>
        	</div>
        	<div class="time">
        		<span class="lun_1">午餐</span>
        		<ul>
        			<li><span id="lun_1" class="choose">午餐</span><div class="choosed"></div></li>
        			<li><span id="din_2">晚餐</span><div></div></li>
        		</ul>
        	</div>
        </div>
        
        <div style="clear: both;"></div>
        <!-- 公告 -->
		<div style="width:100%;" id="noticeContent">
		
		</div>
        
        <div>
    		<ul id="foodContent" >
    		
			</ul>
    	</div>
    	
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
    <form id="myForm" action="${root }/wechat/goReInfoWeChat.do" method="post">
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
<script type="text/javascript">
	$(function(){
		$(".clearfixa>span").click(function(){
			$("#maska").css("display","block");
			$(".clearfixa ul").css("display","none");
			$(".clearfixa ul").slideDown("normal");
		
		}) 
	
		if("${foodBusinessfirst}" != ""){
			$("#checkedFoodBusiness").val($("#defaultFoodBusiness").val());
			$("#checkedBusiness").val($("#checkedFoodBusiness").val());
		}else{
			$("#checkedBusiness").val("${foodBusinessfirst}");
		}
		
		var typeNew = "${type}";
		var leadIdNew = "${leadId}";
		var infosNew = "${infos}";
		var orderTypeNew = "${orderType}";
		var foodBusinessNew = "${foodBusiness}";
		var vistorNameNew = "${vistorName}";
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
			
			$("#checkedFoodBusiness").val($(this).find("input").val());
			$("#checkedBusiness").val($("#checkedFoodBusiness").val());
			
			loadFood("WEEK_7","LUN");
		});
	
	})
</script>
</html>
