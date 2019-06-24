<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>充值</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<style>
	.l-trigger-cancel{
		right:0px;
		z-index:-1111;
	}
</style>
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
			flag = $("#searchBox").find("input[name='chargeObj']:checked").val();
			deptId = $("#dept").val();
			companyId = $("#company").val();
			if(flag != null){
				//部门
				if(flag == 1){
						grid = $(".table_box").ligerGrid({
								url: '${root}order/getRechargeObjXOrders.do',
								parms:[{name:"flag",value:flag},{name:"deptId",value:deptId},{name:"companyId",value:companyId}],
								pageSize:10,
								toolbar:{items: [	{ text: '全部充值', click: chargeAll , icon:'add'},
					              					{ line:true }
					              				]
					              		},
								columns: [
									{ display: 'flag', name: 'flag',hide:true },
									{ display: 'id', name: 'id',hide:true },
									/* { display: '序号', width:'10%',name:"__index",render:function(row,index){
										return index+1;
									} }, */
									{ display: '公司',width:'25%', name: 'company_name' },
									{ display: '部门',width:'15%', name: 'dept_name' },
									{ display: '部门订餐人',width:'20%', name:'specname',render:function(row){
										if(row.specname == null || row.specname == ""){
											return "";
// 											return "暂无";
										}else{
											return row.specname;
										}
									}},
									{ display: '余额',width:'20%', name: 'dept_sum' },
									{ display: '操作',width:'20%', name: 'opt', render: function(row){
										var html = "";
										if(row.id == null){
											return null;
										}
										html += "<div onclick=\"charge('"+row.id+"','"+row.flag+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>充值</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
										return html;
									}}
									]
							});
				
				}else if(flag == 2){//员工
					str = $("#str").val();
					grid = $(".table_box").ligerGrid({
								url: '${root}order/getRechargeObjXOrders.do',
								parms:[{name:"flag",value:flag},{name:"deptId",value:deptId},{name:"companyId",value:companyId},{name:"str",value:str}],
								pageSize:10,
								toolbar:{items: [	{ text: '全部充值', click: chargeAll , icon:'add'},
					              					{ line:true },
					              					{ text: '年底清零', click: clearZero , icon:'edit'}
					              				]
					              		},
								columns: [
									{ display: 'flag', name: 'flag',hide:true },	
									{ display: 'id', name: 'id',hide:true },	
									/* { display: '序号', width:'10%',name:"xh",render:function(row,index){
										return index+1;
									} }, */
									{ display: '公司',width:'25%', name: 'company_name' },
									{ display: '部门',width:'15%', name: 'dept_name' },
									{ display: '姓名',width:'25%', name: 'real_name'},
// 									{ display: '职务',width:'15%', name: 'jobtitle' },
									{ display: '余额',width:'15%', name: 'balance' },
									{ display: '操作',width:'20%', name: 'opt', render: function(row){
										var html = "";
										if(row.id == null){
											return null;
										}
										html += "<div onclick=\"charge('"+row.id+"','"+row.flag+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>充值</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
										return html;
									}}
								]
							});
				
				}
			
			}
		
		}
	
		$(document).ready(function(){
			var companyId = null;
			$.ajax({
			    url:"${root}/order/getCurrentStaffCompanyIdXOrders.do",  //请求的url地址
			    dataType:"json",   //返回格式为json
			    type:"POST",   //请求方式
			    success:function(data){
			    	companyId = data;
			    	
			    	//需要checkbox ，父部门下的所有的部门
				 	var combo2 = $("#txt2").ligerComboBox({
		                width: 180,
		                selectBoxWidth: 200,
		                selectBoxHeight: 200, 
		                valueField : 'id',
		                textField: 'name',
		                treeLeafOnly:false,
		                tree: { 
		//                  data:${deptList},
							url:'${root}/order/getTreeDataXOrders.do',
		                	checkbox: false, 
		                	ajaxType: 'get', 
		                 	textFieldName: 'name',
		                	idFieldName: 'id',
		                 	parentIDFieldName: 'parent_id'
		                 },
		                 onSelected: function (newvalue) {
		                 	if(newvalue.indexOf("dept") != -1){
		                 		$("#dept").val(newvalue.split("_")[1]);
		                 		$("#company").val("");
		                 	}else{
		                 		$("#company").val(newvalue);
		                 		$("#dept").val("");
		                 	}
		                 }
		            });
		            if(companyId == null){
		            	combo2.selectValue("1");
		            }else{
		            	combo2.selectValue(companyId);
		            }
		            
		            initGrids();
					$("#query").click(function(){
		// 				deptId = $("#dept").val();
		// 				str = $("#str").val();
						initGrids();
					});
			    }
			});
		
		});
		
		//全部充值
		function chargeAll(){
			dialog = $.ligerDialog.open({ 
                                height: 250,
                                url: '${root}order/toRechargeAllXOrders.do?flag='+flag+'&deptId='+deptId+'&str='+str+'&companyId='+companyId,
                                width: 500,
                                type:'post',
//                                 parms:{"flag":flag,"deptId":deptId,"str":str},
                                name:'view',
                                title:'充值',
                                isResize:true	
                                 });  
			
		}
		
		//充值
		function charge(id,flag){
				dialog = $.ligerDialog.open({ 
                                height: 250,
                                url: '${root}jsp/order/orders/recharge_form.jsp?id='+id+'&flag='+flag,
                                width: 500,
                                name:'view',
                                title:'充值',
                                isResize:true	
                                 });  
		}
		//导出excel
		function exportExcel(){
			var fileName = "充值";
			var colId = "";
			var colName = "";
			var list = JSON.stringify(grid.rows);
			for(var i = 0;i<grid.getColumns().length;i++){
				for(var i = 0;i<grid.getColumns().length;i++){
					if((grid.getColumns()[i].name != '__index')&&(grid.getColumns()[i].name != 'id')&&(grid.getColumns()[i].name != 'flag') && (grid.getColumns()[i].name != 'opt')){
						colId+=grid.getColumns()[i].name+",";
						colName += grid.getColumns()[i].display+",";
					}
				} 
			}
			colId = colId.substr(0,colId.length-1);
			colName = colName.substr(0,colName.length-1);
			//location.href="${root}/export/exportLocalBaseExport.do?fileName="+encodeURI(encodeURI(fileName))+"&colId="+encodeURI(encodeURI(colId))+"&colName="+encodeURI(encodeURI(colName))+"&typeList="+encodeURI(encodeURI(list));
			//location.href="${root}/staffSum/getXStaByIdXStaffSum.do";
			$.ajax({
			    url:"${root}/order/getRechargeObjExcelXOrders.do",    //请求的url地址
			    dataType:"json",   //返回格式为json
			    data:{
			    	"flag":flag,
			    	"deptId":deptId,
			    	"companyId":companyId,
			    	"str":str,
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
			var typeList = "CZ";//JSON.stringify(req.Rows);
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
			/* $.ajax({
			    url:"${root}/export/exportLocalBaseExport.do",    //请求的url地址
			    dataType:"json",   //返回格式为json
			    data:{
			    	"fileName":fileName,
			    	"colId":colId,
			    	"colName":colName,
			    	"typeList":list2
			    	},    
			    type:"POST",   //请求方式
			    success:function(req){
			    },
			    error:function(req){
			    	//alert(req.msg);
			    }
			});  */ 
			//location.href="${root}/staffSum/getXStaByIdXStaffSum.do";
		}
		
		
		//清零
		function clearZero(){
			 $.ligerDialog.confirm('确认给员工清零吗？', function (yes)
               {	if(yes){
               				
               			$.ajax({
               				url:"${root}order/doClearZeroXOrders.do",
               				data:{"flag":flag,"deptId":deptId,"str":str,"companyId":companyId},
               				dataType:'text',
               				type:'post',
               				success:function(data){
               					if(data == "Y"){
               						$.ligerDialog.success("操作成功！");
               						
               					}else{
               						$.ligerDialog.error("操作失败！");
               					}
               					initGrids();
               				},
               				error:function(){
               				
               				}
               			});
               		}
                        
               });
		}
		

			$(".l-trigger-cancel").css("right","0px")
				

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
	<div style="width:100%;">
		<h3 style="display:block;width:66px;height:20px;font-weight: 600;font-size: 16px;margin:10px auto;color:red;">年底清零</h3>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:2px;color:red;">1.需清零某个公司的所有员工，查询条件选择该公司即可；部门同样；需清零某部门的某个员工，查询条件选择该部门以及输入该员工名字！！！</h4>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:4px;color:red;">2.注意：查询条件填写完后，一定要点击“搜索”按钮</h4>
	</div>
	
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
		<input type="text" id="str" name="str" value="" placeholder="姓名、手机号、用户名"/>
		<input type="button" id="query" value="搜索"/>
		<input type="button" value="导出excel" onclick="exportExcel();"/>
		<div class="table_box_p"><div class="table_box"></div></div>
		</div>
	</form>
</body>
</html>
