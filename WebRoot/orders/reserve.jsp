<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>预约订餐</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>orders/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>orders/css/reserve.css">
    <script type="text/javascript" src="<%=basePath%>orders/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>orders/js/msgbox.js"></script>
    <script type="text/javascript" src="<%=basePath%>orders/js/reserve.js"></script>
</head>

<script>
	var totalNum=0,mealTime=0,mealtime=1,element=null;
	function isIE() { //ie?
      if (!!window.ActiveXObject || "ActiveXObject" in window){
      	 return true;
      }else{
       return false;
      }
       
    }
    $(function(){
     if(isIE()){
//     	alert(1);
    	$('.object-middle select').removeClass("selects");
    }else{
//     	alert(2);
    	$('.object-middle select').addClass("selects");
    };
    })
   
	/*
	 * 加载菜单方法 
	 * week:周几、type：午晚餐
	 */
	 $(".shoppingcar-bottom-l").click(function(){
		 $(".shoppingcar-middle").slideToggle();
	 });
	function loadFood(week){
		week =$(week).find('option:selected').attr("name");
		$("#weekTime").attr("name",week);
		
		var lunContent = $("#lunContent");
		var dinContent = $("#dinContent");
		var noticeLunContent = $("#noticeLunContent");
		var noticeDinContent = $("#noticeDinContent");
		
		//清空内容
		lunContent.html("");
		dinContent.html("");
		noticeLunContent.html("");
		noticeDinContent.html("");
		
		var lunhtml = "";
		var dinhtml = "";
		var noticeLunhtml = "";
		var noticeDinhtml = "";
		
		$.ajax({
			url:"${root}order/toYDPageXOrders.do",
			data:{"week":week,"foodBusiness":$("select[name='foodBusinessSelect']").val()},
			dataType:"json",
			type:"post",
			success:function(data){
				if(data != null){
					//午餐公告
					for(var i = 0; i < data.LUNNoticeList.length; i ++){
						if(i == 0){
							noticeLunhtml += "<h2 style='display:block;width:168px;margin:auto;margin-bottom:8px;font-size: 15px;font-weight: 600;color: #FF8F33;'>午餐公告</h2>";
						}
						noticeLunhtml += "<h3 style='font-size: 13px;color: #666;text-indent:26px;'>"+data.LUNNoticeList[i]["notice_desc"]+"</h3>";
					}
					
					//午餐
					for(var i = 0; i < data.LUNList.length; i ++){
						lunhtml += "<li> ";
			            lunhtml += "    <div>";
			            lunhtml += "<input type=\"hidden\" name=\"food_id\" value=\""+data.LUNList[i]["food_id"]+"\"/>";
			            lunhtml += "        <div class=\"meal-img\"><img style=\"width:100%;height:100%;\" src=\"${root}"+((data.LUNList[i]["food_img"] =="" ||data.LUNList[i]["food_img"] ==null)?"/images/default.jpg": data.LUNList[i]["food_img"])+ "\"/></div> ";
			            lunhtml += "        <div class=\"meal-msg\"> ";
			            lunhtml += "        	<div class=\"msg-0\">"+data.LUNList[i]["food_name"]+"</div> ";
			            lunhtml += "            <div class=\"msg-1\">"+data.LUNList[i]["food_desc"]+"</div> ";
			            lunhtml += "            <div class=\"msg-21\">套餐价格￥<span>"+data.LUNList[i]["sell_price"]+"</span></div> ";
			            lunhtml += "            <div class=\"msg-2\">剩余<span>"+data.LUNList[i]["food_num"]+"</span>份</div> ";
			            lunhtml += "            <a href=\"javascript:;\" class=\"msg-3\">+</a> ";
			            lunhtml += "        </div> ";
			            lunhtml += "    </div> ";
			            lunhtml += "</li> ";
					}
					
					//晚餐公告
					for(var i = 0; i < data.DINNoticeList.length; i ++){
						if(i == 0){
							noticeDinhtml += "<h2 style='display:block;width:168px;margin:auto;margin-bottom:8px;font-size: 15px;font-weight: 600;color: #FF8F33;'>晚餐公告</h2>";
						}
						noticeDinhtml += "<h3 style='font-size: 13px;color: #666;text-indent:26px;'>"+data.DINNoticeList[i]["notice_desc"]+"</h3>";
					}
					//晚餐
					for(var i = 0; i < data.DINList.length; i ++){
						dinhtml += "<li> ";
			            dinhtml += "    <div> ";
			            dinhtml += "<input type=\"hidden\" name=\"food_id\" value=\""+data.DINList[i]["food_id"]+"\"/>";
			          //  dinhtml += "        <div class=\"meal-img\"><img style=\"width:100%;height:100%;\"  src=\"${root}"+data.DINList[i]["food_img"]+"\"/></div> ";
			            dinhtml += "        <div class=\"meal-img\"><img style=\"width:100%;height:100%;\"  src=\"${root}"+((data.DINList[i]["food_img"] =="" ||data.DINList[i]["food_img"] ==null)?"/images/default.jpg": data.DINList[i]["food_img"])+ "\"/></div> ";
			            dinhtml += "        <div class=\"meal-msg\"> ";
			            dinhtml += "        	<div class=\"msg-0\">"+data.DINList[i]["food_name"]+"</div> ";
			            dinhtml += "            <div class=\"msg-1\">"+data.DINList[i]["food_desc"]+"</div> ";
			            dinhtml += "            <div class=\"msg-21\">套餐价格￥<span>"+data.DINList[i]["sell_price"]+"</span></div> ";
			            dinhtml += "            <div class=\"msg-2\">剩余<span>"+data.DINList[i]["food_num"]+"</span>份</div> ";
			            dinhtml += "            <a href=\"javascript:;\" class=\"msg-3\">+</a> ";
			            dinhtml += "        </div> ";
			            dinhtml += "    </div> ";
			            dinhtml += "</li> ";
					}
				$(lunContent).html(lunhtml);
				dinContent.html(dinhtml);
				noticeLunContent.html(noticeLunhtml);
				noticeDinContent.html(noticeDinhtml);
				
/***************************************加号绑定事件***************************************/
				    /*点击添加菜品按钮*/
    $(".msg-3").click(function(){
    	var that=this;
    	//add  by dingzhj   at date 2017-03-14  验证订餐时间是否在订餐时间内
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
		//var odderTime = dayTime.
		if(true){
			var TIME=$("#weekTime").val()+$(that).parents(".meal").prev().prev().html();
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
			
    	if($(this).prev().children().html()==0||$(this).prev().children().html()<0){
    		 $.MsgBox.Alert("消息", "该套餐已售完"); 
    		return;
    	};
    	var num  = $(this).prev().children().html();
    	num--;
    	$(this).prev().children().html(num);
		var flag=false;
		var temp=false;
		/*判断是否有菜品*/
		if($("#append li").length==1){
			flag=true;
		};
		$("#append ul").each(function(i,e){
			var s=$("#weekTime").val()+$(that).parents(".meal").prev().prev().html();
			if($($(this).children()[0]).html()==s){
				flag=false;
				return false;
			}
			flag=true;
		})
		$("#totalMoney").html(Number($("#totalMoney").html())+Number($(this).siblings(".msg-21").find("span").html()));
		if(flag){
			$(".shoppingcar-middle-top").css({"display":"block"});
			$(".shoppingcar-top").css({"display":"block"});
			$(".pay").css({"display":"inline-block"});
			$(".shoppingcar-bottom-r span").html("选好了").css({"font-weight":"700"});
			var string="";
			var time=$("#weekTime").val()+$(that).parents(".meal").prev().prev().html();
			var app=document.getElementById("#append");
			var li=document.createElement("li");
			var append=document.createElement("ul");
			mealTime++;
			append.id="append"+mealTime+"";
			append.name=$("#weekTime").attr("name").substr(5,1)+$(that).parents(".meal").prev().prev().attr("name");
			li.id="append_li"+mealtime+"";
			/*li.name=$(".week>span").prop("className").substr(5,1)+$(".time>span").prop("className").substr(4,1);*/
			mealtime++;
			$("#append").append(li);
			/* $("#totalMoney").html("25"); */
			
			$("#"+li.id).append(append);
			var string="",time=$("#weekTime").val()+$(that).parents(".meal").prev().prev().html();
			string+='<li class="everyMeal">'+time+'</li>';
			$("#"+append.id).append(string);
			
			if($("#append"+mealTime+" li span[class='middle-bot-l']").length==0){
				temp=true;
			};
		
		}
		
		$("#append ul").each(function(i,e){
			if($($(e).children()[0]).html()==TIME){
				element=$(e);
				$(e).find("span[class='middle-bot-l']").each(function(i,e){
					if($(e).html().split('(')[0]==$(that).siblings(".msg-0").html()){
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
			var  str='';
			var c=$($(that).siblings()[0]).html()+"("+$($(that).siblings()[1]).html()+")";
			var food_id = $(that).parent().parent().find("input[name='food_id']").val();
			var sell_price = $(this).siblings('.msg-21').find("span").html();
			str+='<li class="shoppingcar-middle-bot">'
				str+='<input type="hidden" name="c_food_id"  value="'+food_id+'"/>'	
				str+='<input type="hidden" name="c_sell_price"  value="'+sell_price+'"/>'
				str+='<span class="middle-bot-l">'+c+'</span>'
				str+='<span class="middle-bot-r">'
					str+='<span class="jian">-</span>'     
						str+='<span class="number">1</span>'
							str+='<span class="jia">+</span>'
								str+='</span>'
									str+='</li>';
			element.append(str);
    		$(".jia").unbind('click').click(function(e){
    				var that=$(this)
    				var str1=$(this).parent().parent().siblings(".everyMeal").html().substring(2,4)
    				var str2=$(this).parent().siblings(".middle-bot-l").html().split('(')[0]
    				$(".msg-0").each(function(i,e){
    					if(str1==$(e).parents('.meal').prev().prev().html()){
    						if(str2==$(e).html()){
    							var num=$(this).siblings('.msg-2').find("span").html();
	        		    		if(num==0||num<0){
	        		    			num=0;
	        		    			$.MsgBox.Alert("消息", "该套餐数量不足"); 
	        		    			return;
	        		    		}
	        		    		num--;
	        		    		$(this).siblings('.msg-2').find("span").html(num);
	        		    		var s=parseInt($(that).siblings("span[class='number']").html());
	                			s++;
	                			$("#totalMoney").html(Number($("#totalMoney").html())+Number($(this).siblings(".msg-21").find("span").html()));
	                			$(that).siblings("span[class='number']").html(s);
    						}
    					}
    				})
    		});
    		$(".jian").unbind('click').click(function(){
    			var str1=$(this).parent().parent().siblings(".everyMeal").html().substring(2,4)
				var str2=$(this).parent().siblings(".middle-bot-l").html().split('(')[0]
				$(".msg-0").each(function(i,e){
					if(str1==$(e).parents('.meal').prev().prev().html()){
						if(str2==$(e).html()){
							var num=$(this).siblings('.msg-2').find("span").html();
        		    		num++;
        		    		$(this).siblings('.msg-2').find("span").html(num);
						}
					}
				})
    			
    			var n=parseInt($(this).siblings("span[class='number']").html());
    			n--;
    			$("#totalMoney").html(Number($("#totalMoney").html())-Number($(this).parent().siblings("input[name='c_sell_price']").val()));
    			if(n==0){
    				if($(this).parent().parent().siblings().length==1){
    					$(this).parent().parent().parent().parent().remove();
    				}
        			$(this).parent().parent().remove();
        			if($("#append li span[class='middle-bot-l']").length==0){
        				$("#append .shoppingcar-middle-top").css({"display":"none"});
        				$(".shoppingcar-top").css({"display":"none"});
        				$(".shoppingcar-bottom-r span").html("购物车是空的").css({"font-weight":"100"});
        				$(".pay").css({"display":"none"});
        			};
    			}
    			$(this).siblings("span[class='number']").html(n);
    		})
		}
    }else{
    	 $.MsgBox.Alert("消息", "不在订餐时间内"); 
		 return;
    	}
    });
/**************************************************************************************/		
			}
		 }
		});
	} 
	
	$(function(){
		$("#staff").val(${appointUser.userId});
  		$("#zdPerson").text(('${appointUser.realName}'!='')?'${appointUser.realName}':'无');
		loadFood($("#weekTime"));
		$("#goback").click(function(){
			location.href="http://"+window.location.host+"/ordersystem/order/toOrderingPageXOrders.do";
		})
	})
	
	
	function show(obj){
  		if($(obj).val() == "LD"){
  			$("#ld").show();
  			//$("#c").show();
  		}else{
  			$("#ld").hide();
  			//$("#c").hide();
  		}
  		if($(obj).val() == "ZD"){
  			$("#choose").css({"display":"block"});
            $(".mask").css({"display":"block"});
  		}
  		if($(obj).val() == "KR"){
  			$("#krName").css({"display":"inline-block"});
  		}else{
  		$("#krName").css({"display":"none"});
  		}
  	}
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
	
	$(function(){
		//切换不同的餐饮公司，展示不同的餐饮公司餐品
		$("select[name='foodBusinessSelect']").change(function(){
			window.location.href = "${root}order/toOrderingPageXOrders.do?type=YD&foodBusiness="+$("select[name='foodBusinessSelect']").val();
		});
	})
</script>
<body>
  <!-- 头部开始 -->
<%@include file="header.jsp"%>
    <span style="display: block;margin-left: 90px;margin-top: 20px;margin-bottom: 20px;color: red;font-weight: 500;font-size: 14px;"><b>预定时间为：午餐时间 ：00:00:00 - 10:20:00&nbsp;&nbsp;晚餐时间：12:00:00 - 16:00:00（注：只能预定当前订餐时间之后的套餐，晚餐暂不开放）</b></span>
    <!-- 头部结束 -->
    <div class="contain order-object">
    	<div style="margin-left: 18px;float: left;font-size: 16px;font-size: 15px;font-weight: 600;">餐饮公司：</div>
		<div style="float: left;margin-left: 12px;">
			<select style="width:140px;height:30px;border-radius: 4px;border: 1px solid #FF8F33;font-size: 15px;" name="foodBusinessSelect">
				<c:if test="${!empty foodBusinessList }">
					<c:forEach items="${foodBusinessList }" var="fb">
						<option value="${fb.userName }" <c:if test="${fb.userName==foodBusinessfirst.userName }">selected="selected"</c:if> >${fb.foodCompanyName }</option>
					</c:forEach>
				</c:if>
			</select>
		</div>
    	
    	<div class="object-left" style="margin-left: 51px;font-size: 15px;font-weight: 600;">预定时间：</div>
        <div class="object-middle" style="float: left;margin-left: 12px;">
        	<select style="width:140px;height:30px;border-radius: 4px;border: 1px solid #FF8F33;font-size: 15px;" id="weekTime" onchange="loadFood(this);" name="WEEK_1">
        		<option name="WEEK_7">周日</option>
        		<option name="WEEK_1">周一</option>
        		<option name="WEEK_2">周二</option>
        		<option name="WEEK_3">周三</option>
        		<option name="WEEK_4">周四</option>
        		<option name="WEEK_5">周五</option>
        		<option name="WEEK_6">周六</option>
        	</select>
        </div>
    	
		<div class="object-left" style="margin-left: 53px;float: left;font-size: 15px;font-weight: 600;">订餐对象：</div>
		<div class="object-middle" style="float: left;margin-left: 15px;font-size: 15px;">
			<input onclick="show(this);" id="ZJ" checked="checked" type="radio"
				name="object" value="ZJ">自己
			<c:if test="${isKR }">
				<input onclick="show(this);" id="KR" type="radio" name="object"
					value="KR" style="margin-left: 10px;">客人
				<input type="text" id="krName" style="display: none;width: 140px;height: 30px;border-radius: 4px;border: 1px solid #FF8F33;font-size: 15px;margin-left: 6px;"/>
            </c:if>
			<c:if test="${isLD }">
				<input onclick="show(this);" id="LD" type="radio" name="object"
					value="LD" style="margin-left: 10px;">领导
            <select name="ld" id="ld" style="display: none;position:relative;top:-2px;display: none;width: 140px;height: 30px;border-radius: 4px;border: 1px solid #FF8F33;font-size: 15px;">
					<c:forEach items="${leadList }" var="led">
						<option value="${led.lead_id }">${led.lead_name }</option>
					</c:forEach>
				</select>
			</c:if>
			<c:if test="${isZD }">
				<input onclick="show(this);" id="ZD" type="radio" name="object"
					value="ZD">指定人点餐
            </c:if>
		</div>
		
		<div class="object-right">
        	<button id="goback" style="line-height: 30px;">返回</button>
        </div>
	</div>
    
    
    <!-- 订餐对象开始部分 -->
    <!-- <div class="contain order-object">
        <div class="object-left">预定时间：</div>
        <div class="object-middle">
        	<select id="weekTime" onchange="loadFood(this);" name="WEEK_1">
        		<option name="WEEK_7">周日</option>
        		<option name="WEEK_1">周一</option>
        		<option name="WEEK_2">周二</option>
        		<option name="WEEK_3">周三</option>
        		<option name="WEEK_4">周四</option>
        		<option name="WEEK_5">周五</option>
        		<option name="WEEK_6">周六</option>
        	</select>
        </div>
        <div class="object-right">
        	<button id="goback" style="line-height: 30px;">返回</button>
        </div>
    </div> -->
    <!-- 订餐对象部分结束 -->
    
    <!-- 餐品开始部分 -->
    <div class="contain time lunch" name="1" style="font-size: 14px;">午餐</div>
    
    <!-- 午餐公告 -->
	<div class="contain meal" id="noticeLunContent" style="padding-right: 31px;padding-top: 14px;" >
	
	</div>
    
    <div class="contain meal">
        <ul id="lunContent" class="clearfix">
        	<li></li>
           <!--  <li>
                <div>
                    <div class="meal-img"></div>
                    <div class="meal-msg">
                    	<div class="msg-0">套餐A</div>
                        <div class="msg-1">咸鱼米饭+可乐+鸡排</div>
                        <div class="msg-2">剩余<span>20</span>份</div>
                        <a href="javascript:;" class="msg-3">+</a>
                    </div>
                </div>
            </li> -->
            
        </ul>
    </div>
    <div class="contain time dinner" name="2" style="font-size: 14px;">晚餐</div>
    
    <!-- 晚餐公告 -->
	<div class="contain meal" id="noticeDinContent" style="padding-right: 31px;padding-top: 14px;" >
	
	</div>
	
    <div class="contain meal">
        <ul id="dinContent" class="clearfix">
        	<li></li>
          <!--   <li>
                <div>
                    <div class="meal-img"></div>
                    <div class="meal-msg">
                    	<div class="msg-0">套餐A</div>
                        <div class="msg-1">咸鱼米饭+可乐+鸡排</div>
                        <div class="msg-2">剩余<span>20</span>份</div>
                        <a href="javascript:;" class="msg-3">+</a>
                    </div>
                </div>
            </li> -->
            
        </ul>
    </div>
    <!-- 餐品结束部分 -->
    <!-- 购物车开始部分 -->
    <div class="shoppingcar">
        <div class="shoppingcar-top">
            <span class="shoppingcar-top-l">购物车</span>
            <span class="shoppingcar-top-r">清空</span>
        </div> 
        <div class="shoppingcar-middle">
            <ul id="append">
                <li class="shoppingcar-middle-top">
                    <span class="middle-top-l">套餐</span>
                    <span class="middle-top-r">份数</span>
                </li>
            </ul>
        </div>
        <div class="shoppingcar-bottom">
            <div class="shoppingcar-bottom-l">
                <span class="buycar">
                     <img src="<%=basePath%>orders/images/buycar.png">
                 </span>
                <span class="pay">共￥<span id="totalMoney">0</span>元</span>
            </div>
            <div class="shoppingcar-bottom-r">
               <!--  <span>选好了</span> -->
               <span>购物车是空的</span>
            </div>
        </div>
    </div>
    <!-- 购物车部分结束 -->
    <div id="choose">
    	<div class="choose-title">指定人点餐</div>
		<lable>当前指定人：</lable>
		<span  id="zdPerson" ></span> <input type="hidden"
			id="staff" />
		<div style="width: 100%; height: 20px;"></div>
		<lable>选择指定人：</lable>
        	<ul>
        		<li class="department">
	        		<select id="dept" name="dept">
	        			<option value="0">请选择</option>
	        			  <c:forEach items="${deptList}" var="list">
				                <option value="${list.id}">${list.name}</option>
				        </c:forEach>
	        		</select>
        		</li>
        		<li class="orderName">
	        		<select id="staffName" name="staff">
	        			<option value="0">请选择</option>
	        		</select>
        		</li>
        	</ul>
		<input type="button" id="isOk" value="确定" />
    </div>
    <div class="mask"></div>
    
    <form name="myForm" id="myForm" action="${root }/order/goReSumXOrders.do"
		method="POST">
		<input type="hidden" name="orderInfo" id="orderInfo" value="">
		<input type="hidden" name="vistorName" id="vistorName" >
		<input type="hidden" name="foodBusiness" id="foodBusiness">
	</form>
	<div style="height:50px;width:100%"></div>
</body>
</html>
