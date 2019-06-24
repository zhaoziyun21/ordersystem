$(function(){
	var totalNum=0,mealTime=0,mealtime=1;
	/* 返回上一页 */
	$(".back").click(function(){
		window.location.href="${root}wechat/toIndexWeChat.do";
		//window.history.back();
	})
	/*选择订餐时间*/
	$(".week>span").click(function(){
		$("#mask").css("display","block");
		$(".time ul").css("display","none");
		$(".week ul").slideDown("normal");
	})
	$(".week li").click(function(){
		$(".week li span").removeClass();
		$(".week li div").removeClass();
		$(this).find("span").addClass("choose");
		$(this).find("div").addClass("choosed");
		$(".week>span").html($(this).find("span").html());
		var s=$(this).find("span")[0].id;
		$(".week>span").removeClass();
		$(".week>span").addClass(s);
		$("#mask").css("display","none");
		$(".week ul").css("display","none");
		$(".buyCar").css("display","none");

		loadFood($(".week>span").attr("class").toUpperCase(),$(".time>span").attr("class").toUpperCase().substring(0,3));
	});
	$(".time>span").click(function(){
		$("#mask").css("display","block");
		$(".week ul").css("display","none");
		$(".time ul").slideDown("normal");
	
	}) 
	$(".time li").click(function(){
		$(".time li span").removeClass();
		$(".time li div").removeClass();
		$(this).find("span").addClass("choose");
		$(this).find("div").addClass("choosed");
		$(".time>span").html($(this).find("span").html());
		var s=$(this).find("span")[0].id;
		$(".time>span").removeClass();
		$(".time>span").addClass(s);
		$("#mask").css("display","none");
		$(".time ul").css("display","none");
		$(".buyCar").css("display","none");
		
		loadFood($(".week>span").attr("class").toUpperCase(),$(".time>span").attr("class").toUpperCase().substring(0,3));
	});
	
	
	$("#maska").click(function(){
		$("#maska").slideToggle("fast");
		$(".buyCar").css("display","none");
		$(".clearfixa ul").css("display","none");
		$(".clearfixa ul").css("display","none");
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
	$(".buyCar").css("display","none");
	$(".week ul").css("display","none");
	$(".time ul").css("display","none");
})
/*点击加号，添加菜品*/
$(".jia").click(function(){
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
	if(((amSartTime <= dayTime && dayTime<= amEndTime) || (pmSartTime<=dayTime && dayTime<=pmEndTime))){
		totalNum++;
		$(".car .totalNum").html(totalNum);
		var that=this;
		var flag=false;
		var temp=false;
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
		var TIME=$(".week>span").html()+$(".time>span").html();
		$("#append ul").each(function(){
			if($($(this).children()[0]).html()==TIME){
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
			$("#append"+mealTime+"").append(str);
			$(".reduce").off().on("click",function(){
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
				
				if($("#append li").length==0){
					$("#mask").css("display","none");
					$(".buyCar").css("display","none");
					$(".totalPay").html("选点什么吧！")
				}
				$(this).siblings(".number").html(s);
			})
			$(".add").off().on("click",function(){
				totalNum++;
				$(".car .totalNum").html(totalNum);
				var s=$(this).siblings(".number").html();
				s++;
				$(this).siblings(".number").html(s);
			})
		}
		$(".totalPay").html("总共消费"+totalNum*25+"元");
	}else{
		 $.MsgBox.Alert("消息", "不在订餐时间内"); 
		 return;
	}
	
});

		$(".reduce").off().on("click",function(){
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
			totalNum++;
			$(".car .totalNum").html(totalNum);
			var s=$(this).siblings(".number").html();
			s++;
			$(this).siblings(".number").html(s);
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
		  // alert(1)
		   if($(".footer-l span").html()!="选点什么吧！"){
			   $("#orderInfo").val(JSON.stringify(getOrderInfo()));
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
				}/*else if(orderObj == "ZD"){
					//被指定人ID
					orderInfo.zdrId = "指定人的id，等tree出来";
				}*/else if(orderObj == "KR"){
					var vistorName = $("#vistorName").val();
						vistorName = vistorName.replace(/\s/g, "");
					if(vistorName == null || vistorName == ""){
						$.MsgBox.Alert("消息", "客人姓名不能为空！");
						b = false;
						return false;
					}
					
				}
				
				var names = [];
				//购物车套餐明细
				var carDetail = [];
				$("#append>li ").each(function(index,item){
						var week = "";
						names.push(item.name);
						$(item).find("ul>li").each(function(ind,it){
							if($(it).attr("class")=="everyMeal"){
								week = $(it).text();
							}else{
								var detailOne = {};
								detailOne.week = week;
								detailOne.weeknum = item.name;
								detailOne.id = $(it).find("input[name='c_food_id']").val();
								detailOne.price = $(it).find("input[name='c_sell_price']").val();
								detailOne.food = $(it).find("div.car-desc").text();
								detailOne.num = $(it).find("div.car-num").find("span.number").text();
								detailOne.total = Number(detailOne.price)*Number(detailOne.num);	
								carDetail.push(detailOne);
							}
						});
				});
				orderInfo.time = names.sort()[0];
				orderInfo.carDetail = carDetail;
			  return orderInfo;
		    }
})