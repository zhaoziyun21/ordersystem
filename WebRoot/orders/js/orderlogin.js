$(function(){
	 /* form表单拼接为json */
    $.fn.serializeObject = function() {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
    
    $("body").css({"height":$(window).height(),"width":$(window).width()});
	/*输入账号点击登录*/
    $("#denglubtn").click(function(e) {
        e.preventDefault();//阻止元素发生默认的行为（例如，当点击提交按钮时阻止对表单的提交）
        var msg = $("#denglu").serializeObject();
        if(!$("#userName").val()||!$("#password").val()){
        	$.MsgBox.Alert("消息","请输入账号或密码");
        }else{$.ajax({
                type: 'post',
                url: "http://"+window.location.host+"/ordersystem/xuser/orderLoginXUser.do",
                data: msg,
                cache: false,
                dataType: "html",
                success: function(data) {
                	var username = $("#userName").val(); 
                	var url = "http://"+window.location.host+"/ordersystem/order/toOrderingPageXOrders.do";
                    if(data=='Y'){
                	   window.location.href = url;
                	}else{
                	   $.MsgBox.Alert("消息","用户名密码错误");
                	} 
                },
                error: function(data) {
                   
                }
            });
        }
    })
    
    $("#userName").blur(function(){
    })
});