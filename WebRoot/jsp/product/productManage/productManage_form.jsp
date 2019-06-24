<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新增产品</title>
    
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
			width:75px;
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
			width:5px;
			position: absolute;
    		top: 10px;
    		float: left;
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
			margin-top: -23px;
		 }
		 
		 .right{
			text-align: right;
			position: relative;
			
		 }
	</style>
	<script>
	$(function(){
			
		 //图片上传
		 $("#uploadify").uploadify({ 
		        'uploader'        : '${root}/export/uploadProductBaseUpload.do;jsessionid="+"<%=request.getSession().getId()%>',  
		        'swf'             : 'uploadify/uploadify.swf', 
		        'fileTypeExts'    : '*.jpg;*.png;',
		        'multi'           : true,
		        'auto'  		  : true,
		        'fileDataName'    : 'Filedata',
		        'buttonText'	  :'选择图片',
		        'method'          :'post',
		        'onUploadSuccess' : function(file, data, response) {
		        	$("#imgurl").val("");
		        	$("#fileQueue").html("");
		            var imgUrl = '${root}'+data;
		            $("#fileQueue").append("<img src='"+imgUrl+"' width='120' height='120'/>");
		            $("#imgurl").val(data);
		        },
		        'onUploadError' : function(file, errorCode, errorMsg, errorString) {
		            alert('The file ' + file.name + ' could not be uploaded: ' + errorString);
		        } 
		    });  
		}); 
		
		//保存
		function save(){
			var data = $("#myForm").serialize();
			if($("#proCateId").val()!="" && $("#proName").val()!="" && $("input[name='proPrice']").val()!="" && $("input[name='proRemain']").val()!=""){
				$.ajax({
					url:'${root}productManage/addProductXProductManage.do?type=add',
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
	  <form id="myForm" action="">
	  	<table>
	  		<tr>
	  			<td class="right">
	  				<span class="warn">*</span><span>产品类别：</span>
	  			</td>
	  			<td>
	  				<select id="proCateId" name="proCateId" class="required" style="margin: 5px;padding: 5px;border: 1px solid #A3C0E8;font-family: 'Helvetica','Arial','微软雅黑';font-size: 14px;">
		  				<option value="">请输入产品类别</option>
		  				<c:forEach items="${proCategoryList }" var="proCategory">
			  				<option value="${proCategory.id }">${proCategory.proCateName }</option>
		  				</c:forEach>
		  			</select>
	  			</td>
	  		</tr>
	  		<tr>
	  			<td class="right">
	  				<span class="warn">*</span><span>产品名称：</span>
	  			</td>
	  			<td>
	  				<input type="text" id="proName" name="proName" required value="" class="required"  maxlength="50"/>
	  			</td>
	  		</tr>
	  		<tr>
	  			<td class="right"><span>描述：</span></td>
	  			<td><input type="text" name="proDescribe" value=""   maxlength="255"/></td>
	  		</tr>
	  		<tr>
	  			<td class="right"><span class="warn">*</span><span>单价：</span></td>
	  			<td><input type="text" name="proPrice" required class="digits"  value=""/></td>
	  		</tr>
	  		<tr>
	  			<td class="right"><span>参考价描述：</span></td>
	  			<td><input type="text" name="proReferencePrice" maxlength="15" class="digits" /></td>
	  		</tr>
	  		
	  		<tr>
	  			<td class="right"><span class="warn">*</span><span>产品数量：</span></td>
	  			<td><input type="text" name="proRemain" required class="digits"  value=""/></td>
	  		</tr>
	  		<tr>
	  			<td class="right"><span>外链地址：</span></td>
	  			<td><input type="text" name="proOutUrl"   value=""/></td>
	  		</tr>
	  		<!-- <tr>
	            <td  class="right"><span>状态：</span></td>
	            <td>
					<p style="float:left;"><input type="radio"  name="status" value="0" checked="checked" style="width:10px" />上架</p>
	                <p style="float:left"> <input type="radio"  name="status" value="1" style="width:10px" />下架 </p>
	            </td>
	  		</tr> -->
	  		<tr>
	  			<td>图片：</td>
	  			<td>
	 				<input type="file" name="Filedata" id="uploadify" />
			        <div id="fileQueue" style="width: 120px;height: 120px; border: 1px solid #A3C0E8;"></div>
			      	<input type="hidden" id="imgurl" value="" name="proImageUrl"/>
	  			</td>
	  		</tr>
	  	</table>
	  	<input class="Btn" type="button" value="保存" onclick="save();"/>
	  	<input class="Btn" type="button" value="关闭" onclick="clos();"/>
	  </form>
  </div>  
  </body>
</html>
