<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../common/public/TagLib.jsp"%>
<%@ include file="../../../common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>菜谱公告</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="style.css">
	-->
	 <script type="text/javascript">
        
        $(function ()
        {
        	 var tab = liger.get("navtab");
            $("#navtab").ligerTab({ 
            onBeforeSelectTabItem: function (tabid)
            {
              	tab.reload(tabid) ;
            }, onAfterSelectTabItem: function (tabid)
            {
//                 alert('onAfterSelectTabItem' + tabid);
            } 
            });
        });
    </script>
	
	 
  </head>
  
  <body>
  	<div style="width:100%;">
		<h3 style="display:block;width:66px;height:20px;font-weight: 600;font-size: 16px;margin:10px auto;color:red;">温馨提示</h3>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:1px;color:red;font-weight: 400">1.首次点击“菜谱公告管理”栏目，展示的是“菜谱管理”页面的信息，即当前公司录入的所有菜谱套餐信息！！！</h4>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:1px;color:red;font-weight: 400">2.“菜谱管理”页面，可以进行新增套餐、编辑和删除套餐；“公告管理”页面，可以进行新增公告、编辑和删除公告！！！</h4>
		<h4 style="font-size: 14px;padding:0 116px;padding-bottom:12px;color:red;font-weight: 400">3.“一周菜谱”页面，对一周的菜谱信息进行设置与编辑；“一周公告”页面，对一周的公告信息进行设置与编辑；注意：一周的菜谱和公告设置的信息，会展示到员工订餐页面！！！</h4>
	</div>
	<div id="navtab" style="width: 99%; overflow: hidden; border: 1px solid #D3D3d3;" class="liger-tab">
          <div tabid="tab1" title="菜谱管理">
             <iframe src="${root }jsp/order/xfood/xfood_list.jsp"></iframe>
          </div>
          <div tabid="tab2" title="一周菜谱" >
               <iframe src="${root }order/goFoodbillFormXFood.do"></iframe>
          </div>
          <div tabid="tab3" title="公告管理">
             <iframe src="${root }jsp/order/xfood/notice_list.jsp"></iframe>
          </div>
          <div tabid="tab4" title="一周公告" >
               <iframe src="${root }order/goNoticeBillFormXNotice.do"></iframe>
          </div>
      </div>   
  </body>
</html>
