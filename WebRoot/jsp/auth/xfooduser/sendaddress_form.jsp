<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新增餐饮派送地址</title>
    
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
		}

	</style>
	<script>
	
		//保存
		function save(){
			if(!$("#myForm").valid()){
				return;
			}
			
			var data = $("#myForm").serialize();
			$.ajax({
				url:'${root}order/doAddOrUpdateXFoodSendAddress.do?type=add',
				type:'POST',
				dataType:'html',
				data:data,
				async:false,
				success:function(data){
					if(data == "Y"){
						$.ligerDialog.success('保存成功');
					}else{
						ligerDialog.error('保存失败');
					}
					parent.initGrids();
					clos();
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
  
  <body style="overflow:hidden; ">
  		<div title="新增派送地址" tabid="tab2" style="overflow:auto; ">
  			<div>
  				<form id="myForm">
				  	<div class="DIV">
				  		<h4>派送范围信息</h4>
				  		<span><span class="warn">*</span>派送范围：</span>
				  			<select name="regionId" class="required">
				  				<option value="">请输入派送范围</option>
				  				<c:forEach items="${regionList }" var="region">
					  				<option value="${region.id }">${region.regionName }</option>
				  				</c:forEach>
				  			</select>
						<br>
				  		
				  		<h4>派送地址信息</h4>
				  		<span>园区：</span><input type="text" name="park" maxlength="20"/><br>
				  		<span><span class="warn">*</span>楼栋：</span><input type="text" name="highBuilding" class="required" maxlength="20"/><br>
				  		<span>单元（座）：</span><input type="text" name="unit" maxlength="20"/><br>
				  		<!-- <span><span class="warn">*</span>楼层：</span><input type="text" name="floor" class="required" maxlength="20"/><br> -->
				  		<span><span class="warn">*</span>房间号：</span><input type="text" name="roomNum" class="required" maxlength="20"/><br>
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
