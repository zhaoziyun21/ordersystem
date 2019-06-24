<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>发货详情</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<style>
		label.error{
			color: red;
			font-size: 10px;
		}
		.DIV{
			
			width:260px;
			margin:auto;
			padding-top:10px;
		}
		h4{
			line-height:30px;
		}
		.DIV select{
			width:190px;
		}

		.DIV input.Btn{
			margin-top:20px;
			width:100px;
		}
		.DIV ul{
			margin-top: 14px;
		}
		.DIV ul li{
			padding-top: 20px;
			
		}
		.DIV ul li span{
			padding-left: 5px;
		}
		.Div ul li input{
			display: block;
    		width: 14px;
   			height: 20px;
   			padding-top: 13px;
    		float: left
		}
		.Div ul li select{
			width:136px;
			height:20px;
		}
		.Div ul li textarea{
			width:136px;
			height:20px;
		}
		
	</style>
	<script>		
		
		$(document).ready(function(){
		    //初始化详情和编辑框
		    $("#tab").ligerTab(); 
		});
		  
		//保存
		function save(){
			if(!$("#myForm").valid()){
				return;
			}
			
			//表单数据
			$.ajax({
				url:'${root}order/upDateStatusXOrders.do',
			    type:'post',
			    dataType:'html',
			    data:{"id":$("#idId").val(), "sendOutFlag":$("input[name='sendOutFlag']:checked").val(), "expressNum":$("#expressNumId").val()},
			    async:false,
			    success:function(data){
				    if(data == "Y"){
						parent.$.ligerDialog.success("操作成功！");
					}else{
						parent.$.ligerDialog.error("操作失败！");
					}
					parent.initGrids();
					parent.dialog.close();
			    }
			});
			
		}
		
		//关闭
		function clos(){
			parent.$.ligerDialog.close(); 
			//移除遮罩层
			parent.$(".l-dialog,.l-window-mask").remove();
		}
		
	</script>
  </head>
  
  <body style="">	
  	<div id="tab" >
  		<div title="发货详情" lselected="true" tabid="tab1" style="overflow:auto; ">
  			<div class="DIV">
  				<ul>
					<li><b style="float: left;">发货状态：</b>
						<input type="radio" disabled="disabled" name="sendOutFlag1" value="0" <c:if test="${sendOutFlag == 0 }">checked="checked"</c:if>/> <span style="float: left;padding-right:10px">发货</span>	
						<input type="radio" disabled="disabled" name="sendOutFlag1"  value="1" <c:if test="${sendOutFlag == 1 }">checked="checked"</c:if> /><span style="float: left;padding-right:10px">未发货</span>
						<div style="clear: both;"></div>
					</li>
					
					
					<li><b>快递单号：</b><textarea readonly="readonly" name="expressNum" rows="3" maxlength="30" cols="10" style="margin: 0px;width: 220px;height: 60px;">${expressNum }</textarea></li>
				</ul>
  				
  				<input type="button" class="Btn" value="关闭" onclick="clos();"/>
  			</div>
  		</div>
  		
  		<div title="发货编辑" showclose="true" tabid="tab2" style="overflow:auto; ">
  			<div class="Div">
				<form id="myForm">
				   <ul>
						<li>
						<input type="hidden" name="id" id="idId" value="${id }"/>
						
						<b style="float: left;">发货状态：</b>
						<input type="radio" name="sendOutFlag" value="0" <c:if test="${sendOutFlag == 0 }">checked="checked"</c:if> /> <span style="float: left;padding-right:10px">发货</span>	
						<input type="radio" name="sendOutFlag" value="1" <c:if test="${sendOutFlag == 1 }">checked="checked"</c:if> /><span style="float: left;padding-right:10px">未发货</span>
						<div style="clear: both;"></div>
						
						</li>
						<li><b>快递单号：</b><textarea name="expressNum" id="expressNumId" rows="3" maxlength="30" cols="10" style="margin: 0px;width: 220px;height: 60px;">${expressNum }</textarea></li>
						
				   </ul>
				   
				   <input type="button" class="Btn" onclick="save();" value="保存"/>
				   <input type="button" class="Btn" value="关闭" onclick="clos();"/>
				</form>
  			</div>
  		</div>
  	</div>
  	
  </body>
</html>
