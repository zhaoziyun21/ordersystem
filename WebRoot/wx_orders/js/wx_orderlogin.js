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
	/*输入账号点击登录*/
    $("#denglubtn").click(function(e) {
        e.preventDefault();
        var msg = $("#denglu").serializeObject();
//        msg.type="phone";
        if(!$("#userName").val()||!$("#password").val()){
        	alert("请输入账号或密码");
        }else{$.ajax({
                type: 'post',
                url: "http://"+window.location.host+"/ordersystem/wechat/orderLoginWeChat.do",
                data: msg,
                cache: false,
                dataType: "html",
                success: function(data) {
                	var username = $("#userName").val(); 
                	var url = "http://"+window.location.host+"/ordersystem/wechat/toIndexWeChat.do";
                   if(data=='Y'){
                	   window.location.href = url;
                	   }else{
                		   alert("用户名密码错误");
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