<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>充值</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="style.css">
	-->
	<style>
		label.error{
			color: red;
			font-size: 10px;
		}
		.DIV{
			width:380px;
			margin:auto;
			padding-top:10px;
		}
		h4{
			line-height:30px;
		}
		.DIV select{
			width:190px;
		}
		.DIV span{
			display:inline-block;
			width:80px;
			height:24px;
			text-align:right;
			margin-right:10px;
		}
		.DIV input{
			width:190px;
		}

		.DIV span.warn{
			display:inline-block;
			color: red;
			width:0px;
		}
		span{
		 	font-size: 14px;
		 }
		 table{
		 	margin:0;padding:0;
		 	margin-bottom:40px;
		 }
		 table td.td_left{
			width:260px;
			font-size:14px;
		    font-family:'Helvetica','Arial','微软雅黑';		 
			text-align: right;
		 }
		 table input,table textarea{
		 	margin:5px;
		 	padding:5px;
		 	width:482px;
		 	border: 1px solid #A3C0E8;
		 	font-family:'Helvetica','Arial','微软雅黑';
		 	font-size:14px;
		 }
		 
		 .Btn{
		 	width:100px;
			height:30px;
			display:block;
			float:right;
			text-align: centent;
			background-color: #ffa000;
			/* border:1px solid #A3C0E8;  */
			border-radius: 8%;
			font-size: 12px;
			
		 }
		 
		 .right{
			text-align: right;
		 }
	</style>
	<script>
		//保存
		function save(){
			if(!$("#myForm").valid()){
				return;
			}
			$.ajax({
				url:'${root}xuser/editRoomNumXUser.do',
				type:'POST',
				data:{id:${param.id},roomNum:$("#roomNum").val()},
				dataType:'html',
				async:false,
				success:function(data){
					if(data == "Y"){
						parent.$.ligerDialog.success("保存成功！");
					}else{
						parent.$.ligerDialog.error("保存失败！");
					}
					$("#query",parent.document).click();  
					parent.dialog.close();
				}
			});
		
		}
		
		//关闭
		function clos(){
			parent.dialog.close();
		}
		
	</script>
  </head>
  
  <body>
  <div class="DIV">
  <form id="myForm" action="">
  	<table>
  		<tr>
  			<td class="right">
  				<span class="warn">*</span><span>房间号：</span>
  			</td>
  			<td>
  				<input type="text" id="roomNum" name="roomNum" value="" class="required" />
  			</td>
  		</tr>
  	</table>
  	<input class="Btn" type="button" value="保存" onclick="save();"/>
  	<input class="Btn" type="button" value="关闭" onclick="clos();"/>
  </form>
	</div>  
  </body>
</html>
