<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>编辑产品类别</title>
    
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
			width:350px;
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
			width:82px;
			height:24px;
			text-align:right;
			margin-right:10px;
		}
		.DIV input{
			width:190px;
		}
		.DIV input.Btn{
			margin-top:14px;
			width:100px;
		}
		.DIV span.warn{
			display:inline-block;
			color: red;
			width:0px;
		}
		
		.Btninput{
			width:100px;
			margin:20px  auto;
			margin-bottom: 50px;
		}
		
	</style>
	<script>
	
		//关闭窗口
		function clos(){
			parent.$.ligerDialog.close(); 
			//移除遮罩层
			parent.$(".l-dialog,.l-window-mask").remove();
		}
		
		//保存
		function save(){
			if(!$("#myForm").valid()){
				return;
			}
			var data = $("#myForm").serialize();
			$.ajax({
				url:'${root}product/doAddOrUpdateXProCategory.do?type=update',
				type:'POST',
				dataType:'html',
				data:data,
				async:false,
				success:function(data){
					if(data == "Y"){
						alert('保存成功');
					}else{
						alert('保存失败');
					}
					parent.initGrids();
					clos();
				}
			});
		}
		
	</script>
  </head>
  
  <body style="overflow:hidden; ">
  		<div title="编辑产品类别" tabid="tab2" style="overflow:auto; ">
  			<div>
  				<form id="myForm">
  					<input type="hidden" name="id" value="${id }">
				  	<div class="DIV" style="margin-top: 15px;">
				  		<span><span class="warn">*</span>产品类别名称：</span><input type="text" name="proCateName" value="${proCateName }" class="required" maxlength="15"/><br>
				  		<span>描述：</span><textarea name="proCateDesc" style="margin-left: 92px;width: 200px;height: 100px;margin-top: -22px;">${proCateDesc }</textarea><br>
				  	</div>
				  	
					<div class="Btninput">
						<input class="Btn" type="button" value="保存" onclick="save();"/>
				  		<input class="Btn" style="margin-left:40px" type="button" value="关闭" onclick="clos();"/>
					</div>
  				</form>
  			</div>
  		</div>
  
  </body>
</html>
