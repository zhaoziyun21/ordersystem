<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>统计</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<style type="text/css">
    #searchBox {
    	positon:relative;
    }
	.org {
	display:inline-block;
	position:absolute;
	}
	.starTime {
		margin-left:185px;
	}
</style>
<script>
		var dep_id="";
		var grid = null;
		var startDate= "";
		var endDate= "";
		//var a = {"Total":"5","Rows":[{"Iid":"0","money":800.0,"id":"j_1","name":"公司集团"},{"id":"u_1","money":1075.0,"Iid":"d_1","name":"ligang"},{"id":"d_1","Iid":"c_1","money":-300.0,"dept_id":"3","name":"设计研发部"},{"id":"c_1","Iid":"j_1","money":-300.0},{"id":"c_1","Iid":"j_1","money":1100.0,"company_id":"1","name":"上海三盛宏业投资(集团)有限责任公司"}]};       
		//var d = {"Total":"5","Rows":[{"Iid":"0","id":"c_1","money":1100.0,"company_id":"1","name":"上海三盛宏业投资(集团)有限责任公司"},{"Iid":"d_1","id":"u_1","money":1075.0,"user_id":"1","name":"ligang"},{"Iid":"c_1","id":"d_1","money":-300.0,"dept_id":"3","name":"设计研发部"}]};
		function initGrids(){
			var flag = $("#searchBox").find("input[name='chargeObj']:checked").val();
			var startTime = $("#startDate").val();
			if(startTime == "" || startTime == null){
				startTime = startDate;
			}
			var endTime =  $("#endDate").val();
			if(endTime == "" || endTime == null){
				endTime = endDate;
			}
			if(flag!=null){
				if(flag=='1'){
				//部门统计
					grid = $(".table_box").ligerGrid({
						url: '${root}order/orderStatisticsXOrders.do',
		// 				data:d,
						parms:[{name:"flag",value:flag},{name:"startTime",value:startTime},{name:"endTime",value:endTime},{name:"dep_id",value:dep_id}],
						pageSize:10,
						//alternatingRow: false,
						/* tree: {
		                    columnId: 'name',
		                    idField: 'id',
		                    parentIDField: 'Iid'
		                }, */
						columns: [
							/* { display: '序号', width:'25%',render:function(row,index){
								return index+1;
							} }, */
							  { display: '公司', name: 'company_name', id: 'company_name', width: '30%' },
							  { display: '部门', name: 'dept_name', id: 'dept_name', width: '30%' },
// 							  { display: '订餐人', name: 'real_name', id: 'real_name', width: '20%' },
// 							  { display: '为谁订餐', name: 'vistor_name', id: 'vistor_name', width: '20%' },
 							  { display: '餐饮公司', name: 'food_company_name', id: 'food_company_name', width: '20%' },
							  { display: '套餐份数',width:'20%',name:'food_num',id:"food_num",
									totalSummary:
				                    {
				                        type: 'sum',
				                         render:function(food_num,column){
											return "<span>当前页总套餐份数："+food_num.sum+"</span>";
									      },
				                        algin:"center"
				                    } 
							  
							  },
							  { display: '总金额', name: 'money', width: '20%' 
								 
							  },
							]
					});
				}
			else if(flag=='2'){
				//员工统计
					grid = $(".table_box").ligerGrid({
						url: '${root}order/orderStatisticsXOrders.do',
		// 				data:d,
						parms:[{name:"flag",value:flag},{name:"startTime",value:startTime},{name:"endTime",value:endTime},{name:"dep_id",value:dep_id}],
						pageSize:10,
						//alternatingRow: false,
						/* tree: {
		                    columnId: 'name',
		                    idField: 'id',
		                    parentIDField: 'Iid'
		                }, */
						columns: [
							/* { display: '序号', width:'25%',render:function(row,index){
								return index+1;
							} }, */
							  { display: '公司', name: 'company_name', id: 'company_name', width: '20%' },
							  { display: '部门', name: 'dept_name', id: 'dept_name', width: '20%' },
							  { display: '订餐人', name: 'real_name', id: 'real_name', width: '20%' },
							  { display: '为谁订餐', name: 'vistor_name', id: 'vistor_name', width: '20%' },
							  { display: '餐饮公司', name: 'food_company_name', id: 'food_company_name', width: '20%' },
							  { display: '套餐份数',width:'15%',name:'food_num',id:"food_num",
								  totalSummary:
				                    {
				                        type: 'sum',
				                         render:function(food_num,column){
											return "<span>当前页总套餐份数："+food_num.sum+"</span>";
									      },
				                        algin:"center"
				                    } 
							  
							  },
							  { display: '总金额', name: 'money', width: '10%' },
							]
					});
			}
			}
		}
		
		//搜索
		function search(){
			initGrids();
		}
		
		//导出excel
		function exportExcel(){
		var flag = $("#searchBox").find("input[name='chargeObj']:checked").val();
			var startTime1 = $("#startDate").val();
			if(startTime1 == "" || startTime1 == null){
				startTime1 = startDate;
			}
			var endTime1 =  $("#endDate").val();
			if(endTime1 == "" || endTime1 == null){
				endTime1 = endDate;
			}
			$.ajax({
			    url:"${root}/order/orderStatisticsExcelXOrders.do",  //请求的url地址
			    dataType:"json",   //返回格式为json
			    data:{
			    	"startTime":startTime1,
			    	"endTime":endTime1,
			    	"dep_id":dep_id,
			    	"flag":flag
			    	},    
			    type:"POST",   //请求方式
			    success:function(req){
			    	//var list = JSON.stringify(req);
			    	updExcel();
			    },
			    error:function(req){
			    	//alert(req.msg);
			    }
			}) ;
			/* var fileName = "统计";
			var colId = "";
			var colName = "";
			var list = JSON.stringify(grid.rows);
			for(var i = 0;i<grid.getColumns().length;i++){
				if(grid.getColumns()[i].name != 'id'){
				colId+=grid.getColumns()[i].name+",";
				}
				colName += grid.getColumns()[i].display+",";
			}
			colId = colId.substr(0,colId.length-1);
			colName = colName.substr(0,colName.length-1);
			location.href="${root}/export/exportLocalBaseExport.do?fileName="+encodeURI(encodeURI(fileName))+"&colId="+encodeURI(encodeURI(colId))+"&colName="+encodeURI(encodeURI(colName))+"&typeList="+encodeURI(encodeURI(list));
			//location.href="${root}/staffSum/getXStaByIdXStaffSum.do"; */
		}	//----------------------公司树形结构START-------------------------
		
		function updExcel(){
			var fileName = "统计";
			var colId = "";
			var colName = "";
			var typeList = "TJ";//JSON.stringify(req.Rows);
			//alert(list2);
			for(var i = 0;i<grid.getColumns().length;i++){
				colId+=grid.getColumns()[i].name+",";
				colName += grid.getColumns()[i].display+",";
			}
			var startTime = $("#startDate").val();
			var endTime =  $("#endDate").val();
			colId = colId.substr(0,colId.length-1);
			colName = colName.substr(0,colName.length-1);
			if(startTime!=""&&startTime!=undefined){
				colId +=',startTime';
				colName += ',开始时间';
			}
			if(endTime!=""&&endTime!=undefined){
				colId +=',endTime';
				colName += ',结束时间';
			}
			location.href="${root}/export/exportLocalBaseExport.do?fileName="+encodeURI(encodeURI(fileName))+"&colId="+encodeURI(encodeURI(colId))+"&colName="+encodeURI(encodeURI(colName))+"&typeList="+encodeURI(encodeURI(typeList));
		}
		
	 	$(function (){
	 		var companyId = null;
			$.ajax({
			    url:"${root}/order/getCurrentStaffCompanyIdXOrders.do",  //请求的url地址
			    dataType:"json",   //返回格式为json
			    type:"POST",   //请求方式
			    success:function(data){
			    	companyId = data;
			    	
					getCurrentMonthFirst()
					var combo2 = $("#txt2").ligerComboBox({
						width: 180,
						selectBoxWidth: 200,
						selectBoxHeight: 200, 
						valueField : 'id',
						textField: 'name',
						treeLeafOnly:false,
						tree: { url:'${root}/order/getTreeDataXOrders.do', 
							checkbox: false, 
							ajaxType: 'get', 
			                textFieldName: 'name',
							idFieldName: 'id',
			                parentIDFieldName: 'parent_id' 
						},
						onSelected: function (newvalue){
							dep_id = newvalue;
						}
					});
					if(companyId == null){
		            	combo2.selectValue("1");
		            }else{
		            	combo2.selectValue(companyId);
		            }
					
					initGrids();
	            }
	        });
		});
	
	
		//----------------------公司树形结构START-------------------------
		/**
		 * 获取当前月的第一天
		 */
		function getCurrentMonthFirst(){
		 	var date = new Date();
		    var year = date.getFullYear() + "";
		    var month = (date.getMonth() + 1) + "";
		    // 本月第一天日期
		  	startDate = year + "-" + month + "-01 00:00:00";
		    // 本月最后一天日期    
		    var lastDateOfCurrentMonth = new Date(year,month,0); 
		    endDate = year + "-" + month + "-" + lastDateOfCurrentMonth.getDate() + " 23:59:59"; 
			  
			$("#startDate").val(startDate);
			$("#endDate").val(endDate);
		}
	</script>
</head>
<body>
	<form id="searchBox">
	<div style="margin: 5px 8px;">
	查询对象：
		<input type="radio"  name="chargeObj" value="1" checked="checked"/>&nbsp;部门统计
		
		<input type="radio"  name="chargeObj" value="2"/>&nbsp;员工统计
		&nbsp;&nbsp;
	组织结构：
	<div class="org">
		<input type="text" id="txt2"/>
	</div>
	<span class="starTime">&nbsp;&nbsp;时间：</span>
		<input id="startDate" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\')}'})"/> 
		 ~
		<input id="endDate" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startDate\')}'})"/>
		<button type="button"  onclick="search()" style="margin-left: 5px;">搜索</button> 
		<input type="button" value="导出excel" onclick="exportExcel();" style="margin-left: 10px;"/>
		
		</div>
		
		<div id="print_div">
			<div class="table_box"></div>
		</div>
		<!-- <button type="button" id="btn_print" onclick="toPrint()">打印</button> -->
	</form>
</body>
</html>
