<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新增菜谱</title>
    
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
			width:60px;
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
			width:0px;
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
			
		 }
		 
		 .right{
			text-align: right;
		 }
	</style>
	<script>
	
	$(function(){
	
		//图片上传
		 $("#uploadify").uploadify({  
		        'uploader'        : '${root}/export/uploadBaseExport.do',  
		        'swf'             : 'uploadify/uploadify.swf', 
		        'fileSizeLimit'   : '10MB', 
		        'fileTypeExts'    : '*.jpg;*.png;',
		        'multi'           : true,
		        'queueID' 		  : 'queue',
		        'auto'  		  : true,
		        'fileDataName'    : 'file',
		        'queueSizeLimit'  : 1,
		        //'uploadLimit'    :   10,
		        'buttonText'	  :'选择图片',
		        'onUploadSuccess' : function(file, data, response) {
		        	$("#imgurl").val("");
		        	 $("#fileQueue").html("");
		            var imgUrl = '${root}'+data.substring(0,data.length-LEN);
		            $("#fileQueue").append("<img src='"+imgUrl+"' width='120' height='120'/>");
		            $("#imgurl").val(data.substring(0,data.length-LEN));
		            
		        },
		        'onUploadError' : function(file, errorCode, errorMsg, errorString) {
		            alert('The file ' + file.name + ' could not be uploaded: ' + errorString);
		           } 
		    });  
	});
		function checkFoodName(){
		var b =false;
			$.ajax({
					url:'${root}order/checkFoodNameXFood.do',
					data: {"foodName":$("#foodName").val(),"type":"add"},  
					type:'POST',
					dataType:'html',
					async:false,
					success:function(data){
						if(data == "Y"){
						b=true;
						}
					}
				});
				return b;
		}
	
		//保存
		function save(){
			if(!checkFoodName()){
				alert('套餐名必须唯一');
			 	return ;
			}
			if(!$("#myForm").valid()){
				return;
			}
			var data = $("#myForm").serializeArray();
			/* var d={};
            $.each(t, function() {
             d[this.name] = this.value;
               }); */
               
			$.ajax({
				url:'${root}order/saveFoodXFood.do?type=add',
				type:'POST',
				dataType:'html',
				data:d,
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
			parent.dialog.close();
		}
		
	</script>
  </head>
  
  <body>
  <div class="DIV">
  <form id="myForm" action="">
  	<table>
  		<tr>
  			<td class="right">
  				<span class="warn">*</span><span>套餐名：</span>
  			</td>
  			<td>
  				<input type="text" id="foodName" name="xfood.foodName" value="" class="required"  maxlength="50"/>
  			</td>
  		</tr>
  		<tr>
  			<td class="right"><span>描述：</span></td>
  			<td><input type="text" name="xfood.foodDesc" value=""   maxlength="255"/></td>
  		</tr>
  		<tr>
  			<td class="right"><span class="warn">*</span><span>单价：</span></td>
  			<td><input type="text" name="xfood.sellPrice" required class="digits"  value=""/></td>
  		</tr>
  		
  		<tr>
  			<td class="right"><span>类别：</span></td>
  			<td>
  				<select name="xfood.foodType">
  					<option value="素餐">素餐</option>
  					<option value="荤餐">荤餐</option>
  					<option value="饮料">饮料</option>
  				</select>
  			
			</td>
  		</tr>
  		<tr>
  			<td>图片：</td>
  			<td>
  				 	<input type="file" name="uploadify" id="uploadify" />
		            <span id="result" style="font-size: 13px;color: red"></span>  
		            <div id="queue"></div>
			        <div id="fileQueue" style="width: 120px;height: 120px; border: 1px solid #A3C0E8;">
			        </div>
			      <input type="hidden" id="imgurl" value="" name="xfood.foodImg"/>
  			</td>
  		</tr>
  	</table>
  	<input class="Btn" type="button" value="保存" onclick="save();"/>
  	<input class="Btn" type="button" value="关闭" onclick="clos();"/>
  </form>
	</div>  
  </body>
</html>
