<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新增角色</title>
    
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
		.table_box{
			width:260px;
			height:auto;
		}
		.DIV select{
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
	</style>
	<script>
		Array.prototype.unique = function(){
			var res = [];
			var json = {};
			for(var i = 0; i < this.length; i++){
			 if(!json[this[i]]){
			  res.push(this[i]);
			  json[this[i]] = 1;
			 }
			}
			return res;
		}
		var tree1 = null;
		function initTree(){
			tree1 = $(".table_box").ligerTree({  
				url:'${root}/xmenu/getAllMenu2XMenu.do',
	            idFieldName :'id',
	            slide : false,
	            isExpand:2,
	            parentIDFieldName :'parentId', 
				textFieldName:'menuName'
	            });
		}
	
		
		$(document).ready(function(){
			  var h = $(window).height(), h2;
		      $(".auditstaff_content").css("height", h);
		      $(window).resize(function() {
		          h2 = $(this).height();
		          $(".auditstaff_content").css("height", h2);
		      });
			initTree();
		});
		
		
		//保存
		function save(){
			if(!$("#myForm").valid()){
				return;
			}
			
			var allMper = ""; 
			var parentId = new Array();
			for(var i = 0 ;i < tree1.getChecked().length; i++){
				allMper += tree1.getChecked()[i].data.id + ",";
				if(tree1.getChecked()[i].data.parentId!=0){
					parentId.push(tree1.getChecked()[i].data.parentId);
				}
			} 
			allMper += parentId.unique().join(",");
			allMper = allMper.split(',').unique().join(',');
			//赋值
			$("#mper").val(allMper);
			
			//表单数据
			var data = $("#myForm").serialize();
			$.ajax({
				url:'${root}xrole/doAddXRole.do?type=add',//参数type标识
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
		
		
		//关闭
		function clos(){
			parent.$.ligerDialog.close(); 
			//移除遮罩层
			parent.$(".l-dialog,.l-window-mask").remove();
		}
	</script>
  </head>
  
  <body>
  	
  <div class="DIV">
  <form id="myForm">
  	<span>角色名称：</span><input type="text" name="xrole.roleName" required maxlength="30" value=""/><span class="warn">*</span><br/>
  	<span>角色描述：</span><textarea name="xrole.roleDesc" rows="5" maxlength="100" cols="50"></textarea><br/>
  	<input type="hidden" id="mper" name="mper" value=""/>
  
  	<h4>权限设置</h4> 
    <div class="table_box" >
   </div>
   <input  type="button" class="Btn" onclick="save();" value="保存"/>
   <input type="button"  class="Btn" value="关闭" onclick="clos();"/>

</form>
</div>
  </body>
</html>
