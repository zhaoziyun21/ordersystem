<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>导入员工金额excel</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="style.css">
	-->
	 
	<script>
	       $().ready(function(){
    	  $("#uploadFile").live('change',function(){ 
    		 	if(this.value==""){
    		 		$("#btnUpd").attr({"disabled":"disabled"});
	   		 		 $("#btnUpd").addClass("button_gray");
			 		 $("#btnUpd").removeClass("button_green");
    		 	}else{
    		 		 $("#btnUpd").removeAttr("disabled");
    		 		 $("#btnUpd").removeClass("button_gray");
    		 		 $("#btnUpd").addClass("button_green");
    		 	}
    	});
      });
	function jdtBegin(){
      	// 在提交的时候就进行查询后台解析 条数
		if($("#uploadFile").val()!=''){
		 	if($("#uploadFile").val().indexOf(".xls") == -1){
		 		alert("请选择正确的文件！");
		 		return false;
		 	}
			setTimeout(timingFun,100);
			
		 }else{
		 	alert("必须选择文件才能上传");
		 	return  false;
		 }
      	 $("#impForm").submit();
      }
      
        function timingFun(){
        // 进度条总宽度
      	var jdtDiv = $("#jdtDiv").css("width");
      	// 进度条已经到的宽度
      	var jdtShow = $("#jdtShow").css("width");
      	//var divWidth = jdtDiv.replace("px","");
      	//var showWidth = jdtShow.replace("px","");
     	//$.post("${root}/etl/getjdtEtl.do",function(data){
     			// 后台传过来的解析文件总条数
//      		var jdtCount = data.jdtCount;
				// 当前解析的行数
//      		var jdtCurrent = data.jdtCurrent;
//      		alert(jdtCount + "---------" + jdtCurrent);
			// 这里由于文件解析太快 我自己定义了一个总条数和 当前解析条数
     		if(dangqian == zongtiao){
     			// 进度条长度更新
     			$("#jdtShow").css("width",zongtiao);
     			$("#jdtH").html("上传完成&nbsp;&nbsp;&nbsp;" + parseInt((dangqian/zongtiao)*100) + "%");
     			$("#jdtMsg").html("${uploadMsp.success}");
     		}else{
     		    
     			$("#jdtShow").css("width",dangqian);
     			// 每100毫秒进度条进行增长
     			dangqian = dangqian+10;
     			$("#jdtH").html("已上传&nbsp;&nbsp;&nbsp;" + parseInt((dangqian/zongtiao)*100) + "%");
	     		// 间隔100毫秒进行到后台查下解析了多少数据
	     		setTimeout(timingFun,100);
     		}
     	//},"json");
      }
		function uploadFile(){
			$.ajax({
				type:"POST",
					dataType:"json",
		 	 	data:$("#documentFrom").serialize(),
				url:"${root}etl/saveEtlTemplate.do",
				success: function(data) {
					if(data.falg =='0'){
						$.ligerDialog.warn(data.status);
					}else{
						$.ligerDialog.success(data.status);
						$("#finishid").show();
						$("#submitid").val('已提交');
						$("#submitid").css("background-color","#d0d0d0");
						$("#submitid").attr({"disabled":"disabled"});		
					}
					//location.reload();
				},
				 error : function(XMLHttpRequest, textStatus, errorThrown) {
					$.ligerDialog.success('读取超时，请检查网络连接');  
            }
			}) ;
   	}
	</script>
  </head>
  
  <body>
  <form id="impForm" action="${root }staffSum/uploadXStaffSum.do" method="post" enctype="multipart/form-data">
  <div class="DIV">
  	集团员工excel充值：<br/>
  	选择文件：<input  type="file" name="uploadFile" id="uploadFile" >
  	 <input id="btnUpd" type="button" class="button_gray" disabled="disabled" onclick="jdtBegin();" value="上传"/> 
	</div>
	</form>  
  </body>
</html>
