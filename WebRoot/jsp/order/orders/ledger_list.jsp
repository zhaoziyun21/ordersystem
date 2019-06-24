<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>台账列表</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link rel="stylesheet" type="text/css" href="${root}css/loading.css">
<script type="text/javascript" src="${root}js/loading.js"></script>
<script>
		$(function(){
			var currentDate = new Date($.ajax({async: false}).getResponseHeader("Date")); //获取服务器系统的当前时间
			//var currentDate = new Date(); //获取本地电脑当前时间
			var currentYear = currentDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var currentMonth = currentDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var currentDay = currentDate.getDate(); //获取当前日(1-31)
			
			$("#startDate").val(currentYear+"-"+currentMonth+"-"+currentDay+" 00:00:00");
			$("#endDate").val(currentYear+"-"+currentMonth+"-"+currentDay+" 13:30:00");
		})

		var dialog;
		var grid = null;
		function initGrids(){
			var startTime;
			var endTime;
			if($("#startDate").val() != "" && $("#endDate").val() != ""){
				startTime = $("#startDate").val();
				endTime = $("#endDate").val();
			}else{
				var currentDate = new Date($.ajax({async: false}).getResponseHeader("Date")); //获取服务器系统的当前时间
				//var currentDate = new Date(); //获取本地电脑当前时间
				var currentYear = currentDate.getFullYear(); //获取完整的年份(4位,1970-????)
				var currentMonth = currentDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				var currentDay = currentDate.getDate(); //获取当前日(1-31)
				startTime=currentYear+"-"+currentMonth+"-"+currentDay+" 00:00:00";
				endTime=currentYear+"-"+currentMonth+"-"+currentDay+" 12:00:00";
			}
			
			grid = $(".table_box").ligerGrid({
				url: '${root}order/getLedgerListXOrders.do',
				//parms:[{name:"Q|xod.ins_time|D|GT",value:"2017-02-23 00:00:00"},{name:"Q|xod.ins_time|D|LT",value:"2017-02-23 23:59:59"}],
				parms:[{name:"startTime",value:startTime},{name:"endTime",value:endTime}],
				pageSize:10,
				toolbar:{items: [	{ text: '明细', click: detailed , icon:'add'},
	              					{ line:true }
	              				]
	              		},
				columns: [
					{ display: '序号', width:'10%',render:function(row,index){
						return index+1;
					} },
					{ display: '套餐',width:'30%', name: 'food_name' },
					{ display: '单价',width:'20%', name: 'sell_price'},
					{ display: '份数',width:'20%', name: 'food_num', 
						totalSummary:
	                    {
	                        type: 'sum',
	                         render:function(food_num,column){
								return "<span>当前页总份数："+food_num.sum+"</span>";
						      },
	                        algin:"center"
	                    } 
					},
					{ display: '总价',width:'20%', name: 'total',
						totalSummary:
	                    {
	                        type: 'sum',
	                         render:function(total,column){
								return "<span>当前页总金额："+total.sum+"</span>";
						      },
	                        algin:"center"
	                    } }
					]
			});
		}
	
		$(document).ready(function(){
			initGrids();
		});
		
		//添加
		function add(){
				dialog = $.ligerDialog.open({ 
                                height: 400,
                                url: '${root}jsp/order/xfood/xfood_form.jsp',
                                width: 600,
                                name:'view',
                                title:'新增菜谱',
                                isResize:true	
                                 });  
		}
		
		function loading() {
			$('body').loading({
				loadingWidth:240,
				title:'请耐心等待...',
				name:'test',
				discription:'',
				direction:'column',
				type:'origin',
				// originBg:'#71EA71',
				originDivWidth:40,
				originDivHeight:40,
				originWidth:6,
				originHeight:6,
				smallLoading:false,
				loadingMaskBg:'rgba(0,0,0,0.2)'
			});
		}
		
		// add dingzhj  at date 20170-03-04 台帐明细
		function detailed(){
			var startTime1;
			var endTime2;
			if($("#startDate").val() != "" && $("#endDate").val() != ""){
				startTime1 = $("#startDate").val();
				endTime2 = $("#endDate").val();
			}else{
				var currentDate = new Date($.ajax({async: false}).getResponseHeader("Date")); //获取服务器系统的当前时间
				//var currentDate = new Date(); //获取本地电脑当前时间
				var currentYear = currentDate.getFullYear(); //获取完整的年份(4位,1970-????)
				var currentMonth = currentDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				var currentDay = currentDate.getDate(); //获取当前日(1-31)
				startTime1=currentYear+"-"+currentMonth+"-"+currentDay+" 00:00:00";
				endTime2=currentYear+"-"+currentMonth+"-"+currentDay+" 12:00:00";
			}
			
			//定时器遮罩层
			loading();
			setTimeout(function(){
				removeLoading('test');
			},3000);
			
			dialog = $.ligerDialog.open({ 
                            height: 500,
                            url: '${root}order/toLedgerDetailedXOrders.do?&startTime='+startTime1+"&endTime="+endTime2,
                            width: 1000,
                            name:'view',
                            title:'明细',
                            isResize:true	
                          });  
			}
		
		//搜索
		function search(){
			initGrids();
		}
		
	</script>
</head>
<body>
	<div style="width:100%;">
		<h3 style="display:block;width:66px;height:20px;font-weight: 600;font-size: 16px;margin:10px auto;color:red;">温馨提示</h3>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:2px;color:red;">1.首次点击“台账”栏目，搜索条件默认系统当天的00:00:00-12:00:00这个时间段，页面展示以及“明细”是该时段的信息！！！</h4>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:4px;color:red;">2.该“台账”包含了“现场订餐管理”和“订餐管理”的订单信息！！！</h4>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:14px;color:red;">3.如要查询其它时间段的信息，请自行输入，但是最好一天一天的查询，否则数据量大，容易导致系统崩溃！！！</h4>
	</div>
	<form id="searchBox">
		<span style="margin-left:5px;">时间：</span>
		<input id="startDate" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\')}'})"/> 
		 ~
		<input id="endDate" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startDate\')}'})"/>
		<button type="button"  onclick="search()">搜索</button> 
		<div class="table_box" style="margin-top: 5px;">
		</div>
	</form>
</body>
</html>
