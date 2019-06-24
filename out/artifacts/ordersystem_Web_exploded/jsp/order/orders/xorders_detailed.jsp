<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>订餐管理明细</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<script type="text/javascript" src="../../../js/jquery.metadata.js"></script>

<%-- <script type="text/javascript" src="<%=basePath%>/js/print/LodopFuncs.js"></script> --%>
<style>
	.table{
    border:1px solid #DDDDDD;
    background-color: #FFFFFF;
} 
	#btn_print {
	position:fixed;
	right:0;
	bottom:0;
	background-color:#169bD5;
	border-radius:3px;
	}
	
</style>
<script>
		//定义全局变量  
		var LODOP;//打印
		 $(document).ready(function(){
			 // add dingzhj at date 2017-03-02 表格 单元格合并
			 _w_table_rowspan("#process",1); 
			 _w_table_rowspan("#process",2); 
			 _w_table_rowspan("#process",3); 
			 _w_table_rowspan("#process",6); 
			 _w_table_rowspan("#process",7); 
		});			
		
		//打印  
		function toPrint() {  
		    //识别各种浏览器的实现原理是根据navigator.userAgent返回值识别,如果为chrome浏览器  
		    if (navigator.userAgent.indexOf('Chrome')>=0){  
		        var headstr = "<html><head></head><body>";    
		        var footstr = "</body>";    
		        //获得 print_div   里的所有 html 数据(把要打印的数据嵌套在 一个 div 里，获得 div)  
		        var printData = document.getElementById("print_div1").innerHTML;  
		        //获取当前页面的html  
		        var oldstr = document.body.innerHTML;   
		        //把 headstr+printData+footstr 里的数据 复制给 body 的 html 数据 ，相当于重置了 整个页面的 内容  
		        document.body.innerHTML = headstr+printData+footstr;   
		        //使用方法为 window.print() 打印页面上局部的数据   
		        window.print();   
		        document.body.innerHTML = oldstr;    
		    }else{  
		         if(!LODOP){  
		            //getLodop的任务是判断浏览器的类型并决定采用哪个对象实例，并检测控件是否安装、是否最新版本、并引导安装或升级   
		            LODOP=getLodop();  
		              
		        }  
		        if(LODOP){  
		            var headstr = "<html><head></head><body>";    
		            var footstr = "</body>";    
		            var printData = document.getElementById("print_div").innerHTML;  
		            var oldstr = document.body.innerHTML;    
		            var data = headstr+printData+footstr;    
		            LODOP.ADD_PRINT_HTM(20,32,"92%","94%",data);  
		            LODOP.SET_PRINT_STYLEA(0,"HOrient",3);  
		            LODOP.SET_PRINT_STYLEA(0,"VOrient",3);  
		            LODOP.PREVIEW();  
		        }   
		    }
		} 	
	</script>
</head>
<body>
	<div id="print_div1">
	
		<div style="margin-top: 10px;margin-bottom: 10px;text-align: center;">
			<h3>餐饮公司：${foodBusiness.foodCompanyName }</h3>
			<h3 style="margin-top: 5px;">下单时间：${startTime } ~ ${endTime }</h3>
		</div>
		
		<table id="process" cellpadding="2" cellspacing="0"  border="1" class="table" style="margin: auto;"> 
			<thead> 
				<tr height="34px" align="center" style="font-size: 14px;color: #333333"> 
					  <td width="55px">公司</td>
					  <td width="100px">部门</td>
					  <c:if test="${foodBusiness.whetherSendStatus==0 }">
					  	<td width="50px">派餐地址</td>
					  </c:if>
				       <td width="86px">套餐</td>
				      <!--  <td width="171px">描述</td> -->
				       <td width="64px">份数</td>
				       <td width="87px">部门负责人</td>
				       <td width="96px">电话</td>
				       <td width="200px">订餐对象</td>
				</tr> 
			</thead> 
			<tbody> 
				<!-- <tr height="34px" align="center" style="font-size: 12px;color: #666666"> 
					<td>部门A</td> 
					<td>A套餐</td> 
					<td>鱼香肉丝+可乐</td> 
					<td>2</td>
					<td>一页书</td>
					<td>18600443357</td>
					<td>素还真</td>
				</tr> 
				<tr height="34px" align="center" style="font-size: 12px;color: #666666"> 
					<td>部门A</td> 
					<td>A套餐</td> 
					<td>鱼香肉丝+可乐</td> 
					<td>2</td>
					<td>一页书</td>
					<td>18600443357</td>
					<td>素还真</td>
				</tr> 
				<tr height="34px" align="center" style="font-size: 12px;color: #666666"> 
					<td>部门B</td> 
					<td>A套餐</td> 
					<td>鱼香肉丝+可乐</td> h
					<td>2</td>
					<td>叶小钗</td>
					<td>18600443356</td>
					<td>素还真</td>
				</tr>
				<tr height="34px" align="center" style="font-size: 12px;color: #666666"> 
					<td>部门B</td> 
					<td>A套餐</td> 
					<td>鱼香肉丝+可乐</td> 
					<td>2</td>
					<td>叶小钗</td>
					<td>18600443356</td>
					<td>素还真</td>
				</tr> 
					<tr height="34px" align="center" style="font-size: 12px;color: #666666"> 
					<td>部门B</td> 
					<td>A套餐</td> 
					<td>鱼香肉丝+可乐</td> 
					<td>2</td>
					<td>叶小钗</td>
					<td>18600443356</td>
					<td>素还真</td>
				</tr> 
					<tr height="34px" align="center" style="font-size: 12px;color: #666666"> 
					<td>部门B</td> 
					<td>A套餐</td> 
					<td>鱼香肉丝+可乐</td> 
					<td>2</td>
					<td>叶小钗</td>
					<td>18600443356</td>
					<td>素还真</td>
				</tr>  -->
				<c:if test="${!empty detaileList}">
					<c:forEach items="${detaileList}" var="dept">
						<tr height="34px" align="center" style="font-size: 12px;color: #666666"> 
								<td>
									${dept.companyName}
								</td> 
								<td>
									${dept.deptName}
								</td> 
								
								<c:if test="${foodBusiness.whetherSendStatus==0 }">
									<td>
										${dept.roomNum}
									</td>
								</c:if>
								
								<td>${dept.foodName}</td> 
								<td>${dept.foodNum}</td>
								
								<c:if test="${empty dept.deptRoleName}">
									<td></td>
								</c:if>
								<c:if test="${not empty dept.deptRoleName}">
									<td>${dept.deptRoleName}</td>
								</c:if>
								
								<c:if test="${ empty dept.tel}">
									<td></td>
								</c:if>
								<c:if test="${not empty dept.tel}">
									<td>${dept.tel}</td>
								</c:if>
								
								<td>
									<c:if test="${not empty dept.listUser}">
										<c:forEach items="${dept.listUser}" var="listUser1">
											${listUser1.realName}
										</c:forEach>
									</c:if>
								</td> 
						</tr>
					</c:forEach>
				</c:if>
			</tbody> 
		</table> 
	</div>
	<button type="button" id="btn_print" onclick="toPrint()">打印</button>
