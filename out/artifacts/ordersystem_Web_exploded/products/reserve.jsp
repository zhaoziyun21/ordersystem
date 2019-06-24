<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/public/TagLib.jsp"%>
<%@ include file="../../common/public/baseStyle.jsp"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>在线超市</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>products/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>products/css/reserve.css">
    <script type="text/javascript" src="<%=basePath%>products/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>products/js/msgbox.js"></script>
    <%-- <script type="text/javascript" src="<%=basePath%>products/js/reserve.js"></script> --%>

</head>
<script>
	var totalNum=0, mealTime=0, mealtime=1, element=null;
	var b = true;
	
	function isIE() { //ie?
    	if (!!window.ActiveXObject || "ActiveXObject" in window){
    		return true;
		}else{
			return false;
		}
	}
    $(function(){
		if(isIE()){
			$('.object-middle select').removeClass("selects");
		}else{
			$('.object-middle select').addClass("selects");
		};
    })
    
    $(function(){
		var productContent = $("#productContent");
		//清空内容
		productContent.html("");
		var prohtml = "";
		$.ajax({
			url:"${root}product/toBPPageXProducts.do",
			dataType:"json",
			type:"post",
			success:function(data){
				if(data != null ){
					//产品列表展示
					for(var i = 0; i < data.productList.length; i ++){
						prohtml += "<li> ";
			            prohtml += "    <div>";
			            prohtml += "<input type=\"hidden\" name=\"pro_code\" value=\""+data.productList[i]["pro_code"]+"\"/>";
			            prohtml += "<input type=\"hidden\" name=\"pro_price\" value=\""+data.productList[i]["pro_price"]+"\"/>";
			            prohtml += "        <div class=\"meal-img\"><img style=\"width:100%;height:100%;\" src=\"${root}"+((data.productList[i]["pro_image_url"] =="" ||data.productList[i]["pro_image_url"] ==null)?"/images/default.jpg": data.productList[i]["pro_image_url"])+ "\"/></div> ";
			            prohtml += "        <div class=\"meal-msg\"> ";
			            prohtml += "        	<div class=\"msg-0\">"+data.productList[i]["pro_name"]+"</div> ";
			            prohtml += "            <div class=\"msg-1\">"+data.productList[i]["pro_describe"]+"</div> ";
			            prohtml += "            <div class=\"msg-2\"><b>库存余量 : </b><span>"+data.productList[i]["pro_remain"]+"</span> 件 </div> ";
			            prohtml += "            <div class=\"msg-21\"><b>商品价格 : </b><span> ￥"+data.productList[i]["pro_price"]+"</span>  </div> ";
			            
			            if(data.productList[i]["pro_reference_price"] != null && data.productList[i]["pro_reference_price"] != ""){
			            	//text-decoration:line-through;
				            prohtml += "        <div style='margin-left: 18px; margin-top: 13px;'> <span  style='border:1px solid blue;color: blue;'>"+data.productList[i]["pro_reference_price"]+"</span></div> ";
			            }
			            
			            prohtml += "            <a class=\"msg-6\" href="+data.productList[i]["pro_out_url"]+"><b>外链地址 : </b> "+data.productList[i]["pro_out_url"]+"</a> ";
			            prohtml += "            <a href=\"javascript:;\" class=\"msg-3\">+</a> ";
			            prohtml += "        </div> ";
			            prohtml += "    </div> ";
			            prohtml += "</li> ";
					}
					$(productContent).html(prohtml);
					
					//点击“+”按钮触发事件
					$(".msg-3").click(function(){
						//获取当前时间
						var dayTime = new Date();
						var year = dayTime.getFullYear(); //年
						var month = dayTime.getMonth()+1; //月
						var day = dayTime.getDate(); //日
						var currentTime = year+"-"+month+"-"+day;
						
						var that=this;
						if($(this).parent().find(".msg-2 span").html()==0||$(this).parent().find(".msg-2 span").html()<0){
    		 				$.MsgBox.Alert("消息", "该产品已售完，您可以通过外链地址进行购买"); 
    						return;
    					};
    					var num  = $(this).parent().find(".msg-2 span").html();
    					num--;
    					$(this).parent().find(".msg-2 span").html(num);
    					
    					//往购物车添加产品start............................
    					var flag = false;
						var temp = false;
						/*判断是否有菜品*/
						if($("#append li").length == 1){
							flag=true;
						};
						$("#append ul").each(function(i,e){
							var s=currentTime;
							if($($(this).children()[0]).html()==s){
								flag=false;
								return false;
							}
							flag=true;
						})
						
						//购物车产品价格计算
						$("#totalMoney").html(parseInt($("#totalMoney").html())+parseInt($(that).parent().parent().find("input[name='pro_price']").val()));
						
						if(flag){
							$(".shoppingcar-middle-top").css({"display":"block"});
							$(".shoppingcar-top").css({"display":"block"});
							$(".pay").css({"display":"inline-block"});
							$(".shoppingcar-bottom-r span").html("选好了").css({"font-weight":"700"});
							
							var li = document.createElement("li");
							var append = document.createElement("ul");
							mealTime ++;
							append.id = "append" + mealTime + "";
							append.name = $(".week>span").html() + $(".time>span").html();
							li.id = "append_li" + mealtime + "";
							mealtime ++;
							$("#append").append(li);
							$("#" + li.id).append(append);
							
							var string ="", time=currentTime;
							string += '<li class="everyMeal">'+time+'</li>';
							$("#"+append.id).append(string);
							
							if($("#append"+mealTime+" li span[class='middle-bot-l']").length==0){
								temp=true;
							};
						}
						
						var TIME = currentTime;
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
							var str='';
							var c=$($(that).siblings()[0]).html()+"("+$($(that).siblings()[1]).html()+")";
							var pro_code = $(that).parent().parent().find("input[name='pro_code']").val();
							var pro_price = $(that).parent().parent().find("input[name='pro_price']").val();
							str+='<li class="shoppingcar-middle-bot">'
								str+='<input type="hidden" name="c_pro_code"  value="'+pro_code+'"/>'	
								str+='<input type="hidden" name="c_pro_price"  value="'+pro_price+'"/>'	
								str+='<span class="middle-bot-l">'+c+'</span>'
								str+='<span class="middle-bot-r">'
									str+='<span class="jian">-</span>'     
										str+='<span class="number">1</span>'
											str+='<span class="jia">+</span>'
												str+='</span>'
													str+='</li>';
							$("#append"+mealTime+"").append(str);
							
							//购物车中的“+”事件
				    		$(".jia").unbind('click').click(function(e){
			        			var that=$(this);
			        		    var str=$(this).parent().prev().html().split('(')[0]
			        		    $(".msg-0").each(function(i,e){
			        		    	if(str==$(e).html()){
			        		    		var num=$(this).siblings('.msg-2').find("span").html();
			        		    		if(num==0||num<0){
			        		    			num=0;
			        		    			$.MsgBox.Alert("消息", "该产品数量不足，您可以通过外链地址进行购买"); 
			        		    			return;
			        		    		}
			        		    		num--;
			        		    		$(this).siblings('.msg-2').find("span").html(num);
			        		    		
			        		    		var s=parseInt($(that).siblings("span[class='number']").html());
			                			s++;
			                			//购物车产品价格计算toFixed(1)
			                			$("#totalMoney").html(parseInt($("#totalMoney").html())+parseInt($(that).parent().parent().find("input[name='c_pro_price']").val()))
			                			$(that).siblings("span[class='number']").html(s);
			        		    	}
			        		    })
				    		});
				    		//购物车中的“-”事件
				    		$(".jian").unbind('click').click(function(){
				    			var that=$(this);
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
				    			//购物车产品价格计算
				    			$("#totalMoney").html(parseInt($("#totalMoney").html())-parseInt($(this).parent().parent().find("input[name='c_pro_price']").val()));
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
						//往购物车添加产品end............................
						
						//清空购物车
					    $(".shoppingcar-top-r").click(function(){
					    	$.MsgBox.Confirm("提示","确定清空购物车内所有物品吗？",function(){
					    		var str='';
					    		    str+='<li class="shoppingcar-middle-top">';
					    			str+='<span class="middle-top-l">产品</span>';
					    			str+='<span class="middle-top-r">件数</span>';
					    			str+='</li>';
					    		$("#append").html(str);
					    		$("#append .shoppingcar-middle-top").css("display","none");
								$(".shoppingcar-top").css("display","none");
					    		//重新加载页面
					    		location.reload();
					    	})
					    })
					    
					    //点击任意位置去掉遮罩
					    $(".mask").click(function(){
					        $(".mask").css({"display":"none"});
					        $("#choose").css({"display":"none"});
					    })
					    
					    //结账跳转
					    //var b= true;
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
						
					});
					
				}
			}
		})
		
		//级联查询被指定人
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
		
		//点击“确定”按钮
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
		
		//点击“搜索”按钮
		$("#queryProducts").click(function(){
			var productContent = $("#productContent");
			//清空内容
			productContent.html("");
			var prohtml = "";
			var proName = $("#proName").val();
			var proCateId = $("#proCateId").val();
			$.ajax({
				url:"${root}/product/toBPPageXProducts.do",    //请求的url地址
				dataType:"json",   //返回格式为json
				type:"POST",   //请求方式
				data:{
					"proName":proName,
					"proCateId":proCateId
				},    
				success:function(data){
					if(data != null){
						//产品列表展示
						for(var i = 0; i < data.productList.length; i ++){
							prohtml += "<li> ";
				            prohtml += "    <div>";
				            prohtml += "<input type=\"hidden\" name=\"pro_code\" value=\""+data.productList[i]["pro_code"]+"\"/>";
				            prohtml += "<input type=\"hidden\" name=\"pro_price\" value=\""+data.productList[i]["pro_price"]+"\"/>";
				            prohtml += "        <div class=\"meal-img\"><img style=\"width:100%;height:100%;\" src=\"${root}"+((data.productList[i]["pro_image_url"] =="" ||data.productList[i]["pro_image_url"] ==null)?"/images/default.jpg": data.productList[i]["pro_image_url"])+ "\"/></div> ";
				            prohtml += "        <div class=\"meal-msg\"> ";
				            prohtml += "        	<div class=\"msg-0\">"+data.productList[i]["pro_name"]+"</div> ";
				            prohtml += "            <div class=\"msg-1\">"+data.productList[i]["pro_describe"]+"</div> ";
				            prohtml += "            <div class=\"msg-2\"><b>库存余量 : </b><span>"+data.productList[i]["pro_remain"]+"</span> 件 </div> ";
				            prohtml += "            <div class=\"msg-21\"><b>商品价格 : </b><span> ￥"+data.productList[i]["pro_price"]+"</span>  </div> ";
				            
				            if(data.productList[i]["pro_reference_price"] != null && data.productList[i]["pro_reference_price"] != ""){
				            	//text-decoration:line-through;
					            prohtml += "        <div style='margin-left: 18px; margin-top: 13px;'> <span  style='border:1px solid blue;color: blue;'>"+data.productList[i]["pro_reference_price"]+"</span></div> ";
				            }
				            
				            prohtml += "            <a class=\"msg-6\" href="+data.productList[i]["pro_out_url"]+"><b>外链地址 : </b> "+data.productList[i]["pro_out_url"]+"</a> ";
				            prohtml += "            <a href=\"javascript:;\" class=\"msg-3\">+</a> ";
				            prohtml += "        </div> ";
				            prohtml += "    </div> ";
				            prohtml += "</li> ";
						}
						$(productContent).html(prohtml);
						
						//点击“+”按钮触发事件
						$(".msg-3").click(function(){
							//获取当前时间
							var dayTime = new Date();
							var year = dayTime.getFullYear(); //年
							var month = dayTime.getMonth()+1; //月
							var day = dayTime.getDate(); //日
							var currentTime = year+"-"+month+"-"+day;
							
							var that=this;
							if($(this).parent().find(".msg-2 span").html()==0||$(this).parent().find(".msg-2 span").html()<0){
    		 				$.MsgBox.Alert("消息", "该产品已售完，您可以通过外链地址进行购买"); 
	    						return;
	    					};
	    					var num  = $(this).parent().find(".msg-2 span").html();
	    					num--;
	    					$(this).parent().find(".msg-2 span").html(num);
	    					
	    					//往购物车添加产品start............................
	    					var flag = false;
							var temp = false;
							/*判断是否有菜品*/
							if($("#append li").length == 1){
								flag=true;
							};
							$("#append ul").each(function(i,e){
								var s=currentTime;
								if($($(this).children()[0]).html()==s){
									flag=false;
									return false;
								}
								flag=true;
							})
							
							//购物车产品价格计算
							$("#totalMoney").html(parseInt($("#totalMoney").html())+parseInt($(that).parent().parent().find("input[name='pro_price']").val()));
							
							if(flag){
								$(".shoppingcar-middle-top").css({"display":"block"});
								$(".shoppingcar-top").css({"display":"block"});
								$(".pay").css({"display":"inline-block"});
								$(".shoppingcar-bottom-r span").html("选好了").css({"font-weight":"700"});
								
								var li = document.createElement("li");
								var append = document.createElement("ul");
								mealTime ++;
								append.id = "append" + mealTime + "";
								append.name = $(".week>span").html() + $(".time>span").html();
								li.id = "append_li" + mealtime + "";
								mealtime ++;
								$("#append").append(li);
								$("#" + li.id).append(append);
								
								var string ="", time=currentTime;
								string += '<li class="everyMeal">'+time+'</li>';
								$("#"+append.id).append(string);
								
								if($("#append"+mealTime+" li span[class='middle-bot-l']").length==0){
									temp=true;
								};
							}
							
							var TIME = currentTime;
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
								var str='';
								var c=$($(that).siblings()[0]).html()+"("+$($(that).siblings()[1]).html()+")";
								var pro_code = $(that).parent().parent().find("input[name='pro_code']").val();
								var pro_price = $(that).parent().parent().find("input[name='pro_price']").val();
								str+='<li class="shoppingcar-middle-bot">'
									str+='<input type="hidden" name="c_pro_code"  value="'+pro_code+'"/>'	
									str+='<input type="hidden" name="c_pro_price"  value="'+pro_price+'"/>'	
									str+='<span class="middle-bot-l">'+c+'</span>'
									str+='<span class="middle-bot-r">'
										str+='<span class="jian">-</span>'     
											str+='<span class="number">1</span>'
												str+='<span class="jia">+</span>'
													str+='</span>'
														str+='</li>';
								$("#append"+mealTime+"").append(str);
								
								//购物车中的“+”事件
					    		$(".jia").unbind('click').click(function(e){
				        			var that=$(this);
				        		    var str=$(this).parent().prev().html().split('(')[0]
				        		    $(".msg-0").each(function(i,e){
				        		    	if(str==$(e).html()){
				        		    		var num=$(this).siblings('.msg-2').find("span").html();
				        		    		if(num==0||num<0){
				        		    			num=0;
				        		    			$.MsgBox.Alert("消息", "该产品数量不足，您可以通过外链地址进行购买"); 
				        		    			return;
				        		    		}
				        		    		num--;
				        		    		$(this).siblings('.msg-2').find("span").html(num);
				        		    		
				        		    		var s=parseInt($(that).siblings("span[class='number']").html());
				                			s++;
				                			//购物车产品价格计算toFixed(1)
				                			$("#totalMoney").html(parseInt($("#totalMoney").html())+parseInt($(that).parent().parent().find("input[name='c_pro_price']").val()))
				                			$(that).siblings("span[class='number']").html(s);
				        		    	}
				        		    })
					    		});
					    		//购物车中的“-”事件
					    		$(".jian").unbind('click').click(function(){
					    			var that=$(this);
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
					    			//购物车产品价格计算
					    			$("#totalMoney").html(parseInt($("#totalMoney").html())-parseInt($(this).parent().parent().find("input[name='c_pro_price']").val()));
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
							//往购物车添加产品end............................
							
							//清空购物车
						    $(".shoppingcar-top-r").click(function(){
						    	$.MsgBox.Confirm("提示","确定清空购物车内所有物品吗？",function(){
						    		var str='';
						    		    str+='<li class="shoppingcar-middle-top">';
						    			str+='<span class="middle-top-l">产品</span>';
						    			str+='<span class="middle-top-r">件数</span>';
						    			str+='</li>';
						    		$("#append").html(str);
						    		$("#append .shoppingcar-middle-top").css("display","none");
									$(".shoppingcar-top").css("display","none");
						    		//重新加载页面
						    		location.reload();
						    	})
						    })
						    
						    //点击任意位置去掉遮罩
						    $(".mask").click(function(){
						        $(".mask").css({"display":"none"});
						        $("#choose").css({"display":"none"});
						    })
						    
						    //结账跳转
						    //var b= true;
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
							
						})
					}
				}
			})
		})	
			
    })
    
    //单选按钮“自己、客人、领导”点击事件
    function show(obj){
  		if($(obj).val() == "LD"){
			$("#ld").show();
  		}else{
  			$("#ld").hide();
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
    
    /* 购物车信息 */
	function getOrderInfo(){
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
		
		//购物车产品明细
		/* var carDetail = [];
		$("#append").find("li.shoppingcar-middle-bot").each(function(index,item){
			var detailOne = {};
			detailOne.id = $(item).find("input[name='c_pro_code']").val();			//id
			detailOne.food = $(item).find("span.middle-bot-l").html();				//产品名（描述）
			detailOne.num = $(item).find("span.middle-bot-r  span.number").html();	//数量
			detailOne.total = $(item).find("input[name='c_pro_price']").val()*Number(detailOne.num);								//小计
			carDetail.push(detailOne);
		});
		
		orderInfo.carDetail = carDetail;
	  	return orderInfo; */
		
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
					
				detailOne.id = $(item).find("input[name='c_pro_code']").val();			//id
				detailOne.product = $(item).find("span.middle-bot-l").html();				//产品名（描述）
				detailOne.num = $(item).find("span.middle-bot-r  span.number").html();	//数量
				detailOne.price = $(item).find("input[name='c_pro_price']").val();	//数量
				detailOne.total = $(item).find("input[name='c_pro_price']").val()*Number(detailOne.num);								//小计
				carDetail.push(detailOne);
			});
		});
		orderInfo.time=times.sort()[0];
		orderInfo.carDetail = carDetail;
	  	return orderInfo;
    }
    
    //点击“返回”按钮
    $(function(){
		$("#staff").val(${appointUser.userId});
  		$("#zdPerson").text(('${appointUser.realName}'!='')?'${appointUser.realName}':'无');
		$("#goback").click(function(){
			location.href="http://"+window.location.host+"/ordersystem/order/toOrderingPageXOrders.do";
		})
	})
	
	
</script>
<body>
  	<!-- 头部开始 -->
	<%@include file="/orders/header.jsp"%>
    <span style="display: block;margin-left: 90px;margin-top: 20px;margin-bottom: 20px;color: red;font-weight: 500;font-size: 14px;"><b>所有产品订购时间24小时开放，欢迎尽情选购！！！（如有疑问，请联系客服，微信/手机号15618966035）</b></span>
    <!-- 头部结束 -->
    <div class="contain order-object">
		<div class="object-left" style="font-size: 15px;font-weight: 600;">订购对象：</div>
		<div class="object-middle" style="font-size: 15px;margin-left: 2px;">
			<input onclick="show(this);" id="ZJ" checked="checked" type="radio"
				name="object" value="ZJ" style="margin-left: 10px;">自己
			<c:if test="${isKR }">
				<input onclick="show(this);" id="KR" type="radio" name="object"
					value="KR" style="margin-left: 10px;">客人
				<input type="text" id="krName" style="display: none;width: 140px;height: 30px;border-radius: 4px;border: 1px solid #FF8F33;font-size: 15px;"/>
            </c:if>
			<c:if test="${isLD }">
				<input onclick="show(this);" id="LD" type="radio" name="object"
					value="LD" style="margin-left: 10px;">领导
            <select name="ld" id="ld" style="display: none;position:relative;top:-2px;width: 140px;height: 30px;border-radius: 4px;border: 1px solid #FF8F33;font-size: 15px;">
					<c:forEach items="${leadList }" var="led">
						<option value="${led.lead_id }">${led.lead_name }</option>
					</c:forEach>
				</select>
			</c:if>
			<c:if test="${isZD }">
				<input onclick="show(this);" id="ZD" type="radio" name="object"
					value="ZD">指定人订购
            </c:if>
		</div>
	</div>
    
    <!-- 订餐对象开始部分 -->
    <div class="contain order-object">
        <div class="object-middle" style="margin-left: 19px;">
        	<input type="text" style="width: 140px;height: 30px;border-radius: 4px;border: 1px solid #FF8F33;font-size: 15px;" id="proName" placeholder="产品名称"/> 
        	<select id="proCateId" name="proCateId" style="width: 140px;height: 32px;border-radius: 4px;border: 1px solid #FF8F33;font-size: 15px;margin-left: 30px;">
  				<option value="">产品类别</option>
  				<c:forEach items="${proCategoryList }" var="proCategory">
	  				<option value="${proCategory.id }">${proCategory.proCateName }</option>
  				</c:forEach>
  			</select> 
        	<input type="button" style="width: 47px; height: 24px; border-radius: 40% 40% 40% 40%;background: #FF8F33; color: #fff;font-size: 15px;margin-left: 20px;" id="queryProducts" value="搜索"/>
        </div>
        <div class="object-right">
        	<button id="goback" style="line-height: 30px;">返回</button>
        </div>
    </div>
    <!-- 订餐对象部分结束 -->
    
    <!-- 产品开始部分 -->
    <div class="contain time" name="1"></div>
    <div  class="contain meal">
        <ul id="productContent" class="clearfix">
        	<li></li>
            
        </ul>
    </div>
    <!-- 产品结束部分 -->
    
    <!-- 购物车开始部分 -->
    <div class="shoppingcar">
        <div class="shoppingcar-top">
            <span class="shoppingcar-top-l">购物车</span>
            <span class="shoppingcar-top-r">清空</span>
        </div> 
        <div class="shoppingcar-middle">
            <ul id="append">
                <li class="shoppingcar-middle-top">
                    <span class="middle-top-l">产品</span>
                    <span class="middle-top-r">件数</span>
                </li>
            </ul>
        </div>
        <div class="shoppingcar-bottom">
            <div class="shoppingcar-bottom-l">
                <span class="buycar">
                     <img src="<%=basePath%>products/images/buycar.png">
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
    	<div class="choose-title">指定人订购</div>
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
    
    <form name="myForm" id="myForm" action="${root }product/goReSumXProducts.do"
		method="POST">
		<input type="hidden" name="orderInfo" id="orderInfo" value="">
		<input type="hidden" name="vistorName" id="vistorName" >
	</form>
	<div style="height:50px;width:100%"></div>
</body>
</html>
