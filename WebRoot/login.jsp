
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
		$().ready(function(){
			$.ajax({
				url:'${root}/xuser/loginXUser.do',
				type:'POST',
				dataType:'html',
				data:{"flag":"sso"},
				success:function(data){
					if(data == "Y"){
						$.ligerDialog.waitting('登录中,请稍候...');
				        setTimeout(function (){
				        	$.ligerDialog.closeWaitting();
				            window.location.href = "${root}xuser/goIndexXUser.do";
				        }, 500);
				           
					}else if(data == "N"){
						$.ligerDialog.waitting("对不起，您没有此权限！");
						setTimeout(function (){
				        	window.location.href = "${root}/loginSSO.jsp";
				        }, 1000);
					}else{
						window.location.href = "${root}/loginSSO.jsp";
					}
				}
			
			});
		
		});
	</script>
  </head>
  
  <body>
  
  </body>
</html>
