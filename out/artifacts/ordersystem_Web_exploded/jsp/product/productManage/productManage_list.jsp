<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>产品列表</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!-- <script type="text/javascript" src="../../../js/WdatePicker.js"></script> -->
<script>
		var dialog;
		var grid = null;
		
		function initGrids(){
		    var startTime = $("#startDate").val();
			var endTime =  $("#endDate").val();
			flag = $("#searchBox").find("input[name='status']:checked").val();
			grid = $(".table_box").ligerGrid({
				url: '${root}productManage/getAllXProductManage.do',			
				pageSize:10,
				parms:[{name:'startTime',value:startTime},{name:'endTime',value:endTime},{name:'flag',value:flag}],
				toolbar:{items: [	{ text: '添加产品', click: add , icon:'add'},
	              					{ line:true }
	              				]
	              		},
				columns: [
					/* { display: '序号', width:'10%',name:"__index",render:function(row,index){
						return index+1;
					} }, */
					{ display: '产品类别',width:'15%', name: 'pro_cate_name' },
					{ display: '产品名称',width:'15%', name: 'pro_name' },
					{ display: '产品描述',width:'25%', name: 'pro_describe' },
					{ display: '产品价格',width:'10%', name: 'pro_price'},
					{ display: '参考价格描述',width:'15%', name: 'pro_reference_price'},
					{ display: '产品库存量',width:'6%', name: 'pro_remain' },
					{ display: '产品状态',width:'8%', name: 'pro_status',
						render:function(rowData){
							if(rowData.pro_status=='0') {
								return '已上架';
							}else if(rowData.pro_status=='1'){
								return '已下架';
							}else if(rowData.pro_status=='2'){
								return '上架中';
							}
						}
					},
					{ display: '产品更新时间',width:'12%', 
						render:function(result){
							if(result.upd_time != null) {
								return result.upd_time;
							}else{
								return result.ins_time;
							}
						}
						
					},
					{ display: '产品外链地址',width:'15%', name: 'pro_out_url'},
					{ display: '操作', name: 'opt',width:'15%',render: function(row,rowData){
						var html = "";
						if(row.id == null){
							return null;
						}
						html += "<div onclick=\"edit('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>查看</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
						//html += "<div onclick=\"del('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>删除</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-delete\"></div></div></div>";
						if(row.pro_status=='0') { //上架
							html += "<div onclick=\"updateStatus('"+row.id+"','"+1+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>下架</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-delete\"></div></div></div>";
						}else if(row.pro_status=='1'){ //下架
							html += "<div onclick=\"updateStatus('"+row.id+"','"+2+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>上架中</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-delete\"></div></div></div>";
						}else if(row.pro_status=='2'){ //上架中
							if(row.ex_status=='1'){ //已通过
								html += "<div onclick=\"examineDetail('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>审批结果</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
								html += "<div onclick=\"updateStatus('"+row.id+"','"+0+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>上架</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-delete\"></div></div></div>";
							}else{
								html += "<div onclick=\"examineDetail('"+row.id+"')\" class=\"l-panel-topbarinner l-toolbar l-panel-topbarinner-left\" ><div class=\"l-toolbar-item l-panel-btn l-toolbar-item-hasicon\" toolbarid=\"item-1\"><span>审批结果</span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-icon l-icon-edit\"></div></div></div>";
							}
						}
						return html;
					}}
				]
			});
		}

		$(document).ready(function(){
			initGrids();
		});
		 
		//产品添加
		function add(){			
			dialog = $.ligerDialog.open({ 
                               height: 580,
                               //url: '${root}jsp/product/productManage/productManage_form.jsp',
                               url: '${root}productManage/toAddProCateXProductManage.do',
                               width: 600,
                               name:'view',
                               title:'产品添加',
                               isResize:true	
                             });  
		}
			
		//搜索
		function search(){
			//var startTime = $("#startDate").val();
			//var endTime = $("#endDate").val();
			initGrids();
		}
		
		//明细 和编辑产品列表
		function edit(id) {
		dialog = $.ligerDialog.open({ 
                                height: 560,
                                url: '${root}productManage/getDescXProductManage.do?id='+id,
                                width: 600,
                                name:'view',
                                title:'产品详情/编辑',
                                isResize:true	
                              });  
		}
		
		//删除
		function del(id){
			$.ligerDialog.confirm('确定删除?',function(yes){
				if(yes){
					$.ajax({
						url:'${root}productManage/delByIdXProductManage.do?id='+id,
					    type:'post',
					    dataType:'html',
					    async:false,
					    success:function(data){
					    if(data == "Y"){
								parent.$.ligerDialog.success("删除成功！");
							}else{
								parent.$.ligerDialog.error("删除失败！");
						}
						initGrids(); //初始化table
					    }
					});
				}
			});
		}
		
		//修改商品的上下架状态
		function updateStatus(id,status){
			
			$.ajax({
				url:'${root}productManage/uStatusXProductManage.do?id='+id+'&status='+status,
			    type:'post',
			    dataType:'html',
			    async:false,
			    success:function(data){
				    if(data == "Y"){
						parent.$.ligerDialog.success("操作成功！");
					}else{
						parent.$.ligerDialog.error("操作失败！");
					}
					initGrids();
			    }
			});
		}
		
		function examineDetail(id) {
			dialog = $.ligerDialog.open({ 
		                 height: 530,
		                 url: '${root}productManage/getExamineDetailXProductManage.do?id='+id,
		                 width: 600,
		                 name:'view',
		                 title:'审批详情',
		                 isResize:true	
               	 });  
		}
		
	</script>
</head>
<body>
	<form id="searchBox">
		产品状态:
		<input type="radio"  name="status" value="2"/>上架中
		<input type="radio"  name="status" value="0" checked="checked"/>已上架
		<input type="radio"  name="status" value="1"/>已下架                                               
		<span style="margin-left:4px;">时间：</span>
		<input id="startDate" class="Wdate" type="text" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/> 
		 ~
		<input id="endDate" class="Wdate" type="text" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
		<button type="button"  onclick="search()">搜索</button> 
		
		<!-- <input type="button" value="导出excel" onclick="exportExcel();"/> -->
		</br>
		<span id="saText"></span>
		<!-- <input type="hidden" id ="list" value=${orderList}/> -->
		<div class="table_box">
		</div>
	</form>
</body>
</html>
