$(function(){
	/*加減套餐數量*/
		$(".jia").unbind('click').click(function(e){
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
				$("#append .shoppingcar-middle-top").remove();
			};
		}
		$(this).siblings("span[class='number']").html(n);
	})
	 /*点击用户名，弹出我的余额*/
	/*$(".login").append('<span class="login-r">∨</span>')
    $(".login").click(function(){
    	$(".accuntBalance").slideToggle("fast");
    });*/
	/*确认订餐*/
	$("#confirmorder").click(function(){
		if($(".shoppingcar-middle-bot")==0){
			alert("大兄弟，您没点餐");
			return false;
		}else{
			var a = getInfo();
			//alert(JSON.stringify(a))
		/*	var obj={};
			$("#append li span[class='middle-bot-l']").each(function(i,e){
				obj[$(this).html()]=$(this).siblings().find("span[class='number']").html();
			});
			var o={};
			o.username=$(".username span").html();
			obj=$.extend(obj,o);
			console.log(obj)
			$.ajax({
                type: 'post',
                url: "http://"+window.location.host+"/ordersystem/xuser/orderLoginXUser.do",
                data: obj,
                cache: false,
                dataType: "html",
                success: function(data) {
                	
                },
                error: function(data) {
                   
                }
            });*/
		}
	})
});



function getInfo(){
	//套餐信息
	var newOrderInfo = {};
	var orderInfo = $("#orderInfo").html();
	if(orderInfo != null && orderInfo != ""){
		
		var json = eval('('+orderInfo+')');
		//订餐对象(ZJ:自己、KR：客人、LD：领导、ZD：指定)
		newOrderInfo.orderObj = json.orderObj;
		if(json.orderObj == "LD"){
			//领导ID
			newOrderInfo.ldId = json.ldId;
		}else if(json.orderObj == "ZD"){
			//被指定人ID
			newOrderInfo.zdrId = json.zdrId;
		}
		newOrderInfo.obj = json.obj;
		var carDetail = [];
		$("#append").find("li.shoppingcar-middle-bot").each(function(index,item){
			var detailOne = {};
			detailOne.id = $(item).find("input[name='m_food_id']").val();
			detailOne.food = $(item).find("span.middle-bot-l").html();				//套餐名（描述）
			detailOne.num = $(item).find("span.middle-bot-r  span.number").html();	//数量
			detailOne.total = 25*Number(detailOne.num);								//小计
			detailOne.allTotal = 25 * Number($("#totalNum").html()); 				//总钱
			carDetail.push(detailOne);
		});
		newOrderInfo.carDetail = carDetail;
		
		
	}
	
	//确认订餐
//	console.log("newOrderInfo--------"+JSON.stringify(newOrderInfo));
	return newOrderInfo;
}
/* 返回购物车修改 */
function returnCar(){
	//获取购物车信息
	var carInfo = JSON.stringify(getInfo());
	$("#infos").val(carInfo);
	$("#returnCar").submit();
}
