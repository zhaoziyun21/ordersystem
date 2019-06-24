<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>产品详情</title>
    
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
				url:'${root}productManage/getExamineResultInsertOrUpdateXProductManage.do',//参数type标识
				type:'POST',
				dataType:'html',
				data:{"id":$("#examineId").val(), "exStatus":$("input[name='exStatus']:checked").val(), "exDesc":$("#examineDescId").val(), "proId":$("#proId").val()},
				async:false,
				success:function(data){
					if(data == "Y"){
						parent.$.ligerDialog.success("编辑成功！");
					}else{
						parent.$.ligerDialog.success("编辑失败！");
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
  		<div title="审批详情" lselected="true" tabid="tab1" style="overflow:auto; ">
  			<div class="DIV">
  				<ul>
					<li><b style="float: left;">审批状态：</b>
						<input type="radio" name="exStatus1" value="0" <c:if test="${exStatus=='0'}">checked="checked"</c:if>/> <span style="float: left;padding-right:10px">审批中</span>	
						<input type="radio" name="exStatus1"  value="1" <c:if test="${exStatus=='1'}">checked="checked"</c:if> /><span style="float: left;padding-right:10px">通过</span>
						<input type="radio" name="exStatus1" value="2" <c:if test="${exStatus=='2'}">checked="checked"</c:if> /><span style="float: left;padding-right:10px">未通过</span>
						<div style="clear: both;"></div>
					</li>
					
					
					<li><b>原因描述：</b><textarea name="exDesc" rows="3" maxlength="100" cols="50" style="margin: 0px;width: 290px;height: 144px;">${exDesc }</textarea></li>
				</ul>
  				
  				 <input type="button" class="Btn" value="关闭" onclick="clos();"/>
  			</div>
  		</div>
  		
  		<div title="审批编辑" showclose="true" tabid="tab2" style="overflow:auto; ">
  			<div class="Div">
				<form id="myForm">
				   <ul>
						<li>
						<input type="hidden" name="id" id="examineId" value="${id }"/>
						<input type="hidden" name="proId" id="proId" value="${proId }"/>
						
						<b style="float: left;">审批状态：</b>
						<input type="radio" name="exStatus" value="0" <c:if test="${exStatus=='0'}">checked="checked"</c:if> /> <span style="float: left;padding-right:10px">审批中</span>	
						<input type="radio" name="exStatus" value="1" <c:if test="${exStatus=='1'}">checked="checked"</c:if> /><span style="float: left;padding-right:10px">通过</span>
						<input type="radio" name="exStatus" value="2" <c:if test="${exStatus=='2'}">checked="checked"</c:if> /><span style="float: left;padding-right:10px">未通过</span>
						<div style="clear: both;"></div>
						
						</li>
						<li><b>原因描述：</b><textarea name="exDesc" id="examineDescId" rows="3" maxlength="100" cols="50" style="margin: 0px;width: 290px;height: 144px;">${exDesc }</textarea></li>
						
				   </ul>
				   
				   <input type="button" class="Btn" onclick="save();" value="保存"/>
				   <input type="button" class="Btn" value="关闭" onclick="clos();"/>
				</form>
  			</div>
  		</div>
  	</div>
  	
  </body>
</html>
