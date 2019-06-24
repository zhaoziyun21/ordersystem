$(function(){
	
	 /*点击用户名，弹出我的余额*/
	$(".login").append('<span class="login-r">∨</span>')
    $(".login").click(function(){
    	$(".accuntBalance").slideToggle("fast");
    });
})