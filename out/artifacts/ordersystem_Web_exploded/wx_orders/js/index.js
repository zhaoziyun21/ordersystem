$(function(){
//	type:0、自己 1、客餐 2、领导
	var type;
	/*选择订餐类型？正常订餐：预约订餐*/
	$(".header>span").click(function(){
		$("#mask").css("display","block");
		$(".header ul").slideDown("normal");
		$(".triangle").css("display","block");
	})
	$(".header li").click(function(){
		$(".header li span").removeClass();
		$(".header li div").removeClass();
		$(this).find("span").addClass("choose");
		$(this).find("div").addClass("choosed");
		if($(this).find("span").html()=='正常订餐'){
			$("#zcOrder").show();
			$("#ydOrder").hide();
		}else if($(this).find("span").html()=='预约订餐'){
			$("#zcOrder").hide();
			$("#ydOrder").show();
		}
		$(".header>span").html($(this).find("span").html())
		$("#mask").css("display","none");
		$(".header ul").css("display","none");
		$(".triangle").css("display","none");
	});
	/*给自己点餐*/
	$("#selfOrder").click(function(){
		 var orderType = "ZC";
		 if(String($("#orderType").text())=="正常订餐"){
			 orderType = "ZC";
		 }else if($("#orderType").text()=="预约订餐"){
			 orderType = "YY";
		 }else if($("#orderType").text()=="现场订餐"){
			 orderType = "XC";
		 }
		 type='0';
		 location.href="http://"+window.location.host+"/ordersystem/wechat/toSelfOrderPageWeChat.do?type="+type+"&orderType="+orderType;
	})
	/*给客人订餐*/
	$("#guestOrder").click(function(){
		$("#mask").slideDown("fast");
		$(".guest").slideDown("fast");
	})
	$(".guest-bottom").click(function(){
		 var orderType = "";
		 if($("#orderType").text()=="正常订餐"){
			 orderType = "ZC";
		 }else if($("#orderType").text()=="预约订餐"){
			 orderType = "YY";
		 }else if($("#orderType").text()=="现场订餐"){
			 orderType = "XC";
		 }
		type='1';
		var vistorName = $("#vistorName").val();
		if(vistorName !=null && vistorName !=""){
			location.href="http://"+window.location.host+"/ordersystem/wechat/toSelfOrderPageWeChat.do?type="+type+"&vistorName="+encodeURI(encodeURI(vistorName))+"&orderType="+orderType;
		}else{
			$.MsgBox.Alert("提示", "客人姓名不能为空！");
		}
//		$("#mask").slideUp("fast");
//		$(".guest").slideUp("fast");
	})
	$("#mask").click(function(){
		$("#mask").slideUp("fast");
		$(".guest").slideUp("fast");
		$(".leader").slideUp("fast");
		$(".orderOth").slideUp("fast");
		$(".header ul").css("display","none");
		$(".triangle").css("display","none");
		$(".header ul").css("display","none");
	})
	/*给领导订餐*/
	$("#leaderOrder").click(function(){
		$("#mask").slideDown("fast");
		$(".leader").slideDown("fast");
	})
	$(".leader-bottom").click(function(){
		 var orderType = "";
		 if($("#orderType").text()=="正常订餐"){
			 orderType = "ZC";
		 }else if($("#orderType").text()=="预约订餐"){
			 orderType = "YY";
		 }else if($("#orderType").text()=="现场订餐"){
			 orderType = "XC";
		 }
		type='2';
		leadId = $('#c option:selected').val();
		location.href="http://"+window.location.host+"/ordersystem/wechat/toSelfOrderPageWeChat.do?type="+type+"&leadId="+leadId+"&orderType="+orderType;
//		$("#mask").slideUp("fast");
//		$(".leader").slideUp("fast");
	})
	
	/*领导指定人点餐*/
	$(".orderOthers").click(function(){
		$("#mask").slideDown("fast");
		$(".orderOth").slideDown("fast");
		
	})
	$(".confirm").click(function(){
		 var staffname = $("#staff").find("option:selected").text();
		 var staffId = $("#staff").find("option:selected").val();
		 if(staffId!='0'){
			 $.ajax({
				    url:"http://"+window.location.host+"/ordersystem/wechat/saveXAppointRelationWeChat.do",    //请求的url地址
				    dataType:"html",   //返回格式为html
				    type:"POST",   //请求方式
				    data:{
				    	"appointId":staffId,
				    	"appointName":staffname
				    	},    
				    success:function(req){
					    if(req=='Y'){
						    $.MsgBox.Alert("消息","指定成功",function(){
						    	$(".orderOthers").text("您的餐由"+staffname+"负责！(点击修改)");
						    	$("#mask").slideUp("fast");
						    	$(".orderOth").slideUp("fast");
						    });
					    }
//	 			        location.href="${root}/order/toMyOrderXOrders.do";
				    },
				    error:function(req){
				    	alert(req);
				    }
				});
		 }else{
			 $.MsgBox.Alert("提示", "请选择被指定人！");
		 }
	      		
		
//		$(".orderOthers").slideUp("fast");
//		$(".ordersuccess").slideDown("fast");
//		$(".leader").slideDown("fast");
	})
	/*重置指定人*/
	$(".reset").click(function(){
		$("#mask").slideUp("fast");
		$(".orderOth").slideUp("fast");
	})
	
	
//	级联查询被指定人
	$("select[name='dept']").change(function(){
		var deptId = $(this).find("option:selected").val();
		$.ajax({
            type: 'post',
            url: "http://"+window.location.host+"/ordersystem/wechat/getStaffInfoWeChat.do",
            data: {'deptId':deptId},
            cache: false,
            dataType: "html",
            success: function(data) {
            	var d = eval('('+data+')');
            	if(d.success=='true'){
            		var html = "<option value='0'>请选择</option>";
            		$("#staff").html("");
            		var opt = d.data;
            		for ( var i = 0; i < opt.length; i++) {
						html +="<option value='"+opt[i].id+"'>"+opt[i].name+"</option>";
					}
            		$("#staff").html(html);
            	}else{
            		
            	}
            	
            },
            error: function(data) {
               
            }
        });
	});
});
