<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>产品订单后台</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script>
		var dialog;
		var grid = null;
		
		function initGrids(){
			grid = $(".table_box").ligerGrid({
				url: '${root}order/getAllXOrders.do',
				parms:[{name:"flag",value:1},{name:"deptId",value:$("#dept").val()},{name:"companyId",value:$("#company").val()},{name:"str",value:$("#str").val()}],
				pageSize:10,
				columns: [
					{ display: '订单时间',width:'12%',name: 'ins_time'},
					{ display: '订单人所在部门',width:'10%', name: 'dept_name' },
					{ display: '订单人',width:'8%', name: 'real_name' },
					{ display: '为谁订单',width:'8%', name: 'vistor_name' },
					{ display: '收货人',width:'8%', name: 'rec_name' },
					{ display: '收货地址',width:'20%', name: 'rec_address' },
					{ display: '收货人电话',width:'10%', name: 'rec_phone' },
					{ display: '收货状态',width:'8%', name: 'rec_flag',
						render:function(rowData){
							if(rowData.rec_flag=='0' && rowData.send_out_flag=='0') {
								return '手动确认收货';
							}else if(rowData.rec_flag=='2' && rowData.send_out_flag=='0'){
								return '系统默认收货';
							}else{
								return '未收货';
							}
						}
					},
					{ display: '发货状态',width:'8%', name: 'send_out_flag' ,
						render:function(rowData){
							if(rowData.send_out_flag=='0') {
								return '发货';
							}else{
								return '未发货';
							}
						}
					},
					{ display: '发货时间',width:'12%', name: 'send_out_time' },
					{ display: '快递单号',width:'15%', name: 'express_num' },
					{ display: '操作', name: 'opt',width:'15%',render: function(row){
						var html = "";
						if(row.id == null){
							return null;
						}
						html += "<div onclick=\"select('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>查看</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
						
						html += "<div onclick=\"updateStatus('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>发货操作</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
						/* if(row.send_out_flag=='0') {
							
						}else{
							html += "<div onclick=\"updateStatus('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>发货</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
							//html += "<div onclick=\"updateStatus('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>发货</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-delete\"></div></div></div>";
						} */
						return html;
					}}
				]
					
			});
			
		}
		
		function select(id) {
			dialog = $.ligerDialog.open({ 
	                                height: 500,
	                                url: '${root}productManage/getOrderDetailXProductManage.do?id='+id,
	                                width: 900,
	                                name:'view',
	                                title:'订单详情',
	                                isResize:true	
	                              });  
		}
		
		$(document).ready(function(){
			  var h = $(window).height(), h2;
		      $(".auditstaff_content").css("height", h);
		      $(window).resize(function() {
		          h2 = $(this).height();
		          $(".auditstaff_content").css("height", h2);
		      });
		      loadTree();
			  initGrids();
			
		});
		
		function loadTree(){
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
                 onSelected: function (newvalue){
                 	if(newvalue.indexOf("dept") != -1){
                 		$("#dept").val(newvalue.split("_")[1]);
                 		$("#company").val("");
                 	}else{
                 		$("#company").val(newvalue);
                 		$("#dept").val("");
                 	}
				}
            });
            
		}
		
		//修改发货状态
		function updateStatus(id){
			dialog = $.ligerDialog.open({ 
	                     height: 400,
	                     url: '${root}order/getProFaHuoInfoXOrders.do?id='+id,
	                     width: 550,
	                     name:'view',
	                     title:'发货详情/编辑',
	                     isResize:true	
                     }); 
		
		}
		
		function search(){
			initGrids();
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
		组织机构：
		<div class="org">
		  <input type="text" id="txt2"/>
		</div>
		
		<input type="hidden" id="dept"/>
		<input type="hidden" id="company"/>
		
		姓名：
		<input type="text" id="str" name="str" value=""/>
		<input type="button" id="query" value="搜索"  onclick="search();"/>
		<div class="table_box_p"><div class="table_box"></div></div>
		
	</form>
</body>
</html>
