$(function() {
	var Request = new Object(); 
    Request = GetRequest(); 
    if(typeof(Request.username)=="undefined"){
    	 $(".login").html('');
    }else{
    	$(".login").append(Request.username+'<span class="login-r">∨</span>')
    };
    if($('.meal li').length==0){
    	$(".meal").html("暂无数据");
    	$(".meal").css({"padding-bottom":"20px","font-size":"16px"})
    }
    /*指定人点餐*/
    $("#point").click(function(){
    	if($(this)[0].checked){
        		$("#choose").css({"display":"block"});
            	$(".mask").css({"display":"block"});
            	$(this)[0].checked=false;
    	}
    })
    /*获取url里面的参数*/
    function GetRequest() { 
		var url = location.search; //获取url中"?"符后的字串 
		var theRequest = new Object(); 
		if (url.indexOf("?") != -1) { 
			var str = url.substr(1); 
			strs = str.split("&"); 
			for(var i = 0; i < strs.length; i ++) { 
						theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]); 
					} 
				} 
		return theRequest; 
	} 
    
    /*点击添加菜品按钮*/
    $(".msg-3").click(function(){
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
			
    	if($(this).prev().children().html()==0||$(this).prev().children().html()<0){
    		/*alert("该套餐已售完");*/
    		/*$.MsgBox.Alert("消息", data.msg,function(){
				$('.theme-popover-mask').fadeOut(100);
				$('#theme-popover').slideUp(200);
				
				$table.bootstrapTable('refresh')
			});*/
    		 $.MsgBox.Alert("消息", "该套餐已售完"); 
//    		$.MsgBox.Confirm("消息", "确定要点此套餐吗？",function(){
//    			alert(11111);
//    		});
    		return;
    	};
    	var num = $(this).prev().children().html();
    	num--;
    	$(this).prev().children().html(num);
    	var flag=false;
    	var that=$(this);
    	if($(".login").html()=="登录"){
    		$(".denglu").css({"display":"block"});
        	$(".mask").css({"display":"block"});
    	}else{
    		/*判断是否有菜品*/
    	if($("#append li span[class='middle-bot-l']").length==0){
    			$(".shoppingcar-middle-top").css({"display":"block"});
    			$(".shoppingcar-top").css({"display":"block"});
    			$(".pay").css({"display":"inline-block"});
    			$(".shoppingcar-bottom-r span").html("选好了").css({"font-weight":"700"});
    			
    			var can=$($(that).siblings()[0]).html()+"("+$($(that).siblings()[1]).html()+")";
        		var  str='';
        		var food_id = $(that).parent().parent().find("input[name='food_id']").val();
        		var sell_price = $(this).siblings('.msg-21').find("span").html();
        		str+='<li class="shoppingcar-middle-bot">'
        			str+='<input type="hidden" name="c_food_id"  value="'+food_id+'"/>'	
        			str+='<input type="hidden" name="c_sell_price"  value="'+sell_price+'"/>'	
        			str+='<span class="middle-bot-l"></span>'
        			str+='<span class="middle-bot-r">'
	        			str+='<span class="jian">-</span>'     
	        			str+='<span class="number">1</span>'
	        			str+='<span class="jia">+</span>'
        			str+='</span>'
        			str+='</li>';
        		$("#append").append(str);
        		$("#totalMoney").html($(this).siblings('.msg-21').find("span").html()); //$(this).siblings('.msg-21').find("span").html()
        		$(".middle-bot-l").html(can);
        		$(".jia").unbind('click').click(function(e){
        				var that=$(this) 
	        			var str=$(this).parent().prev().html().split('(')[0]
	        		    $(".msg-0").each(function(i,e){
	        		    	if(str==$(e).html()){
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
	 	 	        			$("#totalMoney").html(Number($("#totalMoney").html())+Number($(this).siblings('.msg-21').find("span").html())); //Number($(this).siblings('.msg-21').find("span").html())
	 	 	        			$(that).siblings("span[class='number']").html(s);
	        		    	}
	        		    })
        		});
        		$(".jian").unbind('click').click(function(){
        			var str=$(this).parent().prev().html().split('(')[0]
        		    $(".msg-0").each(function(i,e){
        		    	if(str==$(e).html()){
        		    		var num=$(this).siblings('.msg-2').find("span").html();
        		    		num++;
        		    		$(this).siblings('.msg-2').find("span").html(num);
        		    	}
        		    })
        			
        			var n=parseInt($(this).siblings("span[class='number']").html());
        			n--;
        			$("#totalMoney").html(Number($("#totalMoney").html())-Number($(this).parent().siblings("input[name='c_sell_price']").val())); //Number($(this).parent().siblings("input[name='c_sell_price']").val())
        			if(n==0){
	        			$(this).parent().parent().remove();
	        			if($("#append li span[class='middle-bot-l']").length==0){
	        				$("#append .shoppingcar-middle-top").css("display","none");
	        				$(".shoppingcar-top").css("display","none");
	        				$(".shoppingcar-bottom-r span").html("购物车是空的").css({"font-weight":"100"});
	        				$(".pay").css({"display":"none"});
	        			};
        			}
        			$(this).siblings("span[class='number']").html(n);
        		})
    		}else{
    			/*购物车有菜品*/
    			$("#append li span[class='middle-bot-l']").each(function(i,e){
    				/*判断是否已有该菜品*/
    				if($(e).html().split('(')[0]==$($(that).siblings()[0]).html()){
    					var s=parseInt($(e).siblings().find("span[class='number']").html());
    					s++;
    					$("#totalMoney").html(Number($("#totalMoney").html())+Number($(e).siblings("input[name='c_sell_price']").val())); //Number($(e).siblings("input[name='c_sell_price']").val())
    					$(e).siblings().find("span[class='number']").html(s);
    					flag=false;
    					return false;
    				};
    				flag=true;
    	    	});
    			if(flag){
					var  strings='';
					var c=$($(that).siblings()[0]).html()+"("+$($(that).siblings()[1]).html()+")";
					var food_id = $(that).parent().parent().find("input[name='food_id']").val();
					var sell_price = $(this).siblings('.msg-21').find("span").html();
					strings+='<li class="shoppingcar-middle-bot">'
					strings+='<input type="hidden" name="c_food_id"  value="'+food_id+'"/>'	
					strings+='<input type="hidden" name="c_sell_price"  value="'+sell_price+'"/>'	
					strings+='<span class="middle-bot-l">'+c+'</span>'
					strings+='<span class="middle-bot-r">'
					strings+='<span class="jian">-</span>'     
					strings+='<span class="number">1</span>'
					strings+='<span class="jia">+</span>'
					strings+='</span>'
					strings+='</li>';
	        		$("#append").append(strings);
	        		$("#totalMoney").html(Number($("#totalMoney").html())+Number(sell_price));
	        		$(".jia").unbind('click').click(function(e){
	        			var that=$(this) 
	        			var str=$(this).parent().prev().html().split('(')[0]
	        		    $(".msg-0").each(function(i,e){
	        		    	if(str==$(e).html()){
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
	 	 	        			$("#totalMoney").html(Number($("#totalMoney").html())+Number($(this).siblings('.msg-21').find("span").html())); //Number($(this).siblings('.msg-21').find("span").html())
	 	 	        			$(that).siblings("span[class='number']").html(s);
	        		    	}
	        		    })
	        		});
	        		$(".jian").unbind('click').click(function(){
	        			var str=$(this).parent().prev().html().split('(')[0]
	        		    $(".msg-0").each(function(i,e){
	        		    	if(str==$(e).html()){
	        		    		var num=$(this).siblings('.msg-2').find("span").html();
	        		    		num++;
	        		    		$(this).siblings('.msg-2').find("span").html(num);
	        		    	}
	        		    })
	        			
	        			var n=parseInt($(this).siblings("span[class='number']").html());
	        			n--;
	        			$("#totalMoney").html(Number($("#totalMoney").html())-Number($(this).parent().siblings("input[name='c_sell_price']").val())); //Number($(this).parent().siblings("input[name='c_sell_price']").val())
	        			if(n==0){
		        			$(this).parent().parent().remove();
		        			if($("#append li span[class='middle-bot-l']").length==0){
		        				$("#append .shoppingcar-middle-top").css("display","none");
		        				$(".shoppingcar-top").css("display","none");
		        				$(".shoppingcar-bottom-r span").html("购物车是空的").css({"font-weight":"100"});
		        				$(".pay").css({"display":"none"});
		        			};
	        			}
	        			$(this).siblings("span[class='number']").html(n);
	        		})
				}
    			
    		}
    	}
    }else{
    	 $.MsgBox.Alert("消息", "不在订餐时间内"); 
		 return;
    	}
    });
    
    /*点击任意位置去掉遮罩*/
    $(".mask").click(function(){
        $(".mask").css({"display":"none"});
        $("#choose").css({"display":"none"});
    })
      /*清空购物车*/
    $(".shoppingcar-top-r").click(function(){
    	$.MsgBox.Confirm("提示","确定清空购物车内所有物品吗？",function(){
    		var str='';
    		    str+='<li class="shoppingcar-middle-top">';
    			str+='<span class="middle-top-l">套餐</span>';
    			str+='<span class="middle-top-r">份数</span>';
    			str+='</li>';
    		$("#append").html(str);
    		$("#append .shoppingcar-middle-top").css("display","none");
			$(".shoppingcar-top").css("display","none");
			 location.reload();
    	})
    })
    /*隐藏购物车*/
    $(".shoppingcar-bottom-l").click(function(){
    	if($(".shoppingcar-bottom-r span").html()=="选好了"){
    		$(".shoppingcar-top").slideToggle();
    		$(".shoppingcar-middle").slideToggle();
    	}
    });
    /*结账跳转*/
    var b= true;
   $(".shoppingcar-bottom-r span").click(function(){
	   if($(".shoppingcar-bottom-r span").html()=="选好了"){
		   $("#vistorName").val($("#krName").val());
		   $("#orderInfo").val(JSON.stringify(getOrderInfo()));
		   $("#foodBusiness").val($("select[name='foodBusinessSelect']").val());
		  var data = $("#myForm").serialize();
		  if(b){
			  $("#myForm").submit();
		  }
	   };
	   
   })
   
   /* 购物车信息 */
   function  getOrderInfo(){
	   b = true;
    	var orderInfo = {};
    	//订餐对象(ZJ:自己、KR：客人、LD：领导、ZD：指定)
		var orderObj = $(".object-middle input:radio:checked").val();
		orderInfo.orderObj = orderObj;
		if(orderObj == "LD"){
			//领导ID
			orderInfo.ldId = $("#ld").val();
		}else if(orderObj == "ZD"){
			//解决re-orderInfo.jsp页面中的 “扣除账户”以及“剩余” 的回显问题
			orderInfo.orderObj = "ZJ";
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
		$("#append").find("li.shoppingcar-middle-bot").each(function(index,item){
			var detailOne = {};
			detailOne.id = $(item).find("input[name='c_food_id']").val();			//id
			detailOne.price = $(item).find("input[name='c_sell_price']").val();		//套餐价格
			detailOne.food = $(item).find("span.middle-bot-l").html();				//套餐名（描述）
			detailOne.num = $(item).find("span.middle-bot-r  span.number").html();	//数量
			detailOne.total = Number(detailOne.price)*Number(detailOne.num);								//小计
			carDetail.push(detailOne);
		});
		
		orderInfo.carDetail = carDetail;
	  return orderInfo;
    }

});
