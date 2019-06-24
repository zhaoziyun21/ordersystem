<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
<meta charset="UTF-8">
<!-- 订餐页面 -->
<title>订餐</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>orders/css/common.css">
<script type="text/javascript" src="<%=basePath%>orders/js/ordering.js"></script>
<script type="text/javascript" src="<%=basePath%>orders/js/msgbox.js"></script>
</head>
<style>
select option {
    height: 32px;
    line-height: 32px;
    padding: 0px 5px;
    cursor: pointer;
    padding:20px;
    
}

</style>
<script>
  	function show(obj){
  		if($(obj).val() == "LD"){
  			$("#ld").show();
  			/* $("#c").show(); */
  		}else{
  			$("#ld").hide();
  			/* $("#c").hide(); */
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
  	function myOrder(){
  		location.href="${root}/order/toMyOrderXOrders.do";
  	}
  	
  	$(function(){
  		$("#staff").val(${appointUser.userId})
  		$("#zdPerson").text(('${appointUser.realName}'!='')?'${appointUser.realName}':'无');
  	//加载指定人tree
//     var combo2 = $("#txt2").ligerComboBox({
//         width: 180,
//         selectBoxWidth: 200,
//         valueField : 'id',
//         textField: 'name',
//         treeLeafOnly:true,
//         tree: { url:'${root}/order/getStaffTreeDataXOrders.do', 
//         parms:[{name:'staffName',value:$("#staffName").val()}],
//         checkbox: false, 
//         isExpand:false,
//         ajaxType: 'get', 
//         textFieldName: 'name',
//         idFieldName: 'id',
//         parentIDFieldName: 'parent_id' 
//          },
//          onSelected: function (newvalue,text)
//          {
        
//            		 if(newvalue.indexOf("staff_") != -1){
//                  		$("#staff").val(newvalue.split("_")[1]);
//                  		$("#zdPerson").text(text);
                 		
//                  }else{
//                  	alert('只能选择人员')
//                  }
//           },onSuccess:function(){
//         	  combo2.selectValue('staff_'+$("#staff").val())
//           }
//     });
//     if('${appointUser.userId}'!=''){
//   				combo2.selectValue('staff_'+'${appointUser.userId}')
//           }
  	
  		if("${!empty infos}" == "true"){
  			var json = eval(${infos});
  			if(json.orderObj == "KR"){
  				$("#KR").attr("checked","checked");
  			}else if(json.orderObj == "ZJ"){
  				$("#ZJ").attr("checked","checked");
  			}else if(json.orderObj == "LD"){
  				$("#LD").attr("checked","checked");
  				$("#ld").val(json.ldId);
  				$("#ld").show();
  			}/* else if(json.orderObj == "ZD"){
  			} */
  			//加载购物车信息
  			var detailCar = json.carDetail;
  			var sum = 0;
  			if(detailCar != null && detailCar.length > 0){
  				for(var i = 0; i < detailCar.length; i ++){
  					sum += Number(detailCar[i].total);
  					var str ='';
			  			str+='<li class="shoppingcar-middle-bot">';
			        	str+='<input type="hidden" name="c_food_id"  value="'+detailCar[i].id+'"/>'	;
			        	str+='<input type="hidden" name="c_sell_price"  value="'+detailCar[i].price+'"/>'	;
			        	str+='<span class="middle-bot-l">'+detailCar[i].food+'</span>';
			        	str+='<span class="middle-bot-r">';
				        	str+='<span class="jian">-</span>'     ;
				        	str+='<span class="number">'+detailCar[i].num+'</span>';
				        	str+='<span class="jia">+</span>';
			        	str+='</span>';
			        	str+='</li>';
        			$("#append").append(str);
  				}
  				
  				$(".shoppingcar-middle-top").css({"display":"block"});
    			$(".pay").css({"display":"inline-block"});
    			$(".shoppingcar-bottom-r span").html("选好了").css({"font-weight":"700"});
    			
    			//==========================绑定加减号事件======================================
    					$(".jia").unbind('click').click(function(e){
	        			var s=parseInt($(this).siblings("span[class='number']").html());
	        			s++;
	        			$("#totalMoney").html(Number($("#totalMoney").html())+Number($(this).parent().siblings("input[name='c_sell_price']").val()));
	        			$(this).siblings("span[class='number']").html(s);
	        		    var str=$(this).parent().prev().html().split('(')[0]
	        		    $(".msg-0").each(function(i,e){
	        		    	if(str==$(e).html()){
	        		    		var num=$(this).siblings('.msg-2').find("span").html();
	        		    		num++;
	        		    		$(this).siblings('.msg-2').find("span").html(num);
	        		    	}
	        		    })
        		});
        		$(".jian").unbind('click').click(function(){
        			var n=parseInt($(this).siblings("span[class='number']").html());
        			n--;
        			$("#totalMoney").html(Number($("#totalMoney").html())-Number($(this).parent().siblings("input[name='c_sell_price']").val()));
        			if(n==0){
	        			$(this).parent().parent().remove();
	        			if($("#append li span[class='middle-bot-l']").length==0){
	        				$("#append .shoppingcar-middle-top").remove();
	        				$(".shoppingcar-bottom-r span").html("购物车是空的").css({"font-weight":"100"});
	        				$(".pay").css({"display":"none"});
	        			};
        			}
        			$(this).siblings("span[class='number']").html(n);
        		})
    			
    			//=====================================================================================
    			$("#totalMoney").html(sum);
  			}
  			
  			
  		}
  		$('#isOk').click(function(){
  		 var staffname = $("#staffName").find("option:selected").text();
		 var staffId = $("#staffName").find("option:selected").val();
		 $("#choose").css({"display":"none"});
      		$(".mask").css({"display":"none"});
		 if(staffId!='0'){
			 $.ajax({
				    url:"${root}/order/saveXAppointRelationXOrders.do",    //请求的url地址
				    dataType:"html",   //返回格式为html
				    type:"POST",   //请求方式
				    data:{
				    	"appointId":staffId,
				    	"appointName":staffname
				    	},    
				    success:function(req){
					    if(req=='Y'){
					    $.MsgBox.Alert("消息","指定成功",function(){
					    	$("#zdPerson").text(staffname);
					    	$("#staff").text(staffId);
					    });
					    }
				    },
				    error:function(req){
				    	alert(req);
				    }
				});
		 }else{
			 $.MsgBox.Alert("提示", "请选择被指定人！");
		 }
  		});
  		
  	//	级联查询被指定人
	$("select[name='dept']").change(function(){
		var deptId = $(this).find("option:selected").val();
		$.ajax({
            type: 'post',
            url: "http://"+window.location.host+"/ordersystem/order/getStaffInfoXOrders.do",
            data: {'deptId':deptId},
            cache: false,
            dataType: "html",
            success: function(data) {
            	var d = eval('('+data+')');
            	if(d.success=='true'){
            		var html = "<option value='0'>请选择</option>";
            		$("#staffName").html("");
            		var opt = d.data;
            		for ( var i = 0; i < opt.length; i++) {
						html +="<option value='"+opt[i].id+"'>"+opt[i].name+"</option>";
					}
            		$("#staffName").html(html);
            	}else{
            		
            	}
            	
            },
            error: function(data) {
               
            }
        });
	});
	
	//切换不同的餐饮公司
	$("select[name='foodBusinessSelect']").change(function(){
		window.location.href = "${root}order/toOrderingPageXOrders.do?foodBusiness="+$("select[name='foodBusinessSelect']").val();
	});
  		
 });
  	
  </script>
<body>
	<!-- 头部开始 -->
	<%@include file="header.jsp"%>
	<span style="display: block;margin-left: 90px;margin-top: 20px;color: red;font-weight: 500;font-size: 14px;"><b>午餐时间 ：00:00:00 - 10:20:00 &nbsp;&nbsp;晚餐时间：12:00:00 - 16:00:00 (晚餐暂不开放)</b></span> 
	<!-- 订餐对象开始部分 -->
	
	<div class="contain order-object">
		<!-- 订购产品 -->
		<div style="margin:auto;width:300px;">
			<div style="float: right;">
				<button onclick="toBuyProducts();" style="line-height:38px;width:114px;height:38px;background:#FF8F33;font-size: 18px;font-weight: 600;border-radius: 4px;color: #fff;">在线超市</button>
	 		</div>
			<div style="float:left;">
				<button onclick="toYuDing();" style="line-height:38px;width:114px;height:38px;background:#FF8F33;font-size: 18px;font-weight: 600;border-radius: 4px;color: #fff;">套餐预定</button>
	 		</div>
		</div>
 		
	</div>
	<div class="contain order-object">
	
		<div style="width:810px;margin:auto;">
			<div style="margin-left: 42px;float: left; font-size: 15px;font-weight: 600;">餐饮公司：</div>
			<div style="float: left;margin-left: 12px;">
				<select name="foodBusinessSelect" style="width:140px;height:30px;border-radius: 4px;border: 1px solid #FF8F33;font-size: 15px;">
					<c:if test="${!empty foodBusinessList }">
						<c:forEach items="${foodBusinessList }" var="fb">
							<option  value="${fb.userName }" <c:if test="${fb.userName==foodBusinessfirst.userName }" >selected="selected"</c:if> >${fb.foodCompanyName }</option>
						</c:forEach>
					</c:if>
				</select>
			</div>
			
			<div class="object-left" style="margin-left: 97px;float: left; font-size: 15px;font-weight: 600;">订餐对象：</div>
			<div class="object-middle" style="float: left;margin-left: 15px;font-size: 15px;">
				<input onclick="show(this);" id="ZJ" checked="checked" type="radio"
					name="object" value="ZJ"  >自己
				<c:if test="${isKR }">
					<input onclick="show(this);" id="KR" type="radio" name="object"
						value="KR" style="margin-left:10px">客人
					<input type="text" id="krName" style="display: none;width: 140px;height: 30px;border-radius: 4px;border: 1px solid #FF8F33;font-size: 15px;margin-left: 6px;"/>
	            </c:if>
				<c:if test="${isLD }">
					<input onclick="show(this);" id="LD" type="radio" name="object"
						value="LD" style="margin-left:10px">领导
	            <select name="ld" id="ld" style="display: none;width: 140px;height: 30px;border-radius: 4px;border: 1px solid #FF8F33;font-size: 15px;"
						style="positive:relative;top:-2px;">
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
		
		
		</div>
		
		
 		
	</div>
	<!-- 订餐对象部分结束 -->
	
	<!-- 今日公告 -->
	<c:if test="${!empty notice && notice!='no' }">
		<div class="contain meal" style="padding-right: 31px;padding-bottom:20px;padding-top: 14px;" >
			<h2 style="display:block;width:168px;margin:auto;margin-bottom:8px;font-size: 15px;font-weight: 600;color: #FF8F33;">今日公告</h2>
			<c:forEach items="${noticeList }" var="nt">
				<h3 style="font-size: 13px;color: #666;text-indent:26px;">${nt.notice_desc }</h3>
			</c:forEach>
		</div>
	</c:if>
	
	<!-- 餐品开始部分 -->
	<div class="contain meal">
		<ul id="food" class="clearfix">
			<c:if test="${!empty map }">
				<c:forEach items="${map.foodInfos }" var="food">
					<li>
						<div>
							<div class="meal-img">
								<img style="height: 100%;width:100%" src="${root}${empty food.food_img ? '/images/default.jpg': food.food_img}"/> </div>
							<div class="meal-msg">
								<div class="msg-0">${food.food_name }</div>
								<div class="msg-1">${food.food_desc }</div>
								<div class="msg-21">
									套餐价格￥<span>${food.sell_price }</span>
								</div>
								<div class="msg-2">
									剩余<span>${food.food_num }</span>份
								</div>
								<a href="javascript:;" class="msg-3">+</a>
							</div>
							<input type="hidden" name="food_id" value='${food.food_id}'>
						</div>
					</li>
				</c:forEach>
			</c:if>
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
				<li class="shoppingcar-middle-top"><span class="middle-top-l">套餐</span>
					<span class="middle-top-r">份数</span></li>

			</ul>
		</div>
		<div class="shoppingcar-bottom">
			<div class="shoppingcar-bottom-l">
				<span class="buycar"> <img
					src="${root }orders/images/buycar.png">
				</span> <span class="pay">共￥<span id="totalMoney"></span>元
				</span>
			</div>
			<div class="shoppingcar-bottom-r">
				<!--  <span>选好了</span> -->
				<span>购物车是空的</span>
			</div>
		</div>
	</div>
	<!-- 购物车部分结束 -->

	<div class="mask"></div>

	<div id="choose">
		<div class="choose-title">指定人点餐</div>
		<lable>当前指定人：</lable>
		<span  id="zdPerson" ></span> <input type="hidden"
			id="staff" />
		<!-- <input type="text" id="staffName"  /> <input type="button" id="queryStaff" value="搜索"/> -->
		<div style="width: 100%; height: 20px;"></div>
		<lable>选择指定人：</lable>
<!-- 		<div class="txt2"> -->

<!-- 			<input type="text" id="txt2" /> -->
<!-- 		</div> -->
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



	<form name="myForm" id="myForm" action="${root }/order/goSumXOrders.do"
		method="POST">
		<input type="hidden" name="orderInfo" id="orderInfo" value="">
		<input type="hidden" name="vistorName" id="vistorName" >
		<input type="hidden" name="foodBusiness" id="foodBusiness">
	</form>
	<div style="height:50px;width:100%"></div>
	<!-- 引入css，层叠重复样式 -->
	<link rel="stylesheet" type="text/css"
		href="<%=basePath%>orders/css/ordering.css">
</body>

<script>
	//去预定
	function toYuDing(){
		window.location.href = "${root}order/toOrderingPageXOrders.do?type=YD";
	}

	//去订购产品
	function toBuyProducts(){
		window.location.href = "${root}product/toOrderingPageXProducts.do?type=BP";
	}
</script>
</html>
