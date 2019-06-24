<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="common/public/TagLib.jsp"%>
<%@ include file="common/public/baseStyle.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>后台管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	  <script type="text/javascript">
        $(function ()
        {

            var combo1 = $("#txt1").ligerComboBox({
                width : 180, 
                selectBoxWidth: 200,
                selectBoxHeight: 300, 
                 initText : '请选择',
                 treeLeafOnly: true,
                 isMultiSelect:false,
               valueField : 'id',
               textField: 'name',
                tree: { url:'${root}/order/getTreeDataXOrders.do', 
                textFieldName: 'name',
                ajaxType: 'get',
                idFieldName: 'id',
              }
            }); 


            var combo2 = $("#txt2").ligerComboBox({
                width: 180,
                selectBoxWidth: 200,
                selectBoxHeight: 200, 
                  valueField : 'id',
               textField: 'name',
                treeLeafOnly:true,
                tree: { url:'${root}/order/getTreeDataXOrders.do', 
                parms:[{name:'companyId',value:123}],
                checkbox: false, 
                ajaxType: 'get', 
                 textFieldName: 'name',
                idFieldName: 'id',
                 parentIDFieldName: 'parent_id' 
                 },
                 onSelected: function (newvalue)
                 {
                      alert('选择的是' + newvalue);
                  }
            });

            var combo3 = $("#txt3").ligerComboBox({
                width: 180,
                selectBoxWidth: 200,
                selectBoxHeight: 200, 
                   valueField : 'id',
               textField: 'name',
//                isMultiSelect:false,
                tree: {
                url:'${root}/order/getTreeDataXOrders.do',
                ajaxType: 'get',
                textFieldName: 'name',
                idFieldName: 'id',
                 parentIDFieldName: 'parent_id' 
                 }
            });

            window.f_test = function ()
            {
                combo1.selectValue("节点1.1");
                combo2.selectValue("节点1.1");
                combo3.selectValue("节点1.1");
            };
        });


    </script>
</head>
<body style="padding:10px"> 
带复选框： <br />
   <input type="text" id="txt1"/>
     <br />
不带复选框： <br />
   <input type="text" id="txt2"/>
   <br />
只选择叶节点： <br />
   <input type="text" id="txt3"/>


    <input type="button" onclick="f_test()" value="按钮测试" style="margin-top:10px;padding:5px 10px;" />
 <div style="display:none;">
 
</div>
  </body>
</html>
