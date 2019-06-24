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
	</style>
	<script>
	$(function(){
		
			$.validator.addMethod('phone', function( value, element ){
		   	 	return this.optional( element ) ||/^(\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/ .test( value );
		
			}, '请输入正确的电话号码');
		})
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
				url:'${root}xuser/doAddXUser.do?type=update',
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
		
		
		$(function(){
			$("#tab").ligerTab();
		});
	</script>
  </head>
  
  <body  style="overflow:hidden; ">
  		<div id="tab" >
  		<div title="用户详情" tabid="tab1" style="overflow:auto; ">
  			<div class="DIV">
  				<span>用户名：</span>${xuser.loginName }	<br>
			  	<span>密    码：</span>${xuser.password }<br>
			  	<span>用户角色：</span><s:select disabled="true" name="roleId" list="roleList" headerKey="0" headerValue="选择角色" listKey="id" listValue="roleName" ></s:select>
			  	<br>
			  	<h4>用户信息</h4>
			  	<span>姓名：</span>${xuser.name }<br>
			  	<span>邮箱：</span>${xuser.email }<br>
			  	<span>电话：</span>${xuser.tel }<br>
			  <br><input type="button" class="Btn" value="关闭" onclick="clos();"/>
  			</div>
  		</div>
  		
  		
  		<div title="用户编辑" tabid="tab2" style="overflow:auto; ">
  			<div class="DIV" >
  				<form id="myForm">
  							<input type="hidden" name="userRoleId" value="${userRoleId }" />
  						  	<input type="hidden" name="xuser.id" value="${xuser.id }" />
	  				<span>用户名：</span><input type="text" name="xuser.loginName" value="${xuser.loginName }" required minlength="5" maxlength="15"/><span class="warn">*</span><br>
	  				<span>密    码：</span><input type="text" name="xuser.password" value="${xuser.password }" required minlength="6"/><span class="warn">*</span><br>
	  				<span>用户角色：</span><s:select name="roleId" cssClass="required" list="roleList" headerKey="" headerValue="选择角色" listKey="id" listValue="roleName" ></s:select><span class="warn">*</span>
	  				<h4>用户信息</h4>
	  				<span>姓名：	</span><input type="text" name="xuser.name" value="${xuser.name }" required maxlength="15"/><span class="warn">*</span><br>
	  				<span>邮箱：	</span><input type="text" name="xuser.email" value="${xuser.email }" class="emial" maxlength="30"/><span class="warn">*</span><br>
	  				<span>电话：	</span><input type="text" name="xuser.tel" value="${xuser.tel }" class="phone" maxlength="30"/><span class="warn">*</span><br>
	  						<input type="button" class="Btn" value="保存" onclick="save();"/>
	  						<input type="button" class="Btn" value="关闭" onclick="clos();"/>
  				</form>
  			</div>
  		</div>
  	</div>
  
  
  
  </body>
</html>
