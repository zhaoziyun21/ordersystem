<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>用户详情</title>
    
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
			width:60px;
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
		.addPeople {
			margin-top:30px;
			margin-left:40px;
			absolute:relative;
		}
		.addPeople li {
			margin:20px 0px;
		}
		.Btn {
			position:absolute;
		}
		.Btnsave {
			right:90px;
			bottom:50px;
		}
		.Btnclose {
			right:50px;
			bottom:50px;
		}
		.button-box{
			width:100px;
			margin:auto;
			margin-bottom:20px;
		}
	
	</style>
	<script>
		//关闭窗口
		function clos(){
			parent.dialog.close();
		}
		
		$(function(){
			
		});
		
		//保存
		function save(){
			var roleIds ="";
			$("input[name='cb']").each(function(index,item){
				if($(item).attr("checked") == "checked"){
					roleIds += $(item).val() + ",";
				}
			});
			
			$.ajax({
				url:'${root}xuser/doAddRoleXUser.do',
				type:'POST',
				dataType:'html',
				data:{"userId":"${u.userId}","ids":roleIds},
				async:false,
				success:function(data){
					if(data == "Y"){
						parent.$.ligerDialog.success("操作成功");
					}else{
						parent.$.ligerDialog.error("操作失败");
					}
					parent.initGrids();
					parent.dialog.close();
				}
			});
		}
		
		
	</script>
  </head>
  
  <body  style="overflow:hidden;overflow-x:hidden;overflow-y:auto; ">
  	<ul class="addPeople">
  		<c:forEach items="${roleList }" var="r">
  			<!-- 不显示餐饮公司角色 -->
<!--   			<c:if test="${r.id != '4' }"> -->
  			<li>
  				<input type="checkbox"
  				<c:forEach items="${rlist }" var="ur">
  					<c:if test="${r.id==ur.roleId }">checked = "checked"</c:if>
  				</c:forEach>
  			 name="cb" value="${r.id }" />${r.roleName }
  			</li> 
<!--   			</c:if> -->
  		</c:forEach>
  		
	  </ul>
	 	 <div class="button-box">
	  		<input type="button"  value="保存" onclick="save();"/>
  			<input type="button" style="margin-left:20px;" value="关闭" onclick="clos();"/>
  		</div>
  </body>
</html>
