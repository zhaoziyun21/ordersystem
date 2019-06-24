<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>充值记录</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script>
		var dialog;
		var grid = null;
		var flag;
		
		//条件
		//公司id
		//部门：部门id
		//用户：部门id、模糊查询字段
		var deptId = ""; 
		var str = "";
		var companyId = "";
		
		function initGrids(){
			var startTime = $("#startDate").val();
			var endTime =  $("#endDate").val();
			flag = $("#searchBox").find("input[name='chargeObj']:checked").val();
			deptId = $("#dept").val();
			companyId = $("#company").val();
			if(flag != null){
				//部门
				if(flag == 1){
				str = $("#str").val();
						grid = $(".table_box").ligerGrid({
								url: '${root}order/getRechargeRecordXOrders.do',
								parms:[{name:"Q|startTime|S|LE",value:startTime},{name:"Q|endTime|S|GE",value:endTime},{name:"flag",value:flag},{name:"deptId",value:deptId},{name:"companyId",value:companyId},{name:"str",value:str}],
								pageSize:10,
								columns: [
									{ display: '公司',width:'35%', name: 'company_name' },
									{ display: '部门',width:'35%', name: 'dept_name' },
									{ display: '期间充值',width:'30%', name: 'count' }
									]
							});
				
				}else if(flag == 2){//员工
					str = $("#str").val();
					grid = $(".table_box").ligerGrid({
								url: '${root}order/getRechargeRecordXOrders.do',
								parms:[{name:"Q|startTime|S|LE",value:startTime},{name:"Q|endTime|S|GE",value:endTime},{name:"flag",value:flag},{name:"deptId",value:deptId},{name:"companyId",value:companyId},{name:"str",value:str}],
								pageSize:10,
								columns: [
									{ display: '公司',width:'35%', name: 'company_name' },
									{ display: '部门',width:'35%', name: 'dept_name' },
									{ display: '姓名',width:'15%', name: 'real_name'},
									{ display: '期间充值',width:'15%', name: 'count' }
									]
							});
				
				}
			
			}
		
		}
	
		$(document).ready(function(){
		
		//需要checkbox ，父部门下的所有的部门
		 var combo2 = $("#txt2").ligerComboBox({
                width: 180,
                selectBoxWidth: 200,
                selectBoxHeight: 200, 
                valueField : 'id',
                textField: 'name',
                treeLeafOnly:false,
                tree: { 
//                 data:${deptList},
				url:'${root}/order/getTreeDataXOrders.do',
                checkbox: false, 
                ajaxType: 'get', 
                 textFieldName: 'name',
                idFieldName: 'id',
                 parentIDFieldName: 'parent_id'
                 },
                 onSelected: function (newvalue)
                 {
                 	if(newvalue.indexOf("dept") != -1){
                 		$("#dept").val(newvalue.split("_")[1]);
                 		$("#company").val("");
                 	}else{
                 		$("#company").val(newvalue);
                 		$("#dept").val("");
                 	}
                  }
            });
            initGrids();
			$("#query").click(function(){
// 				deptId = $("#dept").val();
// 				str = $("#str").val();
				initGrids();
			});
		});
		//导出excel
		function exportExcel(){
			var fileName = "充值记录";
			var colId = "";
			var colName = "";
			var startTime = $("#startDate").val();
			var endTime =  $("#endDate").val();
			var list = JSON.stringify(grid.rows);
			/* for(var i = 0;i<grid.getColumns().length;i++){
				for(var i = 0;i<grid.getColumns().length;i++){
					if((grid.getColumns()[i].name != '__index')&&(grid.getColumns()[i].name != 'id')&&(grid.getColumns()[i].name != 'flag') && (grid.getColumns()[i].name != 'opt')){
						colId+=grid.getColumns()[i].name+",";
						colName += grid.getColumns()[i].display+",";
					}
				} 
			} */
			for(var i = 0;i<grid.getColumns().length;i++){
				if((grid.getColumns()[i].name != '__index')&&(grid.getColumns()[i].name != 'id')&&(grid.getColumns()[i].name != 'flag') && (grid.getColumns()[i].name != 'opt')){
					colId+=grid.getColumns()[i].name+",";
					colName += grid.getColumns()[i].display+",";
				}
			} 
			colId = colId.substr(0,colId.length-1);
			colName = colName.substr(0,colName.length-1);
			//location.href="${root}/export/exportLocalBaseExport.do?fileName="+encodeURI(encodeURI(fileName))+"&colId="+encodeURI(encodeURI(colId))+"&colName="+encodeURI(encodeURI(colName))+"&typeList="+encodeURI(encodeURI(list));
			//location.href="${root}/staffSum/getXStaByIdXStaffSum.do";
			$.ajax({
			    url:"${root}/order/getRechargeObjExcelLXOrders.do",    //请求的url地址
			    dataType:"json",   //返回格式为json
			    data:{
			    	"flag":flag,
			    	"deptId":deptId,
			    	"companyId":companyId,
			    	"str":str,
			    	"startTime":startTime,
			    	"endTime":endTime,
			    	},    
			    type:"POST",   //请求方式
			    success:function(req){
			    	var list = JSON.stringify(req);
			    	updExcel(req);
			    },
			    error:function(req){
			    	//alert(req.msg);
			    }
			}) ;
		}
		function updExcel(req){
			var fileName = "统计";
			var colId = "";
			var colName = "";
			//var typeList = "CZ";//JSON.stringify(req.Rows);
			var typeList = "CO";
			//alert(list2);
			 for(var i = 0;i<grid.getColumns().length;i++){
				if((grid.getColumns()[i].name != 'xh')&&(grid.getColumns()[i].name != '__index')&&(grid.getColumns()[i].name != 'id')&&(grid.getColumns()[i].name != 'flag') && (grid.getColumns()[i].name != 'opt')){
					colId+=grid.getColumns()[i].name+",";
					colName += grid.getColumns()[i].display+",";
				}
			} 
			colId = colId.substr(0,colId.length-1);
			colName = colName.substr(0,colName.length-1);
			location.href="${root}/export/exportLocalBaseExport.do?fileName="+encodeURI(encodeURI(fileName))+"&colId="+encodeURI(encodeURI(colId))+"&colName="+encodeURI(encodeURI(colName))+"&typeList="+encodeURI(encodeURI(typeList));
		}
		 
</script>
<style type="text/css">
	.org {
		display:inline-block;
		position:relative;
		top:7px;
	}
	.table_box_p {
		margin-top:4px;
	}
</style>
</head>
<body>
	<form id="searchBox">
		充值对象：
		<input type="radio"  name="chargeObj" value="1" checked="checked"/>部门
		<input type="radio"  name="chargeObj" value="2"/>员工                                                  
		<span>组织机构：</span>
		<div class="org">
		  <input type="text" id="txt2"/>
		</div>
		  <input type="hidden" id="dept"/>
		  <input type="hidden" id="company"/>
		
		
		<span class="starTime">时间：</span>
		<input id="startDate" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endDate\')}'})"/> 
		 ~
		<input id="endDate" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startDate\')}'})"/>
		<input type="text" id="str" name="str" value="" placeholder="姓名/部门"/>
		<input type="button" id="query" value="搜索"/>
		<input type="button" value="导出excel" onclick="exportExcel();"/>
		<div class="table_box_p"><div class="table_box"></div></div>
		</div>
	</form>
</body>
</html>
