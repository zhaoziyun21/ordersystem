$(function(){
	var totalNum=0;
	/* 返回上一页 */
	$(".back").click(function(){
		window.location.href="${root}wechat/toIndexWeChat.do";
		//window.history.back();
	})
	
	/*点击购物车*/
$(".footer-l").click(function(){
	if($("#append li").length!=0){
		$("#mask").slideToggle("fast");
		$(".buyCar").slideToggle("fast");
	}
})
	/*点击结算*/
$(".footer-r").click(function(){
	if($("#append li").length!=0){
		$("#mask").slideToggle("fast");
		$(".buyCar").slideToggle("fast");
	}
})
$("#mask").click(function(){
	$("#mask").slideToggle("fast");
	$(".buyCar").slideToggle("fast");
})
/*点击加号，添加菜品*/
$(".jia").click(function(){
	if($(this).prev().html() == 0 || $(this).prev().html() < 0){
		 $.MsgBox.Alert("消息", "套餐不足"); 
		 return false;
	}
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
	if(dayTime >= pmSartTime){
		 $.MsgBox.Alert("消息", "晚餐暂不开放");
		 return false;
	}
	
	if(((amSartTime <= dayTime && dayTime<= amEndTime) || (pmSartTime<=dayTime && dayTime<=pmEndTime))){
		totalNum++;
		var num = $(this).prev().html();
		num--;
		$(this).prev().html(num);
		$(".car .totalNum").html(totalNum);
		
		$(".car .totalPrice").val(Number($(".car .totalPrice").val())+Number($(this).parent().prev().find("span").html()));
		$(".totalPay").html("总共消费"+$(".car .totalPrice").val()+"元");
		var that=this;
		var flag=false;
		if($("#append li div[class='car-desc']").length==0){
			flag=true;
		};
		$("#append li div[class='car-desc']").each(function(i,e){
			if($(e).html().split('(')[0]==$(that).parent().parent().find(".mealName").html()){
				var s=parseInt($(e).siblings().find("span[class='number']").html());
				s++;
				$(e).siblings().find("span[class='number']").html(s);
				flag=false;
				return false;
			}
			flag=true;
		})
		if(flag){
			var str="";
			var can=$($($(this).parent().siblings()[0]).children()).html()+"("+$($($(this).parent().siblings()[1]).children()).html()+")";
			var food_id = $(this).parent().siblings("input[name='food_id']").val(); 
			var sell_price = $(this).parent().prev().find("span").html(); 
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
			$("#append").append(str);
			$(".reduce").off().on("click",function(){
				
				var str=$(this).parent().prev().html().split('(')[0];
    		    $(".mealName").each(function(i,e){
    		    	if(str==$(e).html()){
    		    		var num=$(this).parent().siblings().find(".numClass").html();
    		    		num++;
    		    		$(this).parent().siblings().find(".numClass").html(num);
    		    		
    		    		$(".car .totalPrice").val(Number($(".car .totalPrice").val())-Number($(this).parent().siblings().find(".priceClass").html())); //Number($(".car .totalPrice").val())-Number($(this).parent().siblings().find(".priceClass").html())
    		    		$(".totalPay").html("总共消费"+$(".car .totalPrice").val()+"元");
    		    	}
    		    })
    		    
				totalNum--;
				$(".car .totalNum").html(totalNum);
				var s=$(this).siblings(".number").html();
				s--;
				if(s==0||s<0){
					$(this).parent().parent().remove();
				}
				if($("#append li").length==0){
					$("#mask").css("display","none");
					$(".buyCar").css("display","none");
					$(".totalPay").html("选点什么吧！")
				}
				$(this).siblings(".number").html(s);
			})
			$(".add").off().on("click",function(){
				var that=$(this)
				var str=$(this).parent().prev().html().split('(')[0];
				$(".mealName").each(function(i,e){
					if(str==$(e).html()){
			    		var num=$(this).parent().siblings().find(".numClass").html();
			    		if(num==0||num<0){
			    			num=0;
			    			$.MsgBox.Alert("消息", "该套餐数量不足"); 
			    			return;
			    		}
			    		num--;
			    		$(this).parent().siblings().find(".numClass").html(num);
			    		$(".car .totalPrice").val(Number($(".car .totalPrice").val())+Number($(this).parent().siblings().find(".priceClass").html()));
			    		$(".totalPay").html("总共消费"+$(".car .totalPrice").val()+"元");
			    		
			    		totalNum++;
						$(".car .totalNum").html(totalNum);
						var s=$(that).siblings(".number").html();
						s++;
						$(that).siblings(".number").html(s);
					}
				})
				
			})
		}
		//$(".totalPay").html("总共消费"+$(".car .totalPrice").val()+"元");
	}else{
		 $.MsgBox.Alert("消息", "不在订餐时间内"); 
		 return;
	}
	
})

		$(".reduce").off().on("click",function(){
			
			var str=$(this).parent().prev().html().split('(')[0];
		    $(".mealName").each(function(i,e){
		    	if(str==$(e).html()){
		    		var num=$(this).parent().siblings().find(".numClass").html();
		    		num++;
		    		$(this).parent().siblings().find(".numClass").html(num);
		    	}
		    })
			
			totalNum--;
			$(".car .totalNum").html(totalNum);
			var s=$(this).siblings(".number").html();
			s--;
			if(s==0||s<0){
				$(this).parent().parent().remove();
			}
			if($("#append li").length==0){
				$("#mask").css("display","none");
				$(".buyCar").css("display","none");
				$(".totalPay").html("选点什么吧！")
			}
			$(this).siblings(".number").html(s);
		})
		$(".add").off().on("click",function(){
			var that=$(this)
			var str=$(this).parent().prev().html().split('(')[0];
			$(".mealName").each(function(i,e){
				if(str==$(e).html()){
		    		var num=$(this).parent().siblings().find(".numClass").html();
		    		if(num==0||num<0){
		    			num=0;
		    			$.MsgBox.Alert("消息", "该套餐数量不足"); 
		    			return;
		    		}
		    		num--;
		    		$(this).parent().siblings().find(".numClass").html(num);
		    		totalNum++;
					$(".car .totalNum").html(totalNum);
					var s=$(that).siblings(".number").html();
					s++;
					$(that).siblings(".number").html(s);
				}
			})
		})
		/*购物车清空*/
		$(".buyCar-top-r").click(function(){
			$.MsgBox.Confirm("提示","是否要清空购物车内所有物品？",function(){
			$("#append li").remove();
			$("#mask").css("display","none");
			$(".buyCar").css("display","none");
			$(".totalPay").html("选点什么吧！");
			totalNum=0;
			$(".car .totalNum").html(totalNum);
			location.reload();
			})
		})
		
		
		/*结账跳转*/
	    var b= true;
	   $("div[class='footer-r']").click(function(){
		   if($(".footer-l span").html()!="选点什么吧！"){
			   $("#vistorName").val();
			   $("#orderInfo").val(JSON.stringify(getOrderInfo()));
			  var data = $("#myForm").serialize();
			  if(b){
				  $("#myForm").submit();
			  }
		   }else{
			   $.MsgBox.Alert("消息", "购物车为空！"); 
		   }
	   })
		/* 购物车信息 */
		   function  getOrderInfo(){
			   b = true;
		    	var orderInfo = {};
		    	//订餐对象(ZJ:自己、KR：客人、LD：领导、ZD：指定)
		    	var orderObj="";
		    	var type =$("#type").val();
		    	switch (type) {
				case '0':
					orderObj='ZJ';
					break;
				case '1':
					orderObj='KR';
					break;
				case '2':
					orderObj='LD';
				break;
				}
				orderInfo.orderObj = orderObj;
				if(orderObj == "LD"){
					//领导ID
					orderInfo.ldId = $("#leadId").val();
				}else if(orderObj == "ZD"){
					//被指定人ID
					orderInfo.zdrId = "指定人的id，等tree出来";
				}else if(orderObj == "KR"){
					var vistorName = $("#vistorName").val();
						vistorName = vistorName.replace(/\s/g, "");
					if(vistorName == null || vistorName == ""){
						$.MsgBox.Alert("消息", "客人姓名不能为空！");
						b = false;
						return false;
					}
					
				}
				
				//购物车套餐明细
				var carDetail = [];
				$("#append li ").each(function(index,item){
					var detailOne = {};
					detailOne.id = $(item).find("input[name='c_food_id']").val();			//id
					detailOne.price = $(item).find("input[name='c_sell_price']").val();		//套餐价格
					detailOne.food = $(item).find("div[class='car-desc']").html();			//套餐名（描述）
					detailOne.num = $(item).find("div[class='car-num']  span.number").html(); //数量
					detailOne.total = Number(detailOne.price)*Number(detailOne.num);								//小计
					carDetail.push(detailOne);
				});
				
				orderInfo.carDetail = carDetail;
			  return orderInfo;
		    }
})