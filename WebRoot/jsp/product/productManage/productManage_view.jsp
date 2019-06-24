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
			margin-top:14px;
			width:100px;
		}
		.DIV ul{
			margin-top: 14px;
		}
		.DIV ul li{
			padding-top: 8px;
			
		}
		.DIV ul li span{
		padding-left: 5px;
		}
		.Div ul li input{
			width:136px;
			height:20px;
		
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
			var h = $(window).height(), h2;
		    $(".auditstaff_content").css("height", h);
		    $(window).resize(function() {
		    	h2 = $(this).height();
		    	$(".auditstaff_content").css("height", h2);
			});
		      
		 	//图片上传
		 	$("#uploadify").uploadify({  
		        'uploader'        : '${root}/export/uploadProductBaseUpload.do',  
		        'swf'             : 'uploadify/uploadify.swf', 
		        'fileSizeLimit'   : '10MB', 
		        'fileTypeExts'    : '*.jpg;*.png;',
		        'multi'           : true,
		        'queueID' 		  : 'queue',
		        'auto'  		  : true,
		        'fileDataName'    : 'fileName',
		        'queueSizeLimit'  : 1,
		        //'uploadLimit'    :   10,
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
		    
		    //初始化详情 和编辑框
		    $("#tab").ligerTab(); 
		});
		  
		//保存
		function save(){
			if(!$("#myForm").valid()){
				return;
			}
			
			//表单数据
			var data = $("#myForm").serialize();
			$.ajax({
				url:'${root}productManage/addProductXProductManage.do?type=update',//参数type标识
				type:'POST',
				dataType:'html',
				data:data,
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
  		<div title="产品详情" lselected="true" tabid="tab1" style="overflow:auto; ">
  			<div class="DIV">
  				<ul>
  					<li><b>产品类别：</b> <span style="padding-left: 0px;">${xProCategory.proCateName }</span> </li>
  					<li><b>产品名称：</b> <span style="padding-left: 0px;">${proName }</span> </li>
  					<li><b>产品价格：</b><span>${proPrice } </span></li>
  					<li><b>参考价描述：</b><span>${proReferencePrice } </span></li>
  					<li><b>产品库存：</b><span>${proRemain } </span></li>
  					<li><b>产品状态：</b><span style="padding-left:2px;"> <c:if test="${status=='0' }">已上架</c:if><c:if test="${status=='1' }">已下架</c:if><c:if test="${status=='2' }">上架中</c:if></span>
  					
  					</li>
  					<li><b>产品描述：</b><span>${proDescribe }</span></li>
  					<li><b>外链地址：</b><span><a href="${proOutUrl }" target="_blank">${proOutUrl }</a></span></li>
  					<li><h4>产品图片：</h4>
		  				<div id="" style="width: 120px;height: 120px; border: 1px solid #A3C0E8;">
		  				<img src="${root }/${proImageUrl }" width='120' height='120'/>
					    </div>
					</li>
  				</ul>
  				
  				 <input type="button" class="Btn" value="关闭" onclick="clos();"/>
  			</div>
  		</div>
  		
  		<div title="产品编辑" showclose="true" tabid="tab2" style="overflow:auto; ">
  			<div class="Div">
				<form id="myForm">
					<ul>
						<li>
							<b>产品类别：</b>
							<select name="proCateId" class="required" style="margin-left: -3px;">
				  				<option value="">请输入产品类别</option>
				  				<c:forEach items="${proCategoryList }" var="proCategory">
					  				<option value="${proCategory.id }" <c:if test="${proCateId==proCategory.id}">selected="selected"</c:if>>${proCategory.proCateName }</option>
				  				</c:forEach>
				  			</select>
						</li>
						<li><b>产品名称：</b><input type="text" name="proName" required maxlength="30" value="${proName }"/></li>
						<li><b>产品库存：</b> <input style="margin-left: -4px;" type="text" name="proRemain" required maxlength="30" value="${proRemain }"/></li>
						<li><b>产品价格：</b><input type="text" name="proPrice" required maxlength="30" value="${proPrice }"/></li>
						<li><b>参考价描述：</b><input type="text" name="proReferencePrice" maxlength="15" value="${proReferencePrice }"/></li>
						<li><b>产品状态：</b><s:select name="status"  value="status" list="#{'0':'已上架','1':'已下架', '2':'上架中'}"></s:select></li>
						<li><b>产品描述：</b><textarea name="proDescribe" rows="3" maxlength="100" cols="50" >${proDescribe }</textarea></li>
						<li><b>外链地址：</b><textarea name="proOutUrl" rows="3" maxlength="200" cols="50" >${proOutUrl }</textarea></li>
						<%-- <li><b>外链地址：</b><input type="text" name="proOutUrl" maxlength="200" cols="50" value="${proOutUrl }"/></li> --%>
						<li>
							<input type="hidden" name="id" value="${id }"/>
							<input type="hidden" name="proCode" value="${proCode }"/>
							<input type="hidden" name="insTime" value="${insTime }"/>
							<input type="hidden" name="insUser" value="${insUser }"/>
							
						  	<h4>产品图片</h4> 
						   	<td>
		  				 		<input type="file" name="uploadify" id="uploadify" />
				            	<span id="result" style="font-size: 13px;color: red"></span>  
				            	<div id="queue"></div>
					        	<div id="fileQueue" style="width: 120px;height: 120px; border: 1px solid #A3C0E8;">
					        		<img src="${root }/${proImageUrl }" width='120' height='120'/>
					        	</div>
					      		<input type="hidden" id="imgurl" value="${proImageUrl }" name="proImageUrl"/>
		  				   </td>
		  				</li>
						<li></li>
					
					</ul>
					
				   <input type="button" class="Btn" onclick="save();" value="保存"/>
				   <input type="button" class="Btn" value="关闭" onclick="clos();"/>
				</form>
  			</div>
  		</div>
  	</div>
  	
  </body>
</html>
