<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>公告详情</title>
    
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
		.Div ul li select{
			width:136px;
			height:20px;
		}
		.Div ul li textarea{
			width:136px;
			height:20px;
		}
		/* .l-tab-content-item{
			height:88%;
		} */
		
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
			
			var data = $("#myForm").serialize();
			$.ajax({
				url:'${root}order/saveNoticeXNotice.do?type=update',
				type:'POST',
				dataType:'html',
				data:data,
				async:false,
				success:function(data){
					if(data == "Y"){
						parent.$.ligerDialog.success("保存成功！");
					}else{
						parent.$.ligerDialog.error("保存失败！");
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
  		<div title="公告详情" lselected="true" tabid="tab1" style="overflow:auto; ">
  			<div class="DIV">
  				<ul>
					<li>
						<b style="float: left;margin-top: -25px;">公告名称：<input type="text" readonly="readonly" value="${noticeName }"></b>
						
					</li>
					<li style="padding-top: 5px;"><b style="display: block;margin-bottom: 14px;">公告描述：</b><textarea readonly="readonly" rows="3" cols="50" style="margin: 0px;width: 290px;height: 144px;">${noticeDesc }</textarea></li>
				</ul>
  				
  				 <input type="button" class="Btn" value="关闭" onclick="clos();"/>
  			</div>
  		</div>
  		
  		<div title="公告编辑" showclose="true" tabid="tab2" style="overflow:auto; ">
  			<div class="Div">
				<form id="myForm">
				   <input type="hidden" name="id" value="${id }"/>
				   <ul>
				   		<li>
				   			<b style="float: left;margin-top: -25px;"><span style="margin-right: 5px;color: red; width:0px;">*</span>公告名称：<input type="text" name="noticeName" class="required" readonly="readonly" value="${noticeName }"></b>
						</li>
						
						<li style="padding-top: 5px;"><b style="display: block;margin-bottom: 14px;"><span style="margin-right: 5px;color: red; width:0px;">*</span>公告描述：</b><textarea class="required" name="noticeDesc" rows="3" cols="50" style="margin: 0px;width: 290px;height: 144px;">${noticeDesc }</textarea></li>
				   </ul>
				   
				   <input type="button" class="Btn" onclick="save();" value="保存"/>
				   <input type="button" class="Btn" value="关闭" onclick="clos();"/>
				</form>
  			</div>
  		</div>
  	</div>
  	
  </body>
</html>