</body>
<script type="text/javascript">
//函数说明：合并指定表格（表格id为_w_table_id）指定列（列数为_w_table_colnum）的相同文本的相邻单元格 
//参数说明：_w_table_id 为需要进行合并单元格的表格的id。如在HTMl中指定表格 id="data" ，此参数应为 #data 
//参数说明：_w_table_colnum 为需要合并单元格的所在列。为数字，从最左边第一列为1开始算起。 
function _w_table_rowspan(_w_table_id,_w_table_colnum){ 
	_w_table_firsttd = ""; 
	_w_table_currenttd = ""; 
	_w_table_SpanNum = 0; 
	_w_table_Obj = $(_w_table_id + " tr td:nth-child(" + _w_table_colnum + ")"); 
	_w_table_Obj.each(function(i){ 
	if(i==0){ 
	_w_table_firsttd = $(this); 
	_w_table_SpanNum = 1; 
	}else{ 
	_w_table_currenttd = $(this); 
	if(_w_table_firsttd.text()==_w_table_currenttd.text()){ 
	_w_table_SpanNum++; 
	_w_table_currenttd.hide(); //remove(); 
	_w_table_firsttd.attr("rowSpan",_w_table_SpanNum); 
	}else{ 
	_w_table_firsttd = $(this); 
	_w_table_SpanNum = 1; 
	} 
	} 
	}); 
} 

//函数说明：合并指定表格（表格id为_w_table_id）指定行（行数为_w_table_rownum）的相同文本的相邻单元格 
//参数说明：_w_table_id 为需要进行合并单元格的表格id。如在HTMl中指定表格 id="data" ，此参数应为 #data 
//参数说明：_w_table_rownum 为需要合并单元格的所在行。其参数形式请参考jQuery中nth-child的参数。 
// 如果为数字，则从最左边第一行为1开始算起。 
// "even" 表示偶数行 
// "odd" 表示奇数行 
// "3n+1" 表示的行数为1、4、7、10. 
//参数说明：_w_table_maxcolnum 为指定行中单元格对应的最大列数，列数大于这个数值的单元格将不进行比较合并。 
// 此参数可以为空，为空则指定行的所有单元格要进行比较合并。 
function _w_table_colspan(_w_table_id,_w_table_rownum,_w_table_maxcolnum){ 
	if(_w_table_maxcolnum == void 0){_w_table_maxcolnum=0;} 
	_w_table_firsttd = ""; 
	_w_table_currenttd = ""; 
	_w_table_SpanNum = 0; 
	$(_w_table_id + " tr:nth-child(" + _w_table_rownum + ")").each(function(i){ 
	_w_table_Obj = $(this).children(); 
	_w_table_Obj.each(function(i){ 
	if(i==0){ 
	_w_table_firsttd = $(this); 
	_w_table_SpanNum = 1; 
	}else if((_w_table_maxcolnum>0)&&(i>_w_table_maxcolnum)){ 
	return ""; 
	}else{ 
	_w_table_currenttd = $(this); 
	if(_w_table_firsttd.text()==_w_table_currenttd.text()){ 
	_w_table_SpanNum++; 
	_w_table_currenttd.hide(); //remove(); 
	_w_table_firsttd.attr("colSpan",_w_table_SpanNum); 
	}else{ 
	_w_table_firsttd = $(this); 
	_w_table_SpanNum = 1; 
	} 
	} 
	}); 
	}); 
	} 
</script>
</html>
