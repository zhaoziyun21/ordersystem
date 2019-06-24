<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>到付统计</title>
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

		var dep_id=""; //公司 部门ID (dep_)
		var grid = null;
		function initGrids(){
			var startTime = $("#startDate").val();
			var endTime =  $("#endDate").val();
			var cyId = $("#car_select").val();
			grid = $(".table_box").ligerGrid({
			url:'${root}onlinePay/orderStatisticsOnlinePay.do',
			parms:[{name:"startTime",value:startTime},
			       {name:"endTime",value:endTime},
			       {name:"dep_id",value:dep_id},
			       {name:"cyId",value:cyId}
			       ],
			pageSize:10,
			columns: [
				  { display: '公司', name: 'company_name', id: 'company_name', width: '30%' },
				  { display: '部门', name: 'dept_name', id: 'dept_name', width: '25%' },
				  { display: '订餐人', name: 'real_name', id: 'real_name', width: '15%' },
				  { display: '餐饮公司名称', name: 'food_company_name', id: 'food_company_name', width: '20%' },
				  { display: '总金额', name: 'numMoney', width: '10%' ,
					  totalSummary:
	                    {
	                        type: 'sum',
	                         render:function(numMoney,column){
								return "<span>当前页总金额："+numMoney.sum+"</span>";
						      },
	                        algin:"center"
	                    }
				  },
				]
			});
		}
		
		//搜索
		function search(){
		initGrids();
		}
		
		
 $(function ()
        {
			initGrids();
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
                onSelected: function (newvalue)
                {
                     dep_id = newvalue;
                 }
           });
	 	  window.f_test = function ()
          {
              combo2.selectValue("节点1.1");
          };
           
        });
	</script>
</head>
<body>
	<form id="searchBox">
	组织结构：
	<div class="org">
	 <input type="text" id="txt2"/>
	</div>
	<span class="starTime">时间：</span>
		<input id="startDate" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\')}'})"/> 
		 ~
		<input id="endDate" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startDate\')}'})"/>
		<select class="selectDIv" id="car_select">
			<option selected="selected" value="">请选择</option>
		     　　		<c:forEach items="${listUser}" var="user">
					<option value="${user.id}">${user.foodCompanyName}</option>
				</c:forEach>       
		 	</select>
		<button type="button"  onclick="search()">搜索</button> 
		<div id="print_div">
			<div class="table_box"></div>
		</div>
	</form>
</body>
</html>
