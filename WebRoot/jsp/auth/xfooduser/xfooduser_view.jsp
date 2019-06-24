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
			width:70px;
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
			margin:30px auto 15px auto;
		}
		
	</style>
	<script>
		$(function(){
			$.validator.addMethod('phone', function( value, element ){
		   	 	return this.optional( element ) ||/^(\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/ .test( value );
		
			}, '请输入正确的电话号码');
		})
		
		var regionArr = "${xuser.sendRegion}".split(",");
		var tree1 = null;
		function initTree1(){
			tree1 = $(".region_box1").ligerTree({  
				url:'${root}order/getRegionAllXFoodSendRegion.do',
	            idFieldName :'id',
	            slide : false,
	            isExpand:1,
				textFieldName:'regionName',
				onSuccess:function(data){
					for(var i = 0; i<regionArr.length; i++){
						this.selectNode(regionArr[i]);
					}
				}
	        });
		}
		
		var tree2 = null;
		function initTree2(){
			tree2 = $(".region_box2").ligerTree({  
				url:'${root}order/getRegionAllXFoodSendRegion.do',
	            idFieldName :'id',
	            slide : false,
	            isExpand:1,
				textFieldName:'regionName',
				onSuccess:function(data){
					for(var i = 0; i<regionArr.length; i++){
						this.selectNode(regionArr[i]);
					}
				}
	        });
		}
		
		$(document).ready(function(){
			  var h = $(window).height(), h2;
		      $(".auditstaff_content").css("height", h);
		      $(window).resize(function() {
		          h2 = $(this).height();
		          $(".auditstaff_content").css("height", h2);
		      });
			initTree1();
			initTree2();
		});
		
		//保存
		function save(){
			if(!$("#myForm").valid()){
				return;
			}
			
			var checkedRegions = ""; 
			for(var i=0; i<tree2.getChecked().length; i++){
				if(i<tree2.getChecked().length-1){
					checkedRegions += tree2.getChecked()[i].data.id + ",";
				}else{
					checkedRegions += tree2.getChecked()[i].data.id;
				}
			} 
			//赋值
			$("#checkedRegions").val(checkedRegions);
			
			var data = $("#myForm").serialize();
			$.ajax({
				url:'${root}xuser/doAddXUser.do?type=update',
				type:'POST',
				dataType:'html',
				data:data,
				async:false,
				success:function(data){
					if(data == "Y"){
						alert('编辑成功！');
					}else{
						alert('编辑失败！');
					}
					var parmsData =[{name: 'Q|u.type|S|EQ', value:'2'},{name: 'Q|u.del_flag|S|EQ', value:'0'}];
					parent.initGrids(parmsData);
					clos();
				}
			});
		}
		
		//关闭窗口
		function clos(){
			parent.$.ligerDialog.close(); 
			//移除遮罩层
			parent.$(".l-dialog,.l-window-mask").remove();
		}
		
		$(function(){
			$("#tab").ligerTab();
		});
	</script>
  </head>
  
  <body style="overflow:hidden; ">
  		<div id="tab" >
  		<div title="用户详情" tabid="tab1" style="overflow:auto; ">
  			<div class="DIV">
  				<h4>登录用户信息</h4>
  				<span>公司名：</span>${xuser.foodCompanyName }	<br>
  				<span>用户名：</span>${xuser.userName }	<br>
			  	<span>密    码：</span>${xuser.password }<br>
			  	<span>用户角色：</span><s:select disabled="true" name="roleId" list="roleList" headerKey="0" headerValue="选择角色" listKey="id" listValue="roleName" ></s:select>
		  		<br>
			  	<h4>真实用户信息</h4>
			  	<span>姓名：</span>${xuser.realName }<br>
			  	<span>邮箱：</span>${xuser.email }<br>
			  	<span>电话：</span>${xuser.tel }<br>
			  	
			  	<h4>是否支持派送<font style="color: red">(支持就设置派送的范围；不支持就生成饭票，到店取餐)</font></h4>
		  		<span style="float: left;">支持：</span><input style="float: left;margin-top: 3px;margin-left:0px;width: 13px;" type="radio" name="xuser.whetherSendStatus" value="0" <c:if test="${xuser.whetherSendStatus==0 }">checked="checked"</c:if> >
		  		<span style="float: left;">不支持：</span><input style="float: left;margin-top: 3px;margin-left:0px;width: 13px;" type="radio" name="xuser.whetherSendStatus" value="1" <c:if test="${xuser.whetherSendStatus==1 }">checked="checked"</c:if> >
		  		<div style="clear: both;"></div>
  			</div>
  			
  			<h4 style="margin-left: 110px;">派送范围设置</h4>
		  	<div class="region_box1" style="margin-left: 128px;">
	   		</div>
			
			<div class="Btninput">
	  			<input class="Btn" style="margin-left:40px;margin-bottom: 30px" type="button" value="关闭" onclick="clos();"/>
			</div>
  		</div>
  		
  		<div title="用户编辑" tabid="tab2" style="overflow:auto; ">
  			<div>
  				<form id="myForm">
					<input type="hidden" name="userRoleId" value="${userRoleId }" />
				  	<input type="hidden" name="xuser.id" value="${xuser.id }" />
				  	<div class="DIV">
		  				<h4>登录用户信息</h4>
		  				<span><span class="warn">*</span>公司名：</span><input type="text" name="xuser.foodCompanyName" value="${xuser.foodCompanyName }" required minlength="3" maxlength="15"/><br>
						<span><span class="warn">*</span>用户名：</span>${xuser.userName }	<br>
		  				<span><span class="warn">*</span>密    码：</span><input type="text" name="xuser.password" value="${xuser.password }" required minlength="6"/><br>
		  				<span><span class="warn">*</span>用户角色：</span><s:select name="roleId" cssClass="required" list="roleList" headerKey="" headerValue="选择角色" listKey="id" listValue="roleName" ></s:select>
		  				
		  				<h4>真实用户信息</h4>
		  				<span><span class="warn">*</span>姓名：</span><input type="text" id="userName" name="xuser.realName" value="${xuser.realName }" required maxlength="15"/><br>
		  				<span>邮箱：	</span><input type="text" name="xuser.email" value="${xuser.email }" class="emial" maxlength="30"/><br>
		  				<span><span class="warn">*</span>电话：</span><input type="text" name="xuser.tel" value="${xuser.tel }" class="phone" required maxlength="30"/><br>
	  					
	  					<h4>是否支持派送<font style="color: red">(支持就设置派送的范围；不支持就生成饭票，到店取餐)</font></h4>
				  		<span style="float: left;">支持：</span><input style="float: left;margin-top: 3px;margin-left:0px;width: 13px;" type="radio" name="xuser.whetherSendStatus" value="0" <c:if test="${xuser.whetherSendStatus==0 }">checked="checked"</c:if> >
				  		<span style="float: left;">不支持：</span><input style="float: left;margin-top: 3px;margin-left:0px;width: 13px;" type="radio" name="xuser.whetherSendStatus" value="1" <c:if test="${xuser.whetherSendStatus==1 }">checked="checked"</c:if> >
				  		<div style="clear: both;"></div>
	  				</div>
	  				
	  				<input type="hidden" id="checkedRegions" name="xuser.sendRegion"/>
				  	<h4 style="margin-left: 110px;">派送范围设置</h4>
				  	<div class="region_box2" style="margin-left: 128px;">
			   		</div>
	  				
	  				<div class="Btninput">
						<input class="Btn" type="button" value="保存" onclick="save();"/>
				  		<input class="Btn" style="margin-left:40px;margin-bottom: 30px" type="button" value="关闭" onclick="clos();"/>
					</div>
					
  				</form>
  			</div>
  		</div>
  	</div>
  
  </body>
</html>
