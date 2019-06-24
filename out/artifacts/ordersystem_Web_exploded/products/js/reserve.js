$(function() {
	var totalNum=0,mealTime=0,mealtime=1;
	var Request = new Object(); 
    Request = GetRequest(); 
    if(typeof(Request.username)=="undefined"){
    	 $(".login").html('');
    }else{
    	$(".login").append(Request.username+'<span class="login-r">∨</span>')
    };
    if($('.meal li').length==0){
    	$(".meal ul li").html("暂无数据");
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
		if(((amSartTime <= dayTime && dayTime<= amEndTime) || (pmSartTime<=dayTime && dayTime<=pmEndTime))){
			
    	if($(this).prev().children().html()==0||$(this).prev().children().html()<0){
    		 $.MsgBox.Alert("消息", "该套餐已售完"); 
    		return;
    	};
    	
		var flag=false;
		var temp=false;
		/*判断是否有菜品*/
		if($("#append li").length==1){
			flag=true;
		};
		$("#append ul").each(function(i,e){
			var s=$(".object-middle select").val()+$(that).parents(".meal").prev().html();
			if($($(this).children()[0]).html()==s){
				flag=false;
				return false;
			}
			flag=true;
		})
		if(flag){
			$(".shoppingcar-middle-top").css({"display":"block"});
			$(".shoppingcar-top").css({"display":"block"});
			$(".pay").css({"display":"inline-block"});
			$(".shoppingcar-bottom-r span").html("选好了").css({"font-weight":"700"});
			var string="";
			var time=$(".object-middle select").val()+$(that).parents(".meal").prev().html();
			var app=document.getElementById("#append");
			var li=document.createElement("li");
			var append=document.createElement("ul");
			mealTime++;
			append.id="append"+mealTime+"";
			append.name=$(".week>span").html()+$(".time>span").html();
			li.id="append_li"+mealtime+"";
			/*li.name=$(".week>span").prop("className").substr(5,1)+$(".time>span").prop("className").substr(4,1);*/
			mealtime++;
			$("#append").append(li);
			$("#"+li.id).append(append);
			var string="",time=$(".object-middle select").val()+$(that).parents(".meal").prev().html();
			string+='<li class="everyMeal">'+time+'</li>';
			$("#"+append.id).append(string);
			
			if($("#append"+mealTime+" li span[class='middle-bot-l']").length==0){
				temp=true;
			};
		
		}
		
		var TIME=$(".object-middle select").val()+$(that).parents(".meal").prev().html();
		$("#append ul").each(function(i,e){
			if($($(e).children()[0]).html()==TIME){
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
			str+='<li class="shoppingcar-middle-bot">'
				str+='<input type="hidden" name="c_food_id"  value="'+food_id+'"/>'	
				str+='<span class="middle-bot-l">'+c+'</span>'
				str+='<span class="middle-bot-r">'
					str+='<span class="jian">-</span>'     
						str+='<span class="number">1</span>'
							str+='<span class="jia">+</span>'
								str+='</span>'
									str+='</li>';
			$("#append"+mealTime+"").append(str);
    		$(".jia").unbind('click').click(function(e){
    			console.log($(this));
        			var s=parseInt($(this).siblings("span[class='number']").html());
        			s++;
        			$(this).siblings("span[class='number']").html(s);
    		});
    		$(".jian").unbind('click').click(function(){
    			var n=parseInt($(this).siblings("span[class='number']").html());
    			n--;
    			if(n==0||n<0){
    				if($(this).parent().parent().siblings().length==1||$(this).parent().parent().siblings().length<1){
    					$(this).parent().parent().parent().parent().remove();
    				}
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
		
    	/*if($("#append"+mealTime+" li span[class='middle-bot-l']").length==0){
    			$(".shoppingcar-middle-top").css({"display":"block"});
    			$(".pay").css({"display":"inline-block"});
    			$(".shoppingcar-bottom-r span").html("选好了").css({"font-weight":"700"});
    			
    			var can=$($(that).siblings()[0]).html()+"("+$($(that).siblings()[1]).html()+")";
        		var  str='';
        		var food_id = $(that).parent().parent().find("input[name='food_id']").val();
        		str+='<li class="shoppingcar-middle-bot">'
        			str+='<input type="hidden" name="c_food_id"  value="'+food_id+'"/>'	
        			str+='<span class="middle-bot-l"></span>'
        			str+='<span class="middle-bot-r">'
	        			str+='<span class="jian">-</span>'     
	        			str+='<span class="number">1</span>'
	        			str+='<span class="jia">+</span>'
        			str+='</span>'
        			str+='</li>';
        		$("#append").append(str);
        		$("#totalMoney").html("25");
        		$(".middle-bot-l").html(can);
        		$(".jia").unbind('click').click(function(e){
	        			var s=parseInt($(this).siblings("span[class='number']").html());
	        			s++;
	        			$("#totalMoney").html(Number($("#totalMoney").html())+25);
	        			$(this).siblings("span[class='number']").html(s);
        		});
        		$(".jian").unbind('click').click(function(){
        			var n=parseInt($(this).siblings("span[class='number']").html());
        			n--;
        			$("#totalMoney").html(Number($("#totalMoney").html())-25);
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
    		}else{
    			购物车有菜品
    			$("#append li span[class='middle-bot-l']").each(function(i,e){
    				判断是否已有该菜品
    				if($(e).html().split('(')[0]==$($(that).siblings()[0]).html()){
    					var s=parseInt($(e).siblings().find("span[class='number']").html());
    					s++;
    					$("#totalMoney").html(Number($("#totalMoney").html())+25);
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
					strings+='<li class="shoppingcar-middle-bot">'
					strings+='<input type="hidden" name="c_food_id"  value="'+food_id+'"/>'	
					strings+='<span class="middle-bot-l">'+c+'</span>'
					strings+='<span class="middle-bot-r">'
					strings+='<span class="jian">-</span>'     
					strings+='<span class="number">1</span>'
					strings+='<span class="jia">+</span>'
					strings+='</span>'
					strings+='</li>';
	        		$("#append").append(strings);
	        		$(".jia").unbind('click').click(function(e){
	        			console.log($(this));
		        			var s=parseInt($(this).siblings("span[class='number']").html());
		        			s++;
		        			$(this).siblings("span[class='number']").html(s);
	        		});
	        		$(".jian").unbind('click').click(function(){
	        			var n=parseInt($(this).siblings("span[class='number']").html());
	        			n--;
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
				}
    			
    		}*/
    }else{
    	 $.MsgBox.Alert("消息", "不在订餐时间内"); 
		 return;
    	}
    });
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
    		
    	})
    })
    
    /*点击任意位置去掉遮罩*/
    $(".mask").click(function(){
        $(".mask").css({"display":"none"});
        $("#choose").css({"display":"none"});
    })
    /*结账跳转*/
    var b= true;
   $(".shoppingcar-bottom-r span").click(function(){
	   if($(".shoppingcar-bottom-r span").html()=="选好了"){
		   $("#vistorName").val($("#krName").val());
		   $("#orderInfo").val(JSON.stringify(getOrderInfo()));
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
		var times=[];
		$("#append>li.shoppingcar-middle-top").siblings().each(function(index,item){
			var week="";
			var weekname="";
			
			if($(item).find('ul li').attr('class')=='everyMeal'){
				week=$(item).children('ul')[0].name;
				weekname=$(item).find('ul li').html();
				times.push(week);
			}
			$(item).find('ul li.everyMeal').siblings().each(function(index,item){
				var detailOne = {};
				detailOne.week = weekname;	
				detailOne.weeknum = week;	
				detailOne.id = $(item).find("input[name='c_food_id']").val();			//id
				detailOne.food = $(item).find("span.middle-bot-l").html();				//套餐名（描述）
				detailOne.num = $(item).find("span.middle-bot-r  span.number").html();	//数量
				detailOne.total = 25*Number(detailOne.num);								//小计
				carDetail.push(detailOne);
			});
		});
		orderInfo.time=times.sort()[0];
		orderInfo.carDetail = carDetail;
	  return orderInfo;
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
   
   
});
