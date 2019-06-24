
<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="common/public/TagLib.jsp"%>
<%@ include file="common/public/baseStyle.jsp"%>
<html>
  <head>
  	<meta http-equiv="content-type" content="text/html;charset=utf-8"/>
  	<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
  	<meta name="referrer" content="always"/>
    <base href="<%=basePath%>">
    
    <title>登录</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	 <style>

		 .error{
		 	color: red;
		 	font-size: 10px;	
		 }
		 html{
			 widht:100%;
			 height:100%;
		 }
        body{
			widht:100%;
			height:100%;
			background-image: url("images/AdminBGZ.png");
            margin:0px;
            padding:0px;
        }
        body img{
        	border-style:none;
        	position:absolute;
        	z-index:-1;
        	width:100%;
        	height:100%;
        	border:0px;
        	margin:0px;
        	padding:0px;
        }
		.AdminBg{
			position: fixed;
			width:100%;height:100%;
			background: url("images/AdminBG.png") no-repeat 50% 50%;
			background-size: 90%;
		}
		.AdminLogo{
			width:240px;height:60px;
			position: absolute;
			left:20px;
			top:10px;
			background: url("images/1966400940") no-repeat;
			background-size: contain;
		}
        .bg{
            /*background-image: url("image/bg.png");*/
            /*background-position: center;*/
            /*background-repeat: no-repeat;*/
            width:400px;
            height:300px;
            position: fixed;
            top:50%;
            left:50%;
			background: #ffffff;
            margin-left:-200px;
            margin-top:-190px;
			border-radius: 5px;
			border: 1px solid #ffa000;
			box-shadow: 0 2px 10px rgba(200,200,200,0.5);
        }
        .DIV_input{
        	margin-top:40px;
        	/*padding-left:252px;*/

        }
		.DIV_input>h1{
			padding:0;
			font-size: 18px;
			text-align: center;
			font-weight: 400;
			margin:20px 0;
			font-family: "微软雅黑";
		}
        .bg_input{
			margin: auto;
            height:44px;
            width:280px;
            padding:0px 10px;
            background: #f0f0f0;
            border-radius: 5px;
        }
        .bg_input input {
            padding: 0;
            background:none;
            border: 0;
            outline: none;
            height:44px;
            line-height: 44px;
            width:280px;
            font-size: 14px;

        }
        .bg_input02{
            margin-top: 20px;
        }
        .bg_input03{
            margin-top: 15px;
            background: #ffa000;
            cursor:pointer;
        }
        .bg_input03 input{
            color:#ffffff;
        }
        .radio1 {
            margin-left: 100px;
    		margin-top: 15px;
        }
        .radio2 {
        	margin-left: 105px;
        }
    </style>
	<script type="text/javascript">
		function KeyDown()
		{
		  if (event.keyCode == 13)
		  {
		    event.returnValue=false;
		    event.cancel = true;
		    $("#sub").click();
		  }
		}
	
	
		$().ready(function(){
			$("#sub").click(function(){
				if(!$("#loginForm").valid()){
					return;
				}
				var data = $("#loginForm").serialize();
				$.ajax({
					url:'${root}/xuser/loginXUser.do',
					type:'POST',
					dataType:'html',
					data:data,
					async:false,
					success:function(data){
						if(data == "Y"){
// 						window.location.href = "${root}index.jsp";
							$.ligerDialog.waitting('登录中,请稍候...');
					        setTimeout(function () {
								$.ligerDialog.closeWaitting();
								window.location.href = "${root}xuser/goIndexXUser.do";
							}, 500);
					           
						}else {
							$.ligerDialog.error("用户名或密码错误！");
						}
					}
				
				});
			});
		});
	</script>
  </head>
  
  <body>
  <form action="" id="loginForm">
  <div class="AdminBg">
	  <div class="AdminLogo"></div>
	  <div class="bg">
		 <div class="DIV_input">
			 <h1>管理员登录</h1>
			<div class="bg_input">
				<!--         <input value="输入用户名"> -->
				<input id="userName" type="text" name="xuser.userName" required minlength="3"    placeholder="输入用户名" onkeydown="KeyDown();"/>
			</div>
			<div class="bg_input bg_input02">
				<!--         <input value="输入密码"> -->
				<input id="password" type="password" name="xuser.password" required placeholder="输入密码" onkeydown="KeyDown();"/>
			</div>
<!-- 			<input  name="xuser.type" type="radio" value="1" checked="checked" class="radio1">后台</input> -->
<!-- 			<input  name="xuser.type" type="radio" value="2" class="radio2">餐饮</input> -->
			<div class="bg_input bg_input03">
				<!--         <input value="登录" type="button"> -->
				<input type="button"  id="sub" value="登录"/>
			</div>
		</div>
	  </div>
 </div>
  
  
  
   	  
   </form>
  </body>
</html>
